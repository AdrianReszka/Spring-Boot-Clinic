package com.example.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import com.example.dto.LekarzDTO;
import com.example.dto.WizytaDTO;
import com.example.services.LekarzService;
import com.example.services.WizytaService;

@RestController
@RequestMapping("/lekarze")
public class LekarzController {

    private final LekarzService lekarzService;
    
    private final WizytaService wizytaService;

    public LekarzController(LekarzService lekarzService, WizytaService wizytaService) {
        this.lekarzService = lekarzService;
        this.wizytaService = wizytaService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<LekarzDTO>>> getAllLekarze() {
        return ResponseEntity.ok(lekarzService.getAllLekarze());
    }
    
    @GetMapping("/{id}/wizyty")
    public CollectionModel<EntityModel<WizytaDTO>> getWizytyByLekarzId(@PathVariable("id") Long idLekarza) {
        return wizytaService.getWizytyByLekarzId(idLekarza);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<LekarzDTO>> getLekarzById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(lekarzService.getLekarzById(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<LekarzDTO>> createLekarz(@Valid @RequestBody LekarzDTO dto) {
        EntityModel<LekarzDTO> model = lekarzService.createLekarz(dto);
        URI location = URI.create(model.getRequiredLink("self").getHref());
        return ResponseEntity.created(location).body(model);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<LekarzDTO>> updateLekarz(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(lekarzService.updateLekarz(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLekarz(@PathVariable("id") Long id) {
        lekarzService.deleteLekarz(id);
        return ResponseEntity.ok("Lekarz with id " + id + " was deleted successfully");
    }
    
}
