package com.administracionclientesapi.application.mapper;

import com.administracionclientesapi.application.dto.CuentaDTO;
import com.administracionclientesapi.domain.entity.Cuenta;

public class CuentaMapper {

    public static CuentaDTO toDTO(Cuenta cuenta) {
        CuentaDTO dto = new CuentaDTO();
        dto.setId(cuenta.getId());
        dto.setTipoCuenta(cuenta.getTipoCuenta());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setEstadoCuenta(cuenta.getEstadoCuenta());
        dto.setSaldo(cuenta.getSaldo());
        dto.setExentaGMF(cuenta.isExentaGMF());
        dto.setFechaCreacion(cuenta.getFechaCreacion());
        dto.setFechaModificacion(cuenta.getFechaModificacion());
        dto.setClienteId(cuenta.getCliente().getId());
        return dto;
    }

    public static Cuenta toEntity(CuentaDTO dto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(dto.getId());
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        cuenta.setEstadoCuenta(dto.getEstadoCuenta());
        cuenta.setSaldo(dto.getSaldo());
        cuenta.setExentaGMF(dto.isExentaGMF());
        cuenta.setFechaCreacion(dto.getFechaCreacion());
        cuenta.setFechaModificacion(dto.getFechaModificacion());
        return cuenta;
    }
}
