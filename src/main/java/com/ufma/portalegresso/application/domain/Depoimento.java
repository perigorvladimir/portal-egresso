package com.ufma.portalegresso.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonIgnoreProperties({"depoimentos"})
    private Egresso egresso;
}
