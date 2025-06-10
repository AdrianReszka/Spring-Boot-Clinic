package com.example.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.hateoas.*;
import org.springframework.stereotype.Service;

import com.example.controller.ObjawController;
import com.example.dto.ObjawDTO;
import com.example.exceptions.ConflictException;
import com.example.exceptions.NoContentException;
import com.example.model.Objaw;
import com.example.repositories.ObjawRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ObjawService {

    private final ObjawRepository objawRepository;

    public ObjawService(ObjawRepository objawRepository) {
        this.objawRepository = objawRepository;
    }

    public CollectionModel<EntityModel<ObjawDTO>> getAllObjawy() {
        List<EntityModel<ObjawDTO>> objawy = objawRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        
        return CollectionModel.of(objawy,
                linkTo(methodOn(ObjawController.class).getAllObjawy()).withSelfRel());
    }

    public EntityModel<ObjawDTO> getObjawById(Long id) {
        Objaw objaw = objawRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Objaw with ID " + id + " does not exists!"));

        return toDto(objaw);
    }

    public EntityModel<ObjawDTO> createObjaw(ObjawDTO dto) {
        if (dto.getId() != null && objawRepository.existsById(dto.getId())) {
            throw new ConflictException("Objaw with ID " + dto.getId() + " already exists!");
        }
    	
         if (objawRepository.existsByNazwa(dto.getNazwa())) {
             throw new ConflictException("Objaw with name " + dto.getNazwa() + " already exists!");
         }

        Objaw objaw = toEntity(dto);
        Objaw saved = objawRepository.save(objaw);
        return toDto(saved);
    }

    public EntityModel<ObjawDTO> updateObjaw(Long id, Map<String, Object> updates) {
        Objaw objaw = objawRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no objaw with ID: " + id));

        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No data was was provided for updating!");
        }

        updates.forEach((field, value) -> {
            switch (field) {
                case "nazwa" -> objaw.setNazwa((String) value);
                case "opis" -> objaw.setOpis((String) value);
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            }
        });

        Objaw updated = objawRepository.save(objaw);
        return toDto(updated);
    }

    public void deleteObjaw(Long id) {
        if (!objawRepository.existsById(id)) {
            throw new EntityNotFoundException("Objaw with ID " + id + " does not exists!");
        }

        objawRepository.deleteById(id);
    }

    private EntityModel<ObjawDTO> toDto(Objaw objaw) {
        if (objaw == null) {
            throw new EntityNotFoundException("Objaw was not found!");
        }
    	
        ObjawDTO dto = new ObjawDTO();
        dto.setId(objaw.getIdObjawu());
        dto.setNazwa(objaw.getNazwa());
        dto.setOpis(objaw.getOpis());

        return EntityModel.of(dto,
                linkTo(methodOn(ObjawController.class).getObjawById(objaw.getIdObjawu())).withSelfRel(),
                linkTo(methodOn(ObjawController.class).getChorobyByObjawId(objaw.getIdObjawu())).withRel("choroby"));
    }

    private Objaw toEntity(ObjawDTO dto) {
        Objaw objaw = new Objaw();
        objaw.setNazwa(dto.getNazwa());
        objaw.setOpis(dto.getOpis());
        return objaw;
    }
}
