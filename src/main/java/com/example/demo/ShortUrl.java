package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "short_urls")
public class ShortUrl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String shortCode;

	@Column(nullable = false, length = 2048)
	private String longUrl;

	@Column(nullable = false)
	private long clickCount = 0;

	@Column(nullable = false)
	private Instant createdAt = Instant.now();

	// JPA requires a no-arg constructor to build objects when reading from the DB
	protected ShortUrl() {
	}

	public ShortUrl(String shortCode, String longUrl) {
		this.shortCode = shortCode;
		this.longUrl = longUrl;
	}

	public Long getId() {
		return id;
	}

	public String getShortCode() {
		return shortCode;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public long getClickCount() {
		return clickCount;
	}

	public void setClickCount(long clickCount) {
		this.clickCount = clickCount;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

}
