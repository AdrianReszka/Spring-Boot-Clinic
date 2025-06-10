package com.example.dto;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacjentDTO extends RepresentationModel<PacjentDTO> {
	
    private Long id;
    
    @NotNull(message = "Pacjent name can not be empty!")
    private String imie;
    
    @NotNull(message = "Pacjent surname can not be empty!")
    private String nazwisko;
    
    @NotNull(message = "Pacjent pesel can not be empty!")
    @Pattern(regexp = "^\\d{11}$", message = "PESEL must contains exactly eleven numbers!")
    private String pesel;
    
    @NotNull(message = "Pacjent date of birth can not be empty!")
    private LocalDate dataUrodzenia;
    
    @NotNull(message = "Pacjent address can not be empty!")
    private String adres;
    
    @NotNull(message = "Pacjent telephone number can not be empty!")
    @Pattern(regexp = "^\\d{9}$", message = "Telephone number must contains exactly nine numbers!")
    private String numerTelefonu;
    
    @NotNull(message = "Pacjent email address can not be empty!")
    @Pattern(regexp = "^^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email must contains only letters!")
    private String email;
}
