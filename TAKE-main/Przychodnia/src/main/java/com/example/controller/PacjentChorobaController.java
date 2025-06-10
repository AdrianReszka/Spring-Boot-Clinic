package com.example.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.example.dto.PacjentChorobaDTO;
import com.example.services.PacjentChorobaService;

@RestController
@RequestMapping("/pacjent_choroba")
public class PacjentChorobaController {

    private final PacjentChorobaService service;

    public PacjentChorobaController(PacjentChorobaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PacjentChorobaDTO>>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PacjentChorobaDTO>> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<PacjentChorobaDTO>> create(@Valid @RequestBody PacjentChorobaDTO dto) {
        EntityModel<PacjentChorobaDTO> model = service.create(dto);
        URI location = URI.create(model.getRequiredLink("self").getHref());
        return ResponseEntity.created(location).body(model);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<PacjentChorobaDTO>> update(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(service.update(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok("PacjentChoroba with id " + id + " was deleted successfully");
    }
}
