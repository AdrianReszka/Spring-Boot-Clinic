package com.example.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.hateoas.*;
import org.springframework.stereotype.Service;

import com.example.controller.PacjentController;
import com.example.dto.PacjentDTO;
import com.example.exceptions.ConflictException;
import com.example.exceptions.NoContentException;
import com.example.model.Pacjent;
import com.example.repositories.PacjentRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PacjentService {

    private final PacjentRepository pacjentRepository;

    public PacjentService(PacjentRepository pacjentRepository) {
        this.pacjentRepository = pacjentRepository;
    }

    public CollectionModel<EntityModel<PacjentDTO>> getAllPacjenci() {
        List<EntityModel<PacjentDTO>> pacjenci = pacjentRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return CollectionModel.of(pacjenci,
                linkTo(methodOn(PacjentController.class).getAllPacjenci()).withSelfRel());
    }

    public EntityModel<PacjentDTO> getPacjentById(Long id) {
        Pacjent pacjent = pacjentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pacjent with ID " + id + " does not exists!"));

        return toDto(pacjent);
    }

    public EntityModel<PacjentDTO> createPacjent(PacjentDTO dto) {
        if (dto.getId() != null && pacjentRepository.existsById(dto.getId())) {
            throw new ConflictException("Pacjent with ID " + dto.getId() + " already exists!");
        }
    	
         if (pacjentRepository.existsByPesel(dto.getPesel())) {
             throw new ConflictException("Pacjent with PESEL " + dto.getPesel() + " already exists!");
         }
         
         
        Pacjent pacjent = toEntity(dto);
        Pacjent saved = pacjentRepository.save(pacjent);
        return toDto(saved);
    }

    public EntityModel<PacjentDTO> updatePacjent(Long id, Map<String, Object> updates) {
        Pacjent pacjent = pacjentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no pacjent with ID: " + id));

        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No data was was provided for updating!");
        }

        updates.forEach((field, value) -> {
            switch (field) {
                case "imie" -> pacjent.setImie((String) value);
                case "nazwisko" -> pacjent.setNazwisko((String) value);
                case "pesel" -> pacjent.setPesel((String) value);
                case "dataUrodzenia" -> pacjent.setDataUrodzenia(LocalDate.parse((String) value));
                case "adres" -> pacjent.setAdres((String) value);
                case "numerTelefonu" -> pacjent.setNumerTelefonu((String) value);
                case "email" -> pacjent.setEmail((String) value);
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            }
        });

        Pacjent updated = pacjentRepository.save(pacjent);
        return toDto(updated);
    }

    public void deletePacjent(Long id) {
        if (!pacjentRepository.existsById(id)) {
            throw new EntityNotFoundException("Pacjent with ID " + id + " does not exists!");
        }

        pacjentRepository.deleteById(id);
    }

    private EntityModel<PacjentDTO> toDto(Pacjent pacjent) {
        if (pacjent == null) {
            throw new EntityNotFoundException("Pacjent was not found!");
        }

        PacjentDTO dto = new PacjentDTO();
        dto.setId(pacjent.getIdPacjenta());
        dto.setImie(pacjent.getImie());
        dto.setNazwisko(pacjent.getNazwisko());
        dto.setPesel(pacjent.getPesel());
        dto.setDataUrodzenia(pacjent.getDataUrodzenia());
        dto.setAdres(pacjent.getAdres());
        dto.setNumerTelefonu(pacjent.getNumerTelefonu());
        dto.setEmail(pacjent.getEmail());

        return EntityModel.of(dto,
                linkTo(methodOn(PacjentController.class).getPacjentById(pacjent.getIdPacjenta())).withSelfRel(),
                linkTo(methodOn(PacjentController.class).getObjawyByPacjentId(pacjent.getIdPacjenta())).withRel("objawy"),
                linkTo(methodOn(PacjentController.class).getChorobyByPacjentId(pacjent.getIdPacjenta())).withRel("choroby"),
                linkTo(methodOn(PacjentController.class).getWizytyByPacjentId(pacjent.getIdPacjenta())).withRel("wizyty"));
    }

    private Pacjent toEntity(PacjentDTO dto) {
        Pacjent entity = new Pacjent();
        entity.setImie(dto.getImie());
        entity.setNazwisko(dto.getNazwisko());
        entity.setPesel(dto.getPesel());
        entity.setDataUrodzenia(dto.getDataUrodzenia());
        entity.setAdres(dto.getAdres());
        entity.setNumerTelefonu(dto.getNumerTelefonu());
        entity.setEmail(dto.getEmail());
        return entity;
    }
}
