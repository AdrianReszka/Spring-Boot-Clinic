package com.example.dto;


import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjawDTO extends RepresentationModel<ObjawDTO>{
	
    private Long id;
    
    @NotNull(message = "Objaw name can not be empty!")
    private String nazwa;
    
    @NotNull(message = "Objaw descritpion can not be empty!")
    private String opis;
}
