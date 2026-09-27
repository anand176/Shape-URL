package com.example.demo;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.NoSuchElementException;

@Service
public class ShortUrlService {

	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int CODE_LENGTH = 6;

	private final ShortUrlRepository repository;
	private final SecureRandom random = new SecureRandom();

	// Constructor injection — Spring hands us the repository bean automatically
	public ShortUrlService(ShortUrlRepository repository) {
		this.repository = repository;
	}

	public ShortUrl createShortUrl(String longUrl) {
		String code;
		do {
			code = generateCode();
		} while (repository.findByShortCode(code).isPresent()); // retry on the rare collision

		return repository.save(new ShortUrl(code, longUrl));
	}

	public ShortUrl resolveAndTrack(String shortCode) {
		ShortUrl shortUrl = findOrThrow(shortCode);
		shortUrl.setClickCount(shortUrl.getClickCount() + 1);
		return repository.save(shortUrl);
	}

	public ShortUrl getStats(String shortCode) {
		return findOrThrow(shortCode);
	}

	private ShortUrl findOrThrow(String shortCode) {
		return repository.findByShortCode(shortCode)
				.orElseThrow(() -> new NoSuchElementException("No URL found for code: " + shortCode));
	}

	private String generateCode() {
		StringBuilder sb = new StringBuilder(CODE_LENGTH);
		for (int i = 0; i < CODE_LENGTH; i++) {
			sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
		}
		return sb.toString();
	}

}
