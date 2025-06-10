package com.example.repositories;

import com.example.model.Choroba;
import com.example.model.PacjentChoroba;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PacjentChorobaRepository extends JpaRepository<PacjentChoroba, Long> {
	
	 @Query("SELECT DISTINCT pc.choroba FROM PacjentChoroba pc " +
	           "JOIN pc.wizyta w " +
	           "WHERE w.pacjent.idPacjenta = :idPacjenta")
	    List<Choroba> findChorobyByPacjentId(@Param("idPacjenta") Long idPacjenta);
	 
	 boolean existsById(Long id);
	 
}



