package com.example.repositories;

import com.example.model.Choroba;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChorobaRepository extends JpaRepository<Choroba, Long> {
	
	@Query("SELECT DISTINCT pc.choroba FROM PacjentObjaw po " +
		       "JOIN po.pacjent p " +
		       "JOIN p.wizyty w " +
		       "JOIN PacjentChoroba pc ON pc.wizyta = w " +
		       "WHERE po.objaw.idObjawu = :idObjawu")
		List<Choroba> findChorobyByObjawId(@Param("idObjawu") Long idObjawu);
	
	boolean existsByNazwa(String nazwa);
	
	boolean existsById(Long id);
	
}
