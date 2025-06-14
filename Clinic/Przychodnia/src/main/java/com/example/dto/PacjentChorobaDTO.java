package com.example.dto;

import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacjentChorobaDTO extends RepresentationModel<PacjentChorobaDTO>{
	
    private Long id;
    
    @NotNull(message = "Wizyta id can not be empty!")
    private Long idWizyty;
    
    @NotNull(message = "Choroba id can not be empty!")
    private Long idChoroby;
}
