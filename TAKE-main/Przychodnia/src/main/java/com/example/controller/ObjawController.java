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
import com.example.services.ObjawService;

@RestController
@RequestMapping("/objawy")
public class ObjawController {

    private final ObjawService objawService;
    
    private final ChorobaService chorobaService;

    public ObjawController(ObjawService objawService, ChorobaService chorobaService) {
        this.objawService = objawService;
        this.chorobaService = chorobaService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ObjawDTO>>> getAllObjawy() {
        return ResponseEntity.ok(objawService.getAllObjawy());
    }
    
    @GetMapping("/{id}/choroby")
    public ResponseEntity<List<ChorobaDTO>> getChorobyByObjawId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(chorobaService.getChorobyByObjawId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ObjawDTO>> getObjawById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(objawService.getObjawById(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<ObjawDTO>> createObjaw(@Valid @RequestBody ObjawDTO dto) {
        EntityModel<ObjawDTO> model = objawService.createObjaw(dto);
        URI location = URI.create(model.getRequiredLink("self").getHref());
        return ResponseEntity.created(location).body(model);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<ObjawDTO>> updateObjaw(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(objawService.updateObjaw(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteObjaw(@PathVariable("id") Long id) {
        objawService.deleteObjaw(id);
        return ResponseEntity.ok("Objaw with id " + id + " was deleted successfully");
    }
}
