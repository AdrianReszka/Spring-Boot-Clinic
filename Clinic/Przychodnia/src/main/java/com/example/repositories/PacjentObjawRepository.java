package com.example.repositories;

import com.example.model.PacjentObjaw;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PacjentObjawRepository extends JpaRepository<PacjentObjaw, Long> {
	
	List<PacjentObjaw> findByPacjentIdPacjenta(Long idPacjenta);
	
	boolean existsById(Long id);
	
}
