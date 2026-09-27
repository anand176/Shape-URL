package com.example.demo;

import java.time.Instant;

public record StatsResponse(String shortCode, String longUrl, long clickCount, Instant createdAt) {
}
