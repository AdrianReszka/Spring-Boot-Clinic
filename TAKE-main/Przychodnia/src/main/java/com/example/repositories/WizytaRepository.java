package com.example.repositories;

import com.example.model.Lekarz;
import com.example.model.Wizyta;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WizytaRepository extends JpaRepository<Wizyta, Long> {
	
	List<Wizyta> findByLekarzIdLekarza(Long idLekarza);
	
	List<Wizyta> findByPacjentIdPacjenta(Long idPacjenta);
	
	boolean existsById(Long id);
	
	boolean existsByLekarzAndTermin(Lekarz lekarz, LocalDateTime termin);
	
}
