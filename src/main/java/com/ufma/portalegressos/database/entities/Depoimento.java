package com.ufma.portalegressos.database.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="depoimento")
public class Depoimento {
    @Id
    private Integer idDepoimento;
    @ManyToOne
    @JoinColumn(name="id_egresso", nullable=false)
    private Egresso egresso;
}
