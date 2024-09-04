package com.administracionclientesapi.application.dto;

import com.administracionclientesapi.domain.states.TipoTransaccion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransaccionDTO {
    private Long id;
    private TipoTransaccion tipoTransaccion;
    private double monto;
    private Long cuentaOrigenId;
    private Long cuentaDestinoId;
}
