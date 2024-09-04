package com.administracionclientesapi.controller;

import com.administracionclientesapi.adapter.controller.TransaccionController;
import com.administracionclientesapi.application.dto.TransaccionDTO;
import com.administracionclientesapi.application.service.TransaccionService;
import com.administracionclientesapi.domain.states.TipoTransaccion;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
public class TransaccionControllerTest {

    @Mock
    private TransaccionService transaccionService;

    @InjectMocks
    private TransaccionController transaccionController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transaccionController).build();
    }

    @Test
    public void testRealizarTransaccion() throws Exception {
        // Crea un objeto TransaccionDTO de prueba
        TransaccionDTO transaccionDTO = new TransaccionDTO();
        transaccionDTO.setTipoTransaccion(TipoTransaccion.CONSIGNACION);
        transaccionDTO.setMonto(1000.0);
        transaccionDTO.setCuentaDestinoId(1L);

        // Simula el comportamiento del servicio
        when(transaccionService.realizarTransaccion(transaccionDTO)).thenReturn(transaccionDTO);

        // Convierte el objeto TransaccionDTO a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String transaccionDTOJson = objectMapper.writeValueAsString(transaccionDTO);

        // Realiza la llamada al endpoint y verifica el resultado
        mockMvc.perform(post("/transacciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transaccionDTOJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.monto", is(transaccionDTO.getMonto())))
                .andExpect(jsonPath("$.tipoTransaccion", is(transaccionDTO.getTipoTransaccion().name())));
    }
}
