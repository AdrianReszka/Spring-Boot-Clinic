package com.example.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import com.example.dto.ChorobaDTO;
import com.example.dto.ObjawDTO;
import com.example.services.ChorobaService;

@RestController
@RequestMapping("/choroby")
public class ChorobaController {

    private final ChorobaService chorobaService;

    public ChorobaController(ChorobaService chorobaService) {
        this.chorobaService = chorobaService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ChorobaDTO>>> getAllChoroby() {
        return ResponseEntity.ok(chorobaService.getAllChoroby());
    }
    
    @GetMapping("/{id}/objawy")
    public ResponseEntity<List<ObjawDTO>> getObjawyByChorobaId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(chorobaService.getObjawyByChorobaId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ChorobaDTO>> getChorobaById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(chorobaService.getChorobaById(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<ChorobaDTO>> createChoroba(@Valid @RequestBody ChorobaDTO dto) {
        EntityModel<ChorobaDTO> model = chorobaService.createChoroba(dto);
        URI location = URI.create(model.getRequiredLink("self").getHref());
        return ResponseEntity.created(location).body(model);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<ChorobaDTO>> updateChoroba(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(chorobaService.updateChoroba(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChoroba(@PathVariable("id") Long id) {
        chorobaService.deleteChoroba(id);
        return ResponseEntity.ok("Choroba with id " + id + " was deleted successfully");
    }
}
