package com.administracionclientesapi.service;

import com.administracionclientesapi.dto.TransaccionDTO;
import com.administracionclientesapi.entity.Cuenta;
import com.administracionclientesapi.entity.Transaccion;
import com.administracionclientesapi.exeption.RecursoNoEncontradoException;
import com.administracionclientesapi.exeption.TransaccionException;
import com.administracionclientesapi.repository.CuentaRepository;
import com.administracionclientesapi.repository.MovimientoRepository;
import com.administracionclientesapi.repository.TransaccionRepository;
import com.administracionclientesapi.states.TipoTransaccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TransaccionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @InjectMocks
    private TransaccionService transaccionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRealizarConsignacion() {
        // Datos de prueba
        TransaccionDTO transaccionDTO = new TransaccionDTO();
        transaccionDTO.setTipoTransaccion(TipoTransaccion.CONSIGNACION);
        transaccionDTO.setCuentaDestinoId(1L);
        transaccionDTO.setMonto(500.0);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setId(1L);
        cuentaDestino.setSaldo(1000.0);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaDestino));
        when(transaccionRepository.save(any(Transaccion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Llamada al método
        TransaccionDTO resultado = transaccionService.realizarTransaccion(transaccionDTO);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(1500.0, cuentaDestino.getSaldo());
        verify(cuentaRepository, times(1)).save(cuentaDestino);
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }

    @Test
    void testRealizarRetiroFondosInsuficientes() {
        // Datos de prueba
        TransaccionDTO transaccionDTO = new TransaccionDTO();
        transaccionDTO.setTipoTransaccion(TipoTransaccion.RETIRO);
        transaccionDTO.setCuentaOrigenId(1L);
        transaccionDTO.setMonto(1500.0);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setId(1L);
        cuentaOrigen.setSaldo(1000.0);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaOrigen));

        // Llamada al método y verificación de excepción
        assertThrows(TransaccionException.class, () -> transaccionService.realizarTransaccion(transaccionDTO));
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void testRealizarTransferenciaExitosa() {
        // Datos de prueba
        TransaccionDTO transaccionDTO = new TransaccionDTO();
        transaccionDTO.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA);
        transaccionDTO.setCuentaOrigenId(1L);
        transaccionDTO.setCuentaDestinoId(2L);
        transaccionDTO.setMonto(500.0);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setId(1L);
        cuentaOrigen.setSaldo(1000.0);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setId(2L);
        cuentaDestino.setSaldo(500.0);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaOrigen));
        when(cuentaRepository.findById(2L)).thenReturn(Optional.of(cuentaDestino));
        when(transaccionRepository.save(any(Transaccion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Llamada al método
        TransaccionDTO resultado = transaccionService.realizarTransaccion(transaccionDTO);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(500.0, cuentaOrigen.getSaldo());
        assertEquals(1000.0, cuentaDestino.getSaldo());
        verify(cuentaRepository, times(1)).save(cuentaOrigen);
        verify(cuentaRepository, times(1)).save(cuentaDestino);
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }

    @Test
    void testRealizarTransferenciaCuentaOrigenNoEncontrada() {
        // Datos de prueba
        TransaccionDTO transaccionDTO = new TransaccionDTO();
        transaccionDTO.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA);
        transaccionDTO.setCuentaOrigenId(1L);
        transaccionDTO.setCuentaDestinoId(2L);
        transaccionDTO.setMonto(500.0);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.empty());

        // Llamada al método y verificación de excepción
        assertThrows(RecursoNoEncontradoException.class, () -> transaccionService.realizarTransaccion(transaccionDTO));
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void testRealizarTransferenciaCuentaDestinoNoEncontrada() {
        // Datos de prueba
        TransaccionDTO transaccionDTO = new TransaccionDTO();
        transaccionDTO.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA);
        transaccionDTO.setCuentaOrigenId(1L);
        transaccionDTO.setCuentaDestinoId(2L);
        transaccionDTO.setMonto(500.0);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setId(1L);
        cuentaOrigen.setSaldo(1000.0);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaOrigen));
        when(cuentaRepository.findById(2L)).thenReturn(Optional.empty());

        // Llamada al método y verificación de excepción
        assertThrows(RecursoNoEncontradoException.class, () -> transaccionService.realizarTransaccion(transaccionDTO));
        verify(cuentaRepository, never()).save(cuentaOrigen);
    }

}

