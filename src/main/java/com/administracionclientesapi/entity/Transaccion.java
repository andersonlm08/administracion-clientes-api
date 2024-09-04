package com.administracionclientesapi.entity;

import com.administracionclientesapi.states.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "tbl_transaccion")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipoTransaccion;

    private double monto;
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "cuenta_origen_id", nullable = false)
    private Cuenta cuentaOrigen;

    @ManyToOne
    @JoinColumn(name = "cuenta_destino_id", nullable = true)
    private Cuenta cuentaDestino;

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.PERSIST)
    private List<Movimiento> movimientos;
    // Getters y Setters
}

