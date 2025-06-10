package com.example.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.example.dto.ChorobaDTO;
import com.example.dto.PacjentDTO;
import com.example.dto.PacjentObjawDTO;
import com.example.dto.WizytaDTO;
import com.example.services.PacjentChorobaService;
import com.example.services.PacjentObjawService;
import com.example.services.PacjentService;
import com.example.services.WizytaService;

@RestController
@RequestMapping("/pacjenci")
public class PacjentController {

    private final PacjentService pacjentService;
    private final PacjentObjawService pacjentObjawService;
    private final PacjentChorobaService pacjentChorobaService;
    private final WizytaService wizytaService;

    public PacjentController(PacjentService pacjentService, PacjentObjawService pacjentObjawService, PacjentChorobaService pacjentChorobaService, WizytaService wizytaService) {
        this.pacjentService = pacjentService;
        this.pacjentObjawService = pacjentObjawService;
        this.pacjentChorobaService = pacjentChorobaService;
        this.wizytaService = wizytaService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PacjentDTO>>> getAllPacjenci() {
        return ResponseEntity.ok(pacjentService.getAllPacjenci());
    }
    
    @GetMapping("/{id}/wizyty")
    public CollectionModel<EntityModel<WizytaDTO>> getWizytyByPacjentId(@PathVariable("id") Long idPacjenta) {
        return wizytaService.getWizytyByPacjentId(idPacjenta);
    }
    
    @GetMapping("/{id}/wizyty/pacjent_choroba")
    public ResponseEntity<List<ChorobaDTO>> getChorobyByPacjentId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(pacjentChorobaService.getChorobyByPacjentId(id));
    }

    @GetMapping("/{id}/pacjent_objaw")
    public ResponseEntity<List<PacjentObjawDTO>> getObjawyByPacjentId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(pacjentObjawService.getObjawyByPacjentId(id));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PacjentDTO>> getPacjentById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(pacjentService.getPacjentById(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<PacjentDTO>> createPacjent(@Valid @RequestBody PacjentDTO dto) {
        EntityModel<PacjentDTO> model = pacjentService.createPacjent(dto);
        URI location = URI.create(model.getRequiredLink("self").getHref());
        return ResponseEntity.created(location).body(model);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<PacjentDTO>> updatePacjent(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(pacjentService.updatePacjent(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePacjent(@PathVariable("id") Long id) {
        pacjentService.deletePacjent(id);
        return ResponseEntity.ok("Pacjent with id " + id + " was deleted successfully");
    }
}
