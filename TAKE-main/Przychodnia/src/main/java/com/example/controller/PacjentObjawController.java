package com.example.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import com.example.dto.PacjentObjawDTO;
import com.example.services.PacjentObjawService;

@RestController
@RequestMapping("/pacjent_objaw")
public class PacjentObjawController {

    private final PacjentObjawService service;

    public PacjentObjawController(PacjentObjawService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PacjentObjawDTO>>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PacjentObjawDTO>> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<PacjentObjawDTO>> create(@Valid @RequestBody PacjentObjawDTO dto) {
        EntityModel<PacjentObjawDTO> model = service.create(dto);
        URI location = URI.create(model.getRequiredLink("self").getHref());
        return ResponseEntity.created(location).body(model);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<PacjentObjawDTO>> update(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(service.update(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok("PacjentObjaw with id " + id + " was deleted successfully");
    }
}
