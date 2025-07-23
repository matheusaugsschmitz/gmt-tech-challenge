package com.gmt.gmttechchallenge.domain;

public enum VideoSource {
    YOUTUBE,
    VIMEO;

    public boolean equals(VideoSource other){
        if (other == null)
            return false;

        return this.name().equals(other.name());
    }
}
