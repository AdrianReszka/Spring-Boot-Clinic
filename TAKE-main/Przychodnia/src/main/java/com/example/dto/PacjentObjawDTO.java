package com.example.dto;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacjentObjawDTO extends RepresentationModel<PacjentObjawDTO>{

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull(message = "Pacjent id can not be empty!")
    private Long idPacjenta;
    
    @NotNull(message = "Objaw id can not be empty!")
    private Long idObjawu;

    @NotNull(message = "Objaw name can not be empty!")
    private String nazwaObjawu;

    @NotNull(message = "Objaw date of occurrence can not be empty!")
    private LocalDate dataWystapienia;
}
