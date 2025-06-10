package com.example.repositories;

import com.example.model.Pacjent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacjentRepository extends JpaRepository<Pacjent, Long> {
	
	boolean existsByPesel(String pesel);
	
	boolean existsById(Long id);
	
}
