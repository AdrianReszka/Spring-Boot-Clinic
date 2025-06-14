package com.example.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.hateoas.*;
import org.springframework.stereotype.Service;

import com.example.controller.ObjawController;
import com.example.controller.PacjentController;
import com.example.controller.PacjentObjawController;
import com.example.dto.PacjentObjawDTO;
import com.example.exceptions.ConflictException;
import com.example.exceptions.NoContentException;
import com.example.model.Objaw;
import com.example.model.Pacjent;
import com.example.model.PacjentObjaw;
import com.example.repositories.ObjawRepository;
import com.example.repositories.PacjentObjawRepository;
import com.example.repositories.PacjentRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PacjentObjawService {

    private final PacjentObjawRepository pacjentObjawRepository;
    private final PacjentRepository pacjentRepository;
    private final ObjawRepository objawRepository;

    public PacjentObjawService(PacjentObjawRepository pacjentObjawRepository,
                               PacjentRepository pacjentRepository,
                               ObjawRepository objawRepository) {
        this.pacjentObjawRepository = pacjentObjawRepository;
        this.pacjentRepository = pacjentRepository;
        this.objawRepository = objawRepository;
    }

    public CollectionModel<EntityModel<PacjentObjawDTO>> getAll() {
        List<EntityModel<PacjentObjawDTO>> list = pacjentObjawRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        
        return CollectionModel.of(list,
                linkTo(methodOn(PacjentObjawController.class).getAll()).withSelfRel());
    }

    public List<PacjentObjawDTO> getObjawyByPacjentId(Long idPacjenta) {
        List<PacjentObjawDTO> objawy = pacjentObjawRepository.findByPacjentIdPacjenta(idPacjenta)
                .stream()
                .map(po -> {
                    PacjentObjawDTO dto = new PacjentObjawDTO();
                    dto.setIdPacjenta(null); 
                    dto.setIdObjawu(po.getObjaw().getIdObjawu());
                    dto.setNazwaObjawu(po.getObjaw().getNazwa());
                    dto.setDataWystapienia(po.getDataWystapienia());
                    return dto;
                })
                .collect(Collectors.toList());

        return objawy;
    }

    public EntityModel<PacjentObjawDTO> getById(Long id) {
        PacjentObjaw obj = pacjentObjawRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PacjentObjaw with ID " + id + " does not exists!"));

        return toDto(obj);
    }

    public EntityModel<PacjentObjawDTO> create(PacjentObjawDTO dto) {
        if (dto.getId() != null && pacjentObjawRepository.existsById(dto.getId())) {
            throw new ConflictException("PacjentObjaw with ID " + dto.getId() + " already exists!");
        }
    	
        Pacjent pacjent = pacjentRepository.findById(dto.getIdPacjenta())
                .orElseThrow(() -> new EntityNotFoundException("Pacjent with ID " + dto.getIdPacjenta() + " does not exists!"));
        Objaw objaw = objawRepository.findById(dto.getIdObjawu())
                .orElseThrow(() -> new EntityNotFoundException("Objaw with ID " + dto.getIdObjawu() + " does not exists!"));

        PacjentObjaw entity = new PacjentObjaw();
        entity.setPacjent(pacjent);
        entity.setObjaw(objaw);
        entity.setDataWystapienia(dto.getDataWystapienia());

        PacjentObjaw saved = pacjentObjawRepository.save(entity);
        return toDto(saved);
    }

    public EntityModel<PacjentObjawDTO> update(Long id, Map<String, Object> updates) {
        PacjentObjaw obj = pacjentObjawRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no PacjentObjaw with ID: " + id));

        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No data was was provided for updating!");
        }

        updates.forEach((field, value) -> {
            switch (field) {
                case "idPacjenta" -> {
                    Long pid = ((Number) value).longValue();
                    Pacjent pacjent = pacjentRepository.findById(pid)
                            .orElseThrow(() -> new EntityNotFoundException("Pacjent with ID " + pid + " does not exists!"));
                    obj.setPacjent(pacjent);
                }
                case "idObjawu" -> {
                    Long oid = ((Number) value).longValue();
                    Objaw objaw = objawRepository.findById(oid)
                            .orElseThrow(() -> new EntityNotFoundException("Objaw with ID " + oid + " does not exists!"));
                    obj.setObjaw(objaw);
                }
                case "dataWystapienia" -> obj.setDataWystapienia(LocalDate.parse((String) value));
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            }
        });

        PacjentObjaw updated = pacjentObjawRepository.save(obj);
        return toDto(updated);
    }

    public void delete(Long id) {
        if (!pacjentObjawRepository.existsById(id)) {
            throw new EntityNotFoundException("PacjentObjaw with ID " + id + " does not exists!");
        }

        pacjentObjawRepository.deleteById(id);
    }

    private EntityModel<PacjentObjawDTO> toDto(PacjentObjaw obj) {
        if (obj == null) {
            throw new EntityNotFoundException("PacjentObjaw was not found!");
        }

        PacjentObjawDTO dto = new PacjentObjawDTO();
        dto.setId(obj.getIdPacjentObjaw());
        dto.setIdPacjenta(obj.getPacjent().getIdPacjenta());
        dto.setIdObjawu(obj.getObjaw().getIdObjawu());
        dto.setDataWystapienia(obj.getDataWystapienia());

        return EntityModel.of(dto,
                linkTo(methodOn(PacjentObjawController.class).getById(obj.getIdPacjentObjaw())).withSelfRel(),
                linkTo(methodOn(PacjentController.class).getPacjentById(obj.getPacjent().getIdPacjenta())).withRel("pacjent"),
                linkTo(methodOn(ObjawController.class).getObjawById(obj.getObjaw().getIdObjawu())).withRel("objaw"));
    }
}
