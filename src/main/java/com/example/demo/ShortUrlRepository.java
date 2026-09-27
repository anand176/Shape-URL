package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

	// Spring Data JPA reads this method name and generates the SQL for you —
	// no query code needed, just "findBy" + the field name.
	Optional<ShortUrl> findByShortCode(String shortCode);

}
