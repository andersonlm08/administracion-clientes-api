package com.administracionclientesapi.service;

import com.administracionclientesapi.dto.TransaccionDTO;
import com.administracionclientesapi.entity.Cuenta;
import com.administracionclientesapi.entity.Movimiento;
import com.administracionclientesapi.entity.Transaccion;
import com.administracionclientesapi.exeption.RecursoNoEncontradoException;
import com.administracionclientesapi.exeption.TransaccionException;
import com.administracionclientesapi.mapper.TransaccionMapper;
import com.administracionclientesapi.repository.CuentaRepository;
import com.administracionclientesapi.repository.MovimientoRepository;
import com.administracionclientesapi.repository.TransaccionRepository;
import com.administracionclientesapi.states.TipoMovimiento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransaccionService {

    private static final Logger logger = LoggerFactory.getLogger(TransaccionService.class);
    public static final String CONSIGNACION = "CONSIGNACION";
    public static final String RETIRO = "RETIRO";
    public static final String TRANSFERENCIA = "TRANSFERENCIA";
    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public TransaccionService(TransaccionRepository transaccionRepository,
                              CuentaRepository cuentaRepository,
                              MovimientoRepository movimientoRepository) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional(readOnly = false)
    public TransaccionDTO realizarTransaccion(TransaccionDTO transaccionDTO) {

        Transaccion transaccion = new Transaccion();

        switch (transaccionDTO.getTipoTransaccion().name()) {
            case CONSIGNACION:
                transaccion = procesarConsignacion(transaccionDTO);
                break;
            case RETIRO:
                transaccion = procesarRetiro(transaccionDTO);
                break;
            case TRANSFERENCIA:
                transaccion = procesarTransferencia(transaccionDTO);
                break;
        }
        return TransaccionMapper.toDTO(transaccion);
    }

    private Transaccion procesarConsignacion(TransaccionDTO transaccionDTO) {
        Cuenta cuenta = cuentaRepository.findById(transaccionDTO.getCuentaDestinoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada"));
        cuenta.setSaldo(cuenta.getSaldo() + transaccionDTO.getMonto());
        cuentaRepository.save(cuenta);
        logger.info("Consignación de {} realizada en la cuenta con id {}", transaccionDTO.getMonto(), transaccionDTO.getCuentaOrigenId());
        return registrarTransaccion(transaccionDTO, cuenta);
    }

    private Transaccion procesarRetiro(TransaccionDTO transaccionDTO) {
        Cuenta cuenta = cuentaRepository.findById(transaccionDTO.getCuentaOrigenId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada"));

        if ((cuenta.getSaldo() - transaccionDTO.getMonto()) < 0) {
            logger.warn("Intento de retiro fallido por saldo insuficiente en la cuenta con id {}", transaccionDTO.getCuentaOrigenId());
            throw new TransaccionException("Fondos insuficientes para el retiro");
        }
        cuenta.setSaldo(cuenta.getSaldo() - transaccionDTO.getMonto());
        cuentaRepository.save(cuenta);
        logger.info("Retiro de {} realizado en la cuenta con id {}", transaccionDTO.getMonto(), transaccionDTO.getCuentaOrigenId());
        return registrarTransaccion(transaccionDTO, cuenta);

    }

    private Transaccion procesarTransferencia(TransaccionDTO transaccionDTO) {

        Optional<Cuenta> cuentaOrigenOpt = cuentaRepository.findById(transaccionDTO.getCuentaOrigenId());
        if (!cuentaOrigenOpt.isPresent()) {
            logger.error("Cuenta origen no encontrada con ID: {}", transaccionDTO.getCuentaOrigenId());
            throw new RecursoNoEncontradoException("Cuenta origen no encontrada");
        }

        Cuenta cuentaDestino = null;
        if (transaccionDTO.getCuentaDestinoId() != null) {
            cuentaDestino = cuentaRepository.findById(transaccionDTO.getCuentaDestinoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta destino no encontrada"));
        }

        Transaccion transaccion = TransaccionMapper.toEntity(transaccionDTO);
        transaccion.setCuentaOrigen(cuentaOrigenOpt.get());
        transaccion.setCuentaDestino(cuentaDestino);
        transaccion.setFecha(LocalDateTime.now());

        actualizarSaldos(transaccion);
        transaccionRepository.save(transaccion);
        logger.info("Transferencia de {} realizada desde la cuenta {} hacia la cuenta {}", transaccionDTO.getMonto(), transaccionDTO.getCuentaOrigenId(), transaccionDTO.getCuentaDestinoId());
        return transaccion;
    }

    private Transaccion registrarTransaccion(TransaccionDTO transaccionDTO, Cuenta cuentaOrigen) {
        Transaccion transaccion = new Transaccion();
        transaccion.setCuentaOrigen(cuentaOrigen);
        transaccion.setMonto(transaccionDTO.getMonto());
        transaccion.setTipoTransaccion(transaccionDTO.getTipoTransaccion());
        transaccion.setFecha(LocalDateTime.now());

        return transaccionRepository.save(transaccion);
    }

    private void actualizarSaldos(Transaccion transaccion) {
        Cuenta cuentaOrigen = transaccion.getCuentaOrigen();
        Cuenta cuentaDestino = transaccion.getCuentaDestino();

        Movimiento movimientoDebito = new Movimiento();
        movimientoDebito.setTipoMovimiento(TipoMovimiento.DEBITO);
        movimientoDebito.setMonto(transaccion.getMonto());
        movimientoDebito.setFecha(LocalDateTime.now());
        movimientoDebito.setCuenta(cuentaOrigen);
        movimientoDebito.setTransaccion(transaccion);
        movimientoRepository.save(movimientoDebito);

        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() - transaccion.getMonto());
        cuentaRepository.save(cuentaOrigen);

        Movimiento movimientoCredito = new Movimiento();
        movimientoCredito.setTipoMovimiento(TipoMovimiento.CREDITO);
        movimientoCredito.setMonto(transaccion.getMonto());
        movimientoCredito.setFecha(LocalDateTime.now());
        movimientoCredito.setCuenta(cuentaDestino);
        movimientoCredito.setTransaccion(transaccion);
        movimientoRepository.save(movimientoCredito);

        cuentaDestino.setSaldo(cuentaDestino.getSaldo() + transaccion.getMonto());
        cuentaRepository.save(cuentaDestino);

    }
}
