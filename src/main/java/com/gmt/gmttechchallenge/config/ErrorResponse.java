package com.gmt.gmttechchallenge.config;

public record ErrorResponse(int status, String message, long timestamp) {
}
