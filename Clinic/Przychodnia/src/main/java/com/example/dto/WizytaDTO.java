package com.example.dto;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WizytaDTO extends RepresentationModel<WizytaDTO>{
	
    private Long id;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull(message = "Pacjent id can not be empty!")
    private Long idPacjenta;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@NotNull(message = "Lekarz id can not be empty!")
    private Long idLekarza;

	@NotNull(message = "Wizyta date can not be empty!")
    private LocalDateTime termin;
}
