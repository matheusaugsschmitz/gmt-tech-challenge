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