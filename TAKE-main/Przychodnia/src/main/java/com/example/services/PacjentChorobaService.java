package com.example.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.hateoas.*;
import org.springframework.stereotype.Service;

import com.example.controller.ChorobaController;
import com.example.controller.PacjentChorobaController;
import com.example.controller.PacjentController;
import com.example.controller.WizytaController;
import com.example.dto.ChorobaDTO;
import com.example.dto.PacjentChorobaDTO;
import com.example.exceptions.ConflictException;
import com.example.exceptions.NoContentException;
import com.example.model.Choroba;
import com.example.model.PacjentChoroba;
import com.example.model.Wizyta;
import com.example.repositories.ChorobaRepository;
import com.example.repositories.PacjentChorobaRepository;
import com.example.repositories.WizytaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PacjentChorobaService {

    private final PacjentChorobaRepository pacjentChorobaRepository;
    private final WizytaRepository wizytaRepository;
    private final ChorobaRepository chorobaRepository;

    public PacjentChorobaService(PacjentChorobaRepository pacjentChorobaRepository,
                                 WizytaRepository wizytaRepository,
                                 ChorobaRepository chorobaRepository) {
        this.pacjentChorobaRepository = pacjentChorobaRepository;
        this.wizytaRepository = wizytaRepository;
        this.chorobaRepository = chorobaRepository;
    }

    public CollectionModel<EntityModel<PacjentChorobaDTO>> getAll() {
        List<EntityModel<PacjentChorobaDTO>> list = pacjentChorobaRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        
        return CollectionModel.of(list,
                linkTo(methodOn(PacjentChorobaController.class).getAll()).withSelfRel());
    }

    public List<ChorobaDTO> getChorobyByPacjentId(Long idPacjenta) {
        List<ChorobaDTO> choroby = pacjentChorobaRepository.findChorobyByPacjentId(idPacjenta)
                .stream()
                .map(c -> new ChorobaDTO(c.getIdChoroby(), c.getNazwa(), c.getOpis()))
                .collect(Collectors.toList());

        return choroby;
    }

    public EntityModel<PacjentChorobaDTO> getById(Long id) {
        PacjentChoroba pc = pacjentChorobaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Connection pacjent-choroba with ID " + id + " does not exists!"));

        return toDto(pc);
    }

    public EntityModel<PacjentChorobaDTO> create(PacjentChorobaDTO dto) {
        if (dto.getId() != null && pacjentChorobaRepository.existsById(dto.getId())) {
            throw new ConflictException("PacjentChoroba with ID " + dto.getId() + " already exists!");
        }
    	
        Wizyta wizyta = wizytaRepository.findById(dto.getIdWizyty())
                .orElseThrow(() -> new EntityNotFoundException("Wizyta with ID " + dto.getIdWizyty() + " does not exists!"));

        Choroba choroba = chorobaRepository.findById(dto.getIdChoroby())
                .orElseThrow(() -> new EntityNotFoundException("Choroba with ID " + dto.getIdChoroby() + " does not exists!"));

        PacjentChoroba entity = new PacjentChoroba();
        entity.setWizyta(wizyta);
        entity.setChoroba(choroba);

        PacjentChoroba saved = pacjentChorobaRepository.save(entity);
        return toDto(saved);
    }

    public EntityModel<PacjentChorobaDTO> update(Long id, Map<String, Object> updates) {
        PacjentChoroba pc = pacjentChorobaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no pacjent-choroba with ID: " + id));

        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No data was was provided for updating!");
        }

        updates.forEach((field, value) -> {
            switch (field) {
                case "idWizyty" -> {
                    Long wid = ((Number) value).longValue();
                    Wizyta wizyta = wizytaRepository.findById(wid)
                            .orElseThrow(() -> new EntityNotFoundException("Wizyta with ID " + wid + " does not exists!"));
                    pc.setWizyta(wizyta);
                }
                case "idChoroby" -> {
                    Long cid = ((Number) value).longValue();
                    Choroba choroba = chorobaRepository.findById(cid)
                            .orElseThrow(() -> new EntityNotFoundException("Choroba with ID " + cid + " does not exists!"));
                    pc.setChoroba(choroba);
                }
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            }
        });

        PacjentChoroba updated = pacjentChorobaRepository.save(pc);
        return toDto(updated);
    }

    public void delete(Long id) {
        if (!pacjentChorobaRepository.existsById(id)) {
            throw new EntityNotFoundException("Connection pacjent-choroba with ID " + id + " does not exists!");
        }

        pacjentChorobaRepository.deleteById(id);
    }

    private EntityModel<PacjentChorobaDTO> toDto(PacjentChoroba pc) {
        if (pc == null) {
            throw new EntityNotFoundException("PacjentChoroba was not found!");
        }

        PacjentChorobaDTO dto = new PacjentChorobaDTO();
        dto.setId(pc.getIdPacjentChoroba());
        dto.setIdWizyty(pc.getWizyta().getIdWizyty());
        dto.setIdChoroby(pc.getChoroba().getIdChoroby());

        return EntityModel.of(dto,
                linkTo(methodOn(PacjentChorobaController.class).getById(pc.getIdPacjentChoroba())).withSelfRel(),
                linkTo(methodOn(ChorobaController.class).getChorobaById(pc.getChoroba().getIdChoroby())).withRel("choroba"),
                linkTo(methodOn(WizytaController.class).getWizytaById(pc.getWizyta().getIdWizyty())).withRel("wizyta"),
                linkTo(methodOn(PacjentController.class).getPacjentById(pc.getWizyta().getPacjent().getIdPacjenta())).withRel("pacjent"));
    }
}
