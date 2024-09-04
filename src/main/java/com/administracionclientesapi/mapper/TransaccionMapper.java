package com.administracionclientesapi.mapper;

import com.administracionclientesapi.dto.TransaccionDTO;
import com.administracionclientesapi.entity.Transaccion;

public class TransaccionMapper {

    public static TransaccionDTO toDTO(Transaccion transaccion) {
        TransaccionDTO dto = new TransaccionDTO();
        dto.setId(transaccion.getId());
        dto.setTipoTransaccion(transaccion.getTipoTransaccion());
        dto.setMonto(transaccion.getMonto());
        dto.setCuentaOrigenId(transaccion.getCuentaOrigen().getId());
        dto.setCuentaDestinoId(transaccion.getCuentaDestino() != null ? transaccion.getCuentaDestino().getId() : null);
        return dto;
    }

    public static Transaccion toEntity(TransaccionDTO dto) {
        Transaccion transaccion = new Transaccion();
        transaccion.setId(dto.getId());
        transaccion.setTipoTransaccion(dto.getTipoTransaccion());
        transaccion.setMonto(dto.getMonto());
        return transaccion;
    }
}
