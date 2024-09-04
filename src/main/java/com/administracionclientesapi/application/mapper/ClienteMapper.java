package com.administracionclientesapi.application.mapper;


import com.administracionclientesapi.application.dto.ClienteDTO;
import com.administracionclientesapi.domain.entity.Cliente;

public class ClienteMapper {

    public static ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setTipoIdentificacion(cliente.getTipoIdentificacion());
        dto.setNumeroIdentificacion(cliente.getNumeroIdentificacion());
        dto.setNombres(cliente.getNombres());
        dto.setApellido(cliente.getApellido());
        dto.setCorreoElectronico(cliente.getCorreoElectronico());
        dto.setFechaNacimiento(cliente.getFechaNacimiento());
        dto.setFechaCreacion(cliente.getFechaCreacion());
        dto.setFechaModificacion(cliente.getFechaModificacion());
        return dto;
    }

    public static Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setTipoIdentificacion(dto.getTipoIdentificacion());
        cliente.setNumeroIdentificacion(dto.getNumeroIdentificacion());
        cliente.setNombres(dto.getNombres());
        cliente.setApellido(dto.getApellido());
        cliente.setCorreoElectronico(dto.getCorreoElectronico());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        cliente.setFechaCreacion(dto.getFechaCreacion());
        cliente.setFechaModificacion(dto.getFechaModificacion());
        return cliente;
    }
}
