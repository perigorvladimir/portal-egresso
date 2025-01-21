package com.ufma.portalegresso.application.domain;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="cargo")
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCargo;
    @NotNull
    private String descricao;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAreaTrabalho tipoAreaTrabalho;
    @NotNull
    private String local;
    @NotNull
    private Integer anoInicio;
    private Integer anoFim;
    @ManyToOne
    @JoinColumn(name="id_egresso", nullable=false)
    @NotNull
    private Egresso egresso;
}
