package com.administracionclientesapi.service;


import com.administracionclientesapi.dto.ClienteDTO;
import com.administracionclientesapi.entity.Cliente;
import com.administracionclientesapi.exeption.ClienteRelacionadoException;
import com.administracionclientesapi.exeption.RecursoNoEncontradoException;
import com.administracionclientesapi.mapper.ClienteMapper;
import com.administracionclientesapi.repository.ClienteRepository;
import com.administracionclientesapi.repository.CuentaRepository;
import com.administracionclientesapi.util.ValidadorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);
    private final ClienteRepository clienteRepository;

    private final CuentaRepository cuentaRepository;

    public ClienteService(ClienteRepository clienteRepository, CuentaRepository cuentaRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
    }

    public ClienteDTO registrarCliente(ClienteDTO clienteDTO) {

        ValidadorUtil.validarNombreYApellido(clienteDTO.getNombres(), clienteDTO.getApellido());
        ValidadorUtil.validarEdad(clienteDTO.getFechaNacimiento());
        ValidadorUtil.validarCorreoElectronico(clienteDTO.getCorreoElectronico());

        Cliente cliente = ClienteMapper.toEntity(clienteDTO);
        cliente.setFechaCreacion(LocalDateTime.now());
        cliente.setFechaModificacion(LocalDateTime.now());
        Cliente clienteGuardado = clienteRepository.save(cliente);
        logger.info("Registro de cliente con id de {}", clienteGuardado.getId());
        return ClienteMapper.toDTO(clienteGuardado);
    }

    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));

        cliente.setTipoIdentificacion(clienteDTO.getTipoIdentificacion());
        cliente.setTipoIdentificacion(clienteDTO.getTipoIdentificacion());
        cliente.setNombres(clienteDTO.getNombres());
        cliente.setApellido(clienteDTO.getApellido());
        cliente.setCorreoElectronico(clienteDTO.getCorreoElectronico());
        cliente.setFechaModificacion(LocalDateTime.now());
        cliente.setFechaNacimiento(clienteDTO.getFechaNacimiento());

        Cliente clienteActualizado = clienteRepository.save(cliente);
        logger.info("Actualización de cliente con id de {}", id);
        return ClienteMapper.toDTO(clienteActualizado);
    }

    public void eliminarCliente(Long clienteId) {

        boolean tieneCuentas = cuentaRepository.existsByClienteId(clienteId);
        if (tieneCuentas) {
            logger.warn("Intento de eliminar cliente con id {} que tiene cuentas asociadas.", clienteId);
            throw new ClienteRelacionadoException("No se puede eliminar el cliente porque tiene cuentas asociadas.");
        }
        logger.info("Eliminando cliente con id {}", clienteId);
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        logger.info("Cliente con id {} eliminado exitosamente.", clienteId);
        clienteRepository.delete(cliente);
    }

    public ClienteDTO consultarCliente(Long id){
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        return ClienteMapper.toDTO(cliente);
    }
}
