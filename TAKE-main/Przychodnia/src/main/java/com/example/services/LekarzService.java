package com.example.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.hateoas.*;
import org.springframework.stereotype.Service;

import com.example.controller.LekarzController;
import com.example.dto.LekarzDTO;
import com.example.exceptions.ConflictException;
import com.example.exceptions.NoContentException;
import com.example.model.Lekarz;
import com.example.repositories.LekarzRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LekarzService {

    private final LekarzRepository lekarzRepository;

    public LekarzService(LekarzRepository lekarzRepository) {
        this.lekarzRepository = lekarzRepository;
    }

    public CollectionModel<EntityModel<LekarzDTO>> getAllLekarze() {
        List<EntityModel<LekarzDTO>> lekarze = lekarzRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        
        return CollectionModel.of(lekarze,
                linkTo(methodOn(LekarzController.class).getAllLekarze()).withSelfRel());
    }

    public EntityModel<LekarzDTO> getLekarzById(Long id) {
        Lekarz lekarz = lekarzRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lekarz with ID " + id + " does not exists!"));

        return toDto(lekarz);
    }

    public EntityModel<LekarzDTO> createLekarz(LekarzDTO dto) {
        if (dto.getId() != null && lekarzRepository.existsById(dto.getId())) {
            throw new ConflictException("Lekarz with ID " + dto.getId() + " already exists!");
        }
    	
        if (lekarzRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("Lekarz with this email address " + dto.getEmail() + " already exists!");
        }

        Lekarz lekarz = toEntity(dto);
        Lekarz saved = lekarzRepository.save(lekarz);
        return toDto(saved);
    }

    public EntityModel<LekarzDTO> updateLekarz(Long id, Map<String, Object> updates) {
        Lekarz lekarz = lekarzRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no lekarz with ID: " + id));

        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No data was was provided for updating!");
        }

        updates.forEach((field, value) -> {
            switch (field) {
                case "imie" -> lekarz.setImie((String) value);
                case "nazwisko" -> lekarz.setNazwisko((String) value);
                case "specjalizacja" -> lekarz.setSpecjalizacja((String) value);
                case "email" -> lekarz.setEmail((String) value);
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            }
        });

        Lekarz updated = lekarzRepository.save(lekarz);
        return toDto(updated);
    }

    public void deleteLekarz(Long id) {
        if (!lekarzRepository.existsById(id)) {
            throw new EntityNotFoundException("Lekarz with ID " + id + " does not exists!");
        }

        lekarzRepository.deleteById(id);
    }
        
    private EntityModel<LekarzDTO> toDto(Lekarz lekarz) {
        if (lekarz == null) {
            throw new EntityNotFoundException("Lekarz was not found!");
        }
    	
        LekarzDTO dto = new LekarzDTO();
        dto.setId(lekarz.getIdLekarza());
        dto.setImie(lekarz.getImie());
        dto.setNazwisko(lekarz.getNazwisko());
        dto.setSpecjalizacja(lekarz.getSpecjalizacja());
        dto.setEmail(lekarz.getEmail());

        return EntityModel.of(dto,
                linkTo(methodOn(LekarzController.class).getLekarzById(lekarz.getIdLekarza())).withSelfRel(),
                linkTo(methodOn(LekarzController.class).getWizytyByLekarzId(lekarz.getIdLekarza())).withRel("wizyty"));
    }

    private Lekarz toEntity(LekarzDTO dto) {
        Lekarz lekarz = new Lekarz();
        lekarz.setImie(dto.getImie());
        lekarz.setNazwisko(dto.getNazwisko());
        lekarz.setSpecjalizacja(dto.getSpecjalizacja());
        lekarz.setEmail(dto.getEmail());
        return lekarz;
    }
}
