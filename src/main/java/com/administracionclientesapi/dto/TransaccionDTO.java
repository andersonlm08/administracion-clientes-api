package com.administracionclientesapi.dto;

import com.administracionclientesapi.states.TipoTransaccion;
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
