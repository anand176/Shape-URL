package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.NoSuchElementException;

@RestController
public class ShortUrlController {

	private final ShortUrlService service;

	public ShortUrlController(ShortUrlService service) {
		this.service = service;
	}

	// POST /api/shorten  { "longUrl": "https://example.com/some/long/path" }
	// -> 201 { "shortCode": "aB3xY9", "shortUrl": "http://localhost:8080/aB3xY9" }
	@PostMapping("/api/shorten")
	public ResponseEntity<ShortenResponse> shorten(@RequestBody ShortenRequest request) {
		ShortUrl shortUrl = service.createShortUrl(request.longUrl());
		String fullShortUrl = "http://localhost:8080/" + shortUrl.getShortCode();
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ShortenResponse(shortUrl.getShortCode(), fullShortUrl));
	}

	// GET /{shortCode} -> 302 redirect to the original long URL (no JSON body at all)
	@GetMapping("/{shortCode}")
	public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
		try {
			ShortUrl shortUrl = service.resolveAndTrack(shortCode);
			return ResponseEntity.status(HttpStatus.FOUND)
					.location(URI.create(shortUrl.getLongUrl()))
					.build();
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// GET /api/stats/{shortCode} -> click count + metadata, for a future dashboard UI
	@GetMapping("/api/stats/{shortCode}")
	public ResponseEntity<StatsResponse> stats(@PathVariable String shortCode) {
		try {
			ShortUrl shortUrl = service.getStats(shortCode);
			return ResponseEntity.ok(new StatsResponse(
					shortUrl.getShortCode(), shortUrl.getLongUrl(),
					shortUrl.getClickCount(), shortUrl.getCreatedAt()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
