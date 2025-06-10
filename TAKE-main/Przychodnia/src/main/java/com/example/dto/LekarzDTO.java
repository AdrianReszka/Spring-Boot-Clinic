package com.example.dto;

import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LekarzDTO extends RepresentationModel<LekarzDTO>{
	
    private Long id;
    
    @NotNull(message = "Lekarz name can not be empty!")
    private String imie;
    
    @NotNull(message = "Lekarz surname can not be empty!")
    private String nazwisko;
    
    @NotNull(message = "Lekarz specialization can not be empty!")
    private String specjalizacja;
    
    @NotNull(message = "Lekarz email address can not be empty!")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email must contains only letters!")
    private String email;
}
