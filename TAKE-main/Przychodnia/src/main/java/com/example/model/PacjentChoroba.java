package com.example.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PACJENT_CHOROBA")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class PacjentChoroba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACJENT_CHOROBA")
    private Long idPacjentChoroba;

    @ManyToOne
    @JoinColumn(name = "ID_WIZYTY")
    private Wizyta wizyta;

    @ManyToOne
    @JoinColumn(name = "ID_CHOROBY")
    private Choroba choroba;
    
}
