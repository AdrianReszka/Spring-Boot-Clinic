package com.example.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import com.example.dto.WizytaDTO;
import com.example.services.WizytaService;

@RestController
@RequestMapping("/wizyty")
public class WizytaController {

    private final WizytaService wizytaService;

    public WizytaController(WizytaService wizytaService) {
        this.wizytaService = wizytaService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<WizytaDTO>>> getAllWizyty() {
        return ResponseEntity.ok(wizytaService.getAllWizyty());
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<WizytaDTO>> getWizytaById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(wizytaService.getWizytaById(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<WizytaDTO>> createWizyta(@Valid @RequestBody WizytaDTO dto) {
        EntityModel<WizytaDTO> model = wizytaService.createWizyta(dto);
        URI location = URI.create(model.getRequiredLink("self").getHref());
        return ResponseEntity.created(location).body(model);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<WizytaDTO>> updateWizyta(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(wizytaService.updateWizyta(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWizyta(@PathVariable("id") Long id) {
        wizytaService.deleteWizyta(id);
        return ResponseEntity.ok("Wizyta with id " + id + " was deleted successfully");
    }
}
