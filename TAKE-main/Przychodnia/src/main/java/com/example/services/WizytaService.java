package com.example.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.hateoas.*;
import org.springframework.stereotype.Service;

import com.example.controller.LekarzController;
import com.example.controller.PacjentController;
import com.example.controller.WizytaController;
import com.example.dto.WizytaDTO;
import com.example.exceptions.ConflictException;
import com.example.model.Lekarz;
import com.example.model.Pacjent;
import com.example.model.Wizyta;
import com.example.repositories.LekarzRepository;
import com.example.repositories.PacjentRepository;
import com.example.repositories.WizytaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class WizytaService {

    private final WizytaRepository wizytaRepository;
    private final PacjentRepository pacjentRepository;
    private final LekarzRepository lekarzRepository;

    public WizytaService(WizytaRepository wizytaRepository, PacjentRepository pacjentRepository,
                         LekarzRepository lekarzRepository) {
        this.wizytaRepository = wizytaRepository;
        this.pacjentRepository = pacjentRepository;
        this.lekarzRepository = lekarzRepository;
    }

    public CollectionModel<EntityModel<WizytaDTO>> getAllWizyty() {
        List<EntityModel<WizytaDTO>> wizyty = wizytaRepository.findAll().stream()
                .map(w -> toDto(w, false, false))
                .collect(Collectors.toList());

        return CollectionModel.of(wizyty,
                linkTo(methodOn(WizytaController.class).getAllWizyty()).withSelfRel());
    }

    public CollectionModel<EntityModel<WizytaDTO>> getWizytyByLekarzId(Long idLekarza) {
        List<EntityModel<WizytaDTO>> list = wizytaRepository.findByLekarzIdLekarza(idLekarza)
                .stream()
                .map(w -> toDto(w, false, true))
                .collect(Collectors.toList());

        return CollectionModel.of(list,
                linkTo(methodOn(LekarzController.class).getWizytyByLekarzId(idLekarza)).withSelfRel());
    }

    public CollectionModel<EntityModel<WizytaDTO>> getWizytyByPacjentId(Long idPacjenta) {
        List<EntityModel<WizytaDTO>> list = wizytaRepository.findByPacjentIdPacjenta(idPacjenta).stream()
                .map(w -> toDto(w, true, false))
                .collect(Collectors.toList());

        return CollectionModel.of(list,
                linkTo(methodOn(PacjentController.class).getWizytyByPacjentId(idPacjenta)).withSelfRel());
    }

    public EntityModel<WizytaDTO> getWizytaById(Long id) {
        Wizyta wizyta = wizytaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wizyta with ID " + id + " does not exists!"));

        return toDto(wizyta, false, false);
    }

    public EntityModel<WizytaDTO> createWizyta(WizytaDTO dto) {
        if (dto.getId() != null && wizytaRepository.existsById(dto.getId())) {
            throw new ConflictException("Wizyta with ID " + dto.getId() + " already exists!");
        }

        Wizyta wizyta = toEntity(dto);

        if (wizytaRepository.existsByLekarzAndTermin(wizyta.getLekarz(), wizyta.getTermin())) {
            throw new ConflictException("Lekarz already has a wizyta at this time.");
        }

        Wizyta saved = wizytaRepository.save(wizyta);
        return toDto(saved, false, false);
    }

    public EntityModel<WizytaDTO> updateWizyta(Long id, Map<String, Object> updates) {
        Wizyta wizyta = wizytaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no wizyta with ID: " + id));

        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No data was was provided for updating!");
        }

        updates.forEach((field, value) -> {
            switch (field) {
                case "idPacjenta" -> {
                    Long pacjentId = ((Number) value).longValue();
                    Pacjent pacjent = pacjentRepository.findById(pacjentId)
                            .orElseThrow(() -> new EntityNotFoundException("Pacjent with ID " + pacjentId + " does not exists!"));
                    wizyta.setPacjent(pacjent);
                }
                case "idLekarza" -> {
                    Long lekarzId = ((Number) value).longValue();
                    Lekarz lekarz = lekarzRepository.findById(lekarzId)
                            .orElseThrow(() -> new EntityNotFoundException("Lekarz with ID " + lekarzId + " does not exists!"));
                    wizyta.setLekarz(lekarz);
                }
                case "termin" -> wizyta.setTermin(LocalDateTime.parse((String) value));
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            }
        });

        Wizyta updated = wizytaRepository.save(wizyta);
        return toDto(updated, false, false);
    }

    public void deleteWizyta(Long id) {
        if (!wizytaRepository.existsById(id)) {
            throw new EntityNotFoundException("Wizyta with ID " + id + " does not exists!");
        }

        wizytaRepository.deleteById(id);
    }

    private EntityModel<WizytaDTO> toDto(Wizyta wizyta, boolean hidePacjent, boolean hideLekarz) {
        if (wizyta == null) {
            throw new EntityNotFoundException("Wizyta was not found!");
        }

        WizytaDTO dto = new WizytaDTO();
        dto.setId(wizyta.getIdWizyty());
        dto.setTermin(wizyta.getTermin());
        dto.setIdPacjenta(hidePacjent ? null : wizyta.getPacjent().getIdPacjenta());
        dto.setIdLekarza(hideLekarz ? null : wizyta.getLekarz().getIdLekarza());

        EntityModel<WizytaDTO> model = EntityModel.of(dto,
                linkTo(methodOn(WizytaController.class).getWizytaById(wizyta.getIdWizyty())).withSelfRel());

        if (!hidePacjent) {
            model.add(linkTo(methodOn(PacjentController.class)
                    .getPacjentById(wizyta.getPacjent().getIdPacjenta()))
                    .withRel("pacjent"));
        }

        if (!hideLekarz) {
            model.add(linkTo(methodOn(LekarzController.class)
                    .getLekarzById(wizyta.getLekarz().getIdLekarza()))
                    .withRel("lekarz"));
        }

        return model;
    }

    public Wizyta toEntity(WizytaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("WizytaDTO is null");
        }

        Pacjent pacjent = pacjentRepository.findById(dto.getIdPacjenta())
                .orElseThrow(() -> new EntityNotFoundException("Pacjent with ID " + dto.getIdPacjenta() + " does not exist"));

        Lekarz lekarz = lekarzRepository.findById(dto.getIdLekarza())
                .orElseThrow(() -> new EntityNotFoundException("Lekarz with ID " + dto.getIdLekarza() + " does not exist"));

        Wizyta wizyta = new Wizyta();
        wizyta.setTermin(dto.getTermin());
        wizyta.setPacjent(pacjent);
        wizyta.setLekarz(lekarz);

        return wizyta;
    }
}
