package com.example.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChorobaDTO extends RepresentationModel<ChorobaDTO>{
	
    private Long id;
    
	@NotNull(message = "Chroba name can not be empty!")
    private String nazwa;
    
	@NotNull(message = "Chroba desctription can not be empty!")
    private String opis;
	
}

