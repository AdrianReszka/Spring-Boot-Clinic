package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Table(name = "CHOROBY")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Choroba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CHOROBY")
    private Long idChoroby;

    @Column(name = "NAZWA")
    private String nazwa;

    @Column(name = "OPIS")
    private String opis;

    @OneToMany(mappedBy = "choroba", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PacjentChoroba> pacjentChoroby = new ArrayList<>();


}
