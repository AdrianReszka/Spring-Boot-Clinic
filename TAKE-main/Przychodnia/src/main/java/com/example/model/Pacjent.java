package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PACJENCI")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Pacjent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACJENTA")
    private Long idPacjenta;

    @Column(name = "IMIE", nullable = false)
    private String imie;

    @Column(name = "NAZWISKO", nullable = false)
    private String nazwisko;

    @Column(name = "PESEL", unique = true)
    private String pesel;

    @Column(name = "DATA_URODZENIA")
    private LocalDate dataUrodzenia;

    @Column(name = "ADRES")
    private String adres;

    @Column(name = "NUMER_TELEFONU")
    private String numerTelefonu;

    @Column(name = "E_MAIL")
    private String email;

    @OneToMany(mappedBy = "pacjent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wizyta> wizyty = new ArrayList<>();

    @OneToMany(mappedBy = "pacjent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PacjentObjaw> pacjentObjawy = new ArrayList<>();

}
