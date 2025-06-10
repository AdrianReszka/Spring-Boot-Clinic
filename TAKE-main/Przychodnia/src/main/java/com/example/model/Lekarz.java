package com.example.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "LEKARZE")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Lekarz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LEKARZA")
    private Long idLekarza;

    @Column(name = "IMIE", nullable = true)
    private String imie;

    @Column(name = "NAZWISKO", nullable = false)
    private String nazwisko;

    @Column(name = "SPECJALIZACJA")
    private String specjalizacja;

    @Column(name = "E_MAIL")
    private String email;
    
    @OneToMany(mappedBy = "lekarz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wizyta> wizyty = new ArrayList<>();

}