package com.administracionclientesapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.administracionclientesapi.dto.ClienteDTO;
import com.administracionclientesapi.entity.Cliente;
import com.administracionclientesapi.exeption.ClienteRelacionadoException;
import com.administracionclientesapi.exeption.RecursoNoEncontradoException;
import com.administracionclientesapi.repository.ClienteRepository;
import com.administracionclientesapi.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private ClienteService clienteService;

    private final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for registrarCliente method
    @Test
    public void testRegistrarCliente_successful() {
        // Arrange
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombres("John");
        clienteDTO.setApellido("Doe");
        clienteDTO.setFechaNacimiento(LocalDate.of(2000, 1, 8));
        clienteDTO.setCorreoElectronico("john.doe@example.com");

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombres("John");
        cliente.setApellido("Doe");
        cliente.setCorreoElectronico("john.doe@example.com");

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        ClienteDTO result = clienteService.registrarCliente(clienteDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getNombres()).isEqualTo("John");
        assertThat(result.getApellido()).isEqualTo("Doe");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    public void testActualizarCliente_successful() {
        // Arrange
        Long clienteId = 1L;
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombres("John");
        clienteDTO.setApellido("Doe");
        clienteDTO.setCorreoElectronico("john.doe@example.com");

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNombres("Jane");
        cliente.setApellido("Smith");
        cliente.setCorreoElectronico("jane.smith@example.com");

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        ClienteDTO result = clienteService.actualizarCliente(clienteId, clienteDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getNombres()).isEqualTo("John");
        assertThat(result.getApellido()).isEqualTo("Doe");
        assertThat(result.getCorreoElectronico()).isEqualTo("john.doe@example.com");
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    public void testActualizarCliente_clienteNotFound() {
        // Arrange
        Long clienteId = 1L;
        ClienteDTO clienteDTO = new ClienteDTO();

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> {
            clienteService.actualizarCliente(clienteId, clienteDTO);
        });

        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    public void testEliminarCliente_successful() {
        // Arrange
        Long clienteId = 1L;

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(cuentaRepository.existsByClienteId(clienteId)).thenReturn(false);

        // Act
        clienteService.eliminarCliente(clienteId);

        // Assert
        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    public void testEliminarCliente_conCuentasAsociadas() {
        // Arrange
        Long clienteId = 1L;

        when(cuentaRepository.existsByClienteId(clienteId)).thenReturn(true);

        // Act & Assert
        assertThrows(ClienteRelacionadoException.class, () -> {
            clienteService.eliminarCliente(clienteId);
        });

        verify(clienteRepository, never()).delete(any(Cliente.class));
    }

    @Test
    public void testEliminarCliente_clienteNotFound() {
        // Arrange
        Long clienteId = 1L;

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> {
            clienteService.eliminarCliente(clienteId);
        });

        verify(clienteRepository, never()).delete(any(Cliente.class));
    }

    @Test
    public void testConsultarCliente_successful() {
        // Arrange
        Long clienteId = 1L;

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNombres("John");
        cliente.setApellido("Doe");

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));

        // Act
        ClienteDTO result = clienteService.consultarCliente(clienteId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getNombres()).isEqualTo("John");
        assertThat(result.getApellido()).isEqualTo("Doe");
    }

    @Test
    public void testConsultarCliente_clienteNotFound() {
        // Arrange
        Long clienteId = 1L;

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> {
            clienteService.consultarCliente(clienteId);
        });
    }
}
