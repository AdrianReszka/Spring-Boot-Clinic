package com.example.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.hateoas.*;
import org.springframework.stereotype.Service;

import com.example.controller.ChorobaController;
import com.example.dto.ChorobaDTO;
import com.example.dto.ObjawDTO;
import com.example.exceptions.ConflictException;
import com.example.exceptions.NoContentException;
import com.example.model.Choroba;
import com.example.repositories.ChorobaRepository;
import com.example.repositories.ObjawRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ChorobaService {

    private final ChorobaRepository chorobaRepository;
    private final ObjawRepository objawRepository;

    public ChorobaService(ChorobaRepository chorobaRepository, ObjawRepository objawRepository) {
        this.chorobaRepository = chorobaRepository;
        this.objawRepository = objawRepository;
    }

    public CollectionModel<EntityModel<ChorobaDTO>> getAllChoroby() {
        List<EntityModel<ChorobaDTO>> choroby = chorobaRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
                
        return CollectionModel.of(choroby, linkTo(methodOn(ChorobaController.class).getAllChoroby()).withSelfRel());
    }

    public List<ChorobaDTO> getChorobyByObjawId(Long idObjawu) {
        List<ChorobaDTO> choroby = chorobaRepository.findChorobyByObjawId(idObjawu)
                .stream()
                .map(c -> new ChorobaDTO(c.getIdChoroby(), c.getNazwa(), c.getOpis()))
                .collect(Collectors.toList());

        return choroby;
    }

    public List<ObjawDTO> getObjawyByChorobaId(Long idChoroby) {
        List<ObjawDTO> objawy = objawRepository.findObjawyByChorobaId(idChoroby)
                .stream()
                .map(o -> new ObjawDTO(o.getIdObjawu(), o.getNazwa(), o.getOpis()))
                .collect(Collectors.toList());

        return objawy;
    }

    public EntityModel<ChorobaDTO> getChorobaById(Long id) {
        Choroba choroba = chorobaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Choroba with ID " + id + " does not exists"));

        return toDto(choroba);
    }

    public EntityModel<ChorobaDTO> createChoroba(ChorobaDTO dto) {
        if (dto.getId() != null && chorobaRepository.existsById(dto.getId())) {
            throw new ConflictException("Choroba with ID " + dto.getId() + " already exists!");
        }
    	
         if (chorobaRepository.existsByNazwa(dto.getNazwa())) {
             throw new ConflictException("This choroba already exists!");
         }

        Choroba choroba = toEntity(dto);
        Choroba saved = chorobaRepository.save(choroba);
        return toDto(saved);
    }

    public EntityModel<ChorobaDTO> updateChoroba(Long id, Map<String, Object> updates) {
        Choroba choroba = chorobaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no choroba with ID: " + id));

        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No data was was provided for updating!");
        }

        updates.forEach((field, value) -> {
            switch (field) {
                case "nazwa" -> choroba.setNazwa((String) value);
                case "opis" -> choroba.setOpis((String) value);
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            }
        });

        Choroba updated = chorobaRepository.save(choroba);
        return toDto(updated);
    }

    public void deleteChoroba(Long id) {
        if (!chorobaRepository.existsById(id)) {
            throw new EntityNotFoundException("Choroba with ID " + id + " does not exists!");
        }

        chorobaRepository.deleteById(id);
    }

    private EntityModel<ChorobaDTO> toDto(Choroba choroba) {
        if (choroba == null) {
            throw new EntityNotFoundException("Choroba was not found!");
        }
    	
        ChorobaDTO dto = new ChorobaDTO();
        dto.setId(choroba.getIdChoroby());
        dto.setNazwa(choroba.getNazwa());
        dto.setOpis(choroba.getOpis());

        return EntityModel.of(dto,
                linkTo(methodOn(ChorobaController.class).getChorobaById(choroba.getIdChoroby())).withSelfRel(),
                linkTo(methodOn(ChorobaController.class).getObjawyByChorobaId(choroba.getIdChoroby())).withRel("objawy")
        );
    }

    private Choroba toEntity(ChorobaDTO dto) {
        Choroba choroba = new Choroba();
        choroba.setIdChoroby(dto.getId());
        choroba.setNazwa(dto.getNazwa());
        choroba.setOpis(dto.getOpis());
        return choroba;
    }
}
