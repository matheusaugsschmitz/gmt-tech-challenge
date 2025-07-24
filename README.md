## Table of Contents
- [About](#about)
- [How to Run](#how-to-run)
- [How to use API](#how-to-use-api)
- [Design Choices](#design-choices)

## About Repository
This project is an implementation of a basic video metadata compilation service that 
is intended to retrieve data from mocked APIs, store in an in-memory storage
and make this available it through a set of API endpoints. 

**This project was made for a code challenge.**

## About Project
Built with Java 24 with Spring Boot 3.5.3

OpenAPI Specs available in [this file]()

## How to Run
> **For all following steps, execute the respective commands in a terminal inside
> the folder that contains the source code of this project.**

### Build JAR file
```
$ ./gradlew build
```
The JAR file will be placed in the `./build/libs` folder.

### Run standalone

```
$ java -jar ./build/libs/gmt-tech-challenge-0.0.1-SNAPSHOT.jar
```

### Run with Docker

```
# Build the Docker image
$ docker build -t gmt-tech-challenge .

# Run the Docker container
$ docker run -p 8080:8080 gmt-tech-challenge
```

## How to Run tests
```
$ ./gradlew test
```

## How to use API
All endpoints in this API are restricted to authenticated access except from the login endpoint. 
The first step then is to use the Authentication endpoint to log in and retrieve a valid access token to be used in the 
other requests.

### Get access token
The API has two in-memory users, both with the same password "password".
- Basic User: username `user`
- Admin User: username `admin`

```
$ curl --location 'http://localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data '{
    "username": "user",
    "password": "password"
}'
```

### Import videos (example of auth token usage)
Since this service doesn't call real APIs, there are only a few mocked video metadata to be "imported".
The following request will get all mocked video metadata loaded up.

> Before executing the following command, replace `<access_token>` with the actual token returned from the `/auth/token` endpoint
```
$ curl --location 'http://localhost:8080/videos/import' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <access_token>' \
--data '{
    "videoIdentifiers": [
        {
            "videoId": "abc123",
            "source": "YOUTUBE"
        },
        {
            "videoId": "def456",
            "source": "YOUTUBE"
        },
        {
            "videoId": "ghi789",
            "source": "YOUTUBE"
        },
        {
            "videoId": "jkl123",
            "source": "YOUTUBE"
        },
        {
            "videoId": "123456",
            "source": "VIMEO"
        },
        {
            "videoId": "234567",
            "source": "VIMEO"
        },
        {
            "videoId": "345678",
            "source": "VIMEO"
        },
        {
            "videoId": "456789",
            "source": "VIMEO"
        }
    ]
}'
```

## Design Choices
In this section I'll dig into some of the design choices I made for this code challenge, but have in mind it doesn't 
mean I would to that for all scenarios, within the code I've added a few disclosure comments here and there where I 
also mention about this in more specific situations, so I recommend taking at look at them if said choice is not 
described in here.

### Auth process
The challenge requires authentication to be handled with JWT tokens, but not implies as a requirement the usage of Oauth2, for example, 
which would be an extra dependency to have and one of the goals was to be a standalone application that can run with just itself.

That being said, I opted out for adapting the Spring Security authentication flow to generate and validate JWT tokens rather
than just use the JSESSIONIDs.

For that I had to implement several classes:
1. `AuthenticationApi.java`: Controller created to receive POST HTTP calls to `/auth/token` with credentials to retrieve a valid token.
2. `InMemoryUserStorageConfig.java`: Bean specification to mock 2 users up using already existing Spring Security user-handling code.*
3. `JWTSecurityFilter.java`: Custom filter to be used by Spring to validate the Bearer token in the `Authorization` in every API call.
4. `WebSecurityConfig.java`: Actual Security setup to force all endpoints to require authentication except from `/auth/token` and to require `ADMIN` role to interact with the `/videos/import` endpoint.

> \*In a real-world situation password would never be plain text nor handled this way so besides from needing
an extra implementation to have users stored somewhere else, it would be also required to add configuration
to use BCrypt for password encryption.

### Video Metadata Persistence
To keep this API having no dependencies I've created a simple `Map` in `VideoMetadataRepository.java` that stores all metadata
imported from all sources, where the key is the ID and the value is the record with all relevant data.

Since by having multiple sources we might face ID collision, I decided to not use the ID from the source as the unique identifier. 
Instead, we have an extra field that is the combination of both source name and video ID (e.g. "YOUTUBE:123554") converted into a UUID,
which allow us to avoid having multiple records for the same video with ease.

> By having an extra ID field of our own, I also created an extra endpoint for fetch data by ID that is not part of the
> initial requirement of the code challenge.
> 
> The code challenge requires an endpoint to retrieve video metadata by ID, but the ID used for importing metadata is the ID
> from the source and once the actual is stored, the easy access is through the before-mentioned UUID.
> 
> So there are 3 ways to retrieve video metadata using IDs:
> 1. `/videos/{id}`: In which the ID is the UUID from our domain
> 2. `/videos/{source}/{id}`: In which the ID is the one from the source, but we also require the source to be specified
> 3. `/videos?sourceId={id}`: In which the ID is the one from the source, but DOES NOT REQUIRE source identification. 
>    - This endpoint have many filters and all of them are optional, so when calling it like this it will iterate over all
>    records and filter by the ID from the source.

### Client (Mocked external APIs)
As the concept of this challenge is to retrieve data from platforms such as YouTube and Vimeo, but not as advanced that 
would actually have dependencies to run, I have decided to make `MockedYoutubeClient.java` and `MockedVimeoClient.java`
that replicates the payload and requests that would be needed to interact with the real APIs, but that only retrieve data
from a pre-loaded in-memory data store.

### Adapters (Interactions with Mocked Clients)
The goal when building this API was to have the type of core implementation that could be used in some extension in 
real-world situations, so I implemented `VideoSourceInterface.java` as an abstract interface for classes that should
handle the actual communication with "external" (in brackets because they are not actual external) dependencies.

By defining a middle state to be met and having all individual implementations handling their own specificities we allow
the code to be easily expandable (in case we want to add a 3rd video source) and easier to maintain since we are separating
our own domain to third-party ones. 

For each source there is an implementation of that Interface, for YouTube is `YoutubeAdapter.java` and for Vimeo is 
`VimeoAdapter.java`, and in case we want another one, just add another value in the `VideoSource.java` ENUM, create 
another Adapter class that implements `VideoSourceInterface.java` and everything else will just work. 

For that to work, each one of the adapters must have two methods:
1. `getSourceMethod()`: This method is responsible for identifying which value in the `VideoSource.java` ENUM corresponds to said implementation.
2. `fetchBatchMetadata()`: This method is responsible for retrieving data from the source and converting to our own domain class, the `VideoMetadata.java`.
Is up to the implementation to handle the source API as it can, if it need to do multiple API calls or just one, for the
interface or what is above it, it must not matter.

> **Important note**
> 
> Even though the Adapters is a good concept to be used in real-world scenarios, importing data should not be handled
> like this.
>
> In complete approach of this problem I would opt to have some sort of orchestration and use events to trigger async
> importing for several reasons, such as (but not limited to):
> 1. **Retries**: If something goes wrong while calling external resources, this approach would just lose the import request data and do nothing, so being able to retry is a must.
> 2. **Invalid IDs**: Since we receive batches of IDs, if ONE doesn't exists on the other side (video source) or if the ID is invalid, we must have a way to know and the only way 
> for that is to track individual progress of the import, even if the actual import is made in batch.
> 3. **Load Distribution**: This implementation will handle all import process on the same instance that received the HTTP request,
> which might be fine in low usage, but can create problems in environments with high demand, so offloading the thread requires
> said events so instances will import data upon resource availability.
> 4. **API Limits**: External APIs often have limits on how much one can interact with them per-minute, per-hour, per-day...
> so making the process not rely solely on one thread or one instance, allow us to get multiple import requests and process
> them all at once, fetching more data in one pull rather than in several API calls. Some APIs even charge for usage, so
> it's also about costs savings.

### Error handling
As a simple approach to make error responses standard, I've created `GlobalExcpetionHandler.java` to handle expected and
unexpected exceptions and format it in a reasonable way to keep all the same endpoints with a standard error response format
and to avoid extrapolating to whoever is calling the API too much information about the internal error.

By the time of this test I couldn't polish as much as I wanted it so it's still a bit rough and generic, but returning 
proper status codes with somewhat meaning messages is already a step further to having a useful API.