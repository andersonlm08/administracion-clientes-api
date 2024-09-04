package com.administracionclientesapi.service;

import com.administracionclientesapi.dto.CuentaDTO;
import com.administracionclientesapi.entity.Cliente;
import com.administracionclientesapi.entity.Cuenta;
import com.administracionclientesapi.exeption.RecursoNoEncontradoException;
import com.administracionclientesapi.mapper.CuentaMapper;
import com.administracionclientesapi.repository.ClienteRepository;
import com.administracionclientesapi.repository.CuentaRepository;
import com.administracionclientesapi.states.EstadoCuenta;
import com.administracionclientesapi.states.TipoCuenta;
import com.administracionclientesapi.util.ValidadorUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    private final ClienteRepository clienteRepository;

    public CuentaService(CuentaRepository cuentaRepository, ClienteRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    public CuentaDTO crearCuenta(CuentaDTO cuentaDTO) {

        ValidadorUtil.validarSaldoInicial(cuentaDTO.getSaldo());

        Cliente cliente = clienteRepository.findById(cuentaDTO.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));

        Cuenta cuenta = CuentaMapper.toEntity(cuentaDTO);
        cuenta.setNumeroCuenta(generarNumeroCuenta(cuentaDTO.getTipoCuenta()));
        cuenta.setFechaCreacion(LocalDateTime.now());
        cuenta.setFechaModificacion(LocalDateTime.now());
        if (cuenta.getTipoCuenta() == TipoCuenta.AHORROS) {
            cuenta.setEstadoCuenta(EstadoCuenta.ACTIVA);
        }
        cuenta.setCliente(cliente);
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        return CuentaMapper.toDTO(cuentaGuardada);
    }

    public void actualizarEstadoCuenta(Long id, EstadoCuenta estadoCuenta) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada"));

        ValidadorUtil.validarSaldoParaCancelarCuenta(cuenta);
        cuenta.setEstadoCuenta(estadoCuenta);
        cuenta.setFechaModificacion(LocalDateTime.now());
        cuentaRepository.save(cuenta);
    }

    public CuentaDTO obtenerEstadoCuenta(Long id) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(id);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            return CuentaMapper.toDTO(cuenta);
        }
        return null;
    }
    private String generarNumeroCuenta(TipoCuenta tipoCuenta) {
        String prefix = tipoCuenta == TipoCuenta.AHORROS ? "53" : "33";
        return prefix + new Random().nextInt(900000000) + 100000000;
    }
}
