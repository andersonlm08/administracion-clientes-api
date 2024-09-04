package com.administracionclientesapi.controller;

import com.administracionclientesapi.dto.ClienteDTO;
import com.administracionclientesapi.exeption.GlobalExceptionHandler;
import com.administracionclientesapi.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testRegistrarCliente_successful() throws Exception {
        // Arrange
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombres("John");
        clienteDTO.setApellido("Doe");

        when(clienteService.registrarCliente(any(ClienteDTO.class))).thenReturn(clienteDTO);

        // Act & Assert
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombres\": \"John\", \"apellido\": \"Doe\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombres").value("John"))
                .andExpect(jsonPath("$.apellido").value("Doe"));

        verify(clienteService, times(1)).registrarCliente(any(ClienteDTO.class));
    }

    @Test
    public void testActualizarCliente_successful() throws Exception {
        // Arrange
        Long clienteId = 1L;
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombres("John");
        clienteDTO.setApellido("Doe");

        when(clienteService.actualizarCliente(eq(clienteId), any(ClienteDTO.class))).thenReturn(clienteDTO);

        // Act & Assert
        mockMvc.perform(put("/clientes/{id}", clienteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombres\": \"John\", \"apellido\": \"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombres").value("John"))
                .andExpect(jsonPath("$.apellido").value("Doe"));

        verify(clienteService, times(1)).actualizarCliente(eq(clienteId), any(ClienteDTO.class));
    }

    @Test
    public void testEliminarCliente_successful() throws Exception {
        // Arrange
        Long clienteId = 1L;

        doNothing().when(clienteService).eliminarCliente(clienteId);

        // Act & Assert
        mockMvc.perform(delete("/clientes/{id}", clienteId))
                .andExpect(status().isNoContent());

        verify(clienteService, times(1)).eliminarCliente(clienteId);
    }

    @Test
    public void testConsultarCliente_successful() throws Exception {
        // Arrange
        Long clienteId = 1L;
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombres("John");
        clienteDTO.setApellido("Doe");

        when(clienteService.consultarCliente(clienteId)).thenReturn(clienteDTO);

        // Act & Assert
        mockMvc.perform(get("/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombres").value("John"))
                .andExpect(jsonPath("$.apellido").value("Doe"));

        verify(clienteService, times(1)).consultarCliente(clienteId);
    }
}
