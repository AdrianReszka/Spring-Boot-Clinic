package com.example.repositories;

import com.example.model.Lekarz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LekarzRepository extends JpaRepository<Lekarz, Long> {
	
	boolean existsByEmail(String email);
	
	boolean existsById(Long id);
		
}
