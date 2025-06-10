package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "WIZYTY")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Wizyta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_WIZYTY")
    private Long idWizyty;

    @ManyToOne
    @JoinColumn(name = "ID_LEKARZA")
    private Lekarz lekarz;

    @ManyToOne
    @JoinColumn(name = "ID_PACJENTA")
    private Pacjent pacjent;

    @Column(name = "TERMIN")
    private LocalDateTime termin;

    @OneToMany(mappedBy = "wizyta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PacjentChoroba> pacjentChoroby = new ArrayList<>();
}
