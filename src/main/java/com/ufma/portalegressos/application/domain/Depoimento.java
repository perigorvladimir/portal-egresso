package com.ufma.portalegressos.application.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="depoimento")
public class Depoimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDepoimento;
    private String texto;
    private LocalDate data;
    @ManyToOne
    @JoinColumn(name="id_egresso", nullable=false)
    @NotNull
    private Egresso egresso;
}
