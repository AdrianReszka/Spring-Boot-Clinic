package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "PACJENT_OBJAW")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class PacjentObjaw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACJENT_OBJAW")
    private Long idPacjentObjaw;

    @ManyToOne
    @JoinColumn(name = "ID_PACJENTA")
    private Pacjent pacjent;

    @ManyToOne
    @JoinColumn(name = "ID_OBJAWU")
    private Objaw objaw;

    @Column(name = "DATA_WYSTAPIENIA")
    private LocalDate dataWystapienia;

}
