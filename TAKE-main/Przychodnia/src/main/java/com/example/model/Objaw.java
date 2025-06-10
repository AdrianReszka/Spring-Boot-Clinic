package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OBJAWY")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Objaw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_OBJAWU")
    private Long idObjawu;

    @Column(name = "NAZWA")
    private String nazwa;

    @Column(name = "OPIS")
    private String opis;

    @OneToMany(mappedBy = "objaw", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PacjentObjaw> pacjentObjawy = new ArrayList<>();

}
