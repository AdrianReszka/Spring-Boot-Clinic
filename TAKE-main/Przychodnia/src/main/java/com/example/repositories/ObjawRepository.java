package com.example.repositories;

import com.example.model.Objaw;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ObjawRepository extends JpaRepository<Objaw, Long> {
	
	 @Query("SELECT DISTINCT po.objaw FROM PacjentChoroba pc " +
	           "JOIN pc.wizyta w " +
	           "JOIN w.pacjent p " +
	           "JOIN p.pacjentObjawy po " +
	           "WHERE pc.choroba.idChoroby = :idChoroby")
	    List<Objaw> findObjawyByChorobaId(@Param("idChoroby") Long idChoroby);
	 
	 boolean existsByNazwa(String nazwa);
	 
	 boolean existsById(Long id);

}
