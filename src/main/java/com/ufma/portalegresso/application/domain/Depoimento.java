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
@Table(schema = "portalegresso", name="depoimento")
public class Depoimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDepoimento;
    @Column(nullable = false)
    private String texto;
    @Column(nullable = false)
    private LocalDate data;
    @ManyToOne
    @JoinColumn(name="id_egresso", nullable=false)
    @JsonIgnoreProperties({"depoimentos"})
    private Egresso egresso;
}
