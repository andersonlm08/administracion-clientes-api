package com.administracionclientesapi.dto;

import com.administracionclientesapi.states.EstadoCuenta;
import com.administracionclientesapi.states.TipoCuenta;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CuentaDTO {
    private Long id;
    private TipoCuenta tipoCuenta;
    private String numeroCuenta;
    private EstadoCuenta estadoCuenta;
    private double saldo;
    private boolean exentaGMF;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private Long clienteId;
}
