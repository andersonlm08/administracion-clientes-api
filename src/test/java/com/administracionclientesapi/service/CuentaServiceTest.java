package com.administracionclientesapi.service;

import com.administracionclientesapi.dto.CuentaDTO;
import com.administracionclientesapi.entity.Cliente;
import com.administracionclientesapi.entity.Cuenta;
import com.administracionclientesapi.exeption.RecursoNoEncontradoException;
import com.administracionclientesapi.repository.ClienteRepository;
import com.administracionclientesapi.repository.CuentaRepository;
import com.administracionclientesapi.states.EstadoCuenta;
import com.administracionclientesapi.states.TipoCuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class CuentaServiceTest {
    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearCuenta_successful() {
        // Arrange
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setSaldo(5000.0);
        cuentaDTO.setClienteId(1L);
        cuentaDTO.setTipoCuenta(TipoCuenta.AHORROS);

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("1234567890");
        cuenta.setTipoCuenta(TipoCuenta.AHORROS);
        cuenta.setSaldo(5000.0);
        cuenta.setFechaCreacion(LocalDateTime.now());
        cuenta.setFechaModificacion(LocalDateTime.now());
        cuenta.setEstadoCuenta(EstadoCuenta.ACTIVA);
        cuenta.setCliente(cliente);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        // Act
        CuentaDTO result = cuentaService.crearCuenta(cuentaDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getNumeroCuenta()).isEqualTo("1234567890");
        assertThat(result.getTipoCuenta()).isEqualTo(TipoCuenta.AHORROS);
        assertThat(result.getSaldo()).isEqualTo(5000.0);

        verify(clienteRepository, times(1)).findById(1L);
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @Test
    public void testCrearCuenta_clienteNotFound() {
        // Arrange
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setSaldo(5000.0);
        cuentaDTO.setClienteId(1L);
        cuentaDTO.setTipoCuenta(TipoCuenta.AHORROS);

        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> {
            cuentaService.crearCuenta(cuentaDTO);
        });

        verify(clienteRepository, times(1)).findById(1L);
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    public void testActualizarEstadoCuenta_successful() {
        // Arrange
        Long cuentaId = 1L;
        EstadoCuenta nuevoEstado = EstadoCuenta.INACTIVA;

        Cuenta cuenta = new Cuenta();
        cuenta.setId(cuentaId);
        cuenta.setEstadoCuenta(EstadoCuenta.ACTIVA);

        when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.of(cuenta));

        // Act
        cuentaService.actualizarEstadoCuenta(cuentaId, nuevoEstado);

        // Assert
        assertThat(cuenta.getEstadoCuenta()).isEqualTo(nuevoEstado);
        verify(cuentaRepository, times(1)).save(cuenta);
    }

    @Test
    public void testActualizarEstadoCuenta_cuentaNotFound() {
        // Arrange
        Long cuentaId = 1L;
        EstadoCuenta nuevoEstado = EstadoCuenta.INACTIVA;

        when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> {
            cuentaService.actualizarEstadoCuenta(cuentaId, nuevoEstado);
        });

        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    public void testObtenerEstadoCuenta_cuentaNotFound() {
        // Arrange
        Long cuentaId = 1L;

        when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.empty());

        // Act
        CuentaDTO result = cuentaService.obtenerEstadoCuenta(cuentaId);

        // Assert
        assertThat(result).isNull();
    }

}
