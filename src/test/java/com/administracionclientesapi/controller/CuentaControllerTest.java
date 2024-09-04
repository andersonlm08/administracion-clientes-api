package com.administracionclientesapi.controller;

import com.administracionclientesapi.adapter.controller.CuentaController;
import com.administracionclientesapi.application.dto.CuentaDTO;
import com.administracionclientesapi.application.dto.EstadoCuentaDTO;
import com.administracionclientesapi.application.service.CuentaService;
import com.administracionclientesapi.domain.states.EstadoCuenta;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CuentaControllerTest {

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private CuentaController cuentaController;

    public CuentaControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearCuenta() {
        // Datos de prueba
        CuentaDTO cuentaDTO = new CuentaDTO();
        CuentaDTO cuentaCreada = new CuentaDTO();

        // Simulamos la respuesta del servicio
        when(cuentaService.crearCuenta(cuentaDTO)).thenReturn(cuentaCreada);

        // Llamada al método del controlador
        ResponseEntity<CuentaDTO> response = cuentaController.crearCuenta(cuentaDTO);

        // Verificaciones
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cuentaCreada, response.getBody());
        verify(cuentaService, times(1)).crearCuenta(cuentaDTO);
    }

    @Test
    void testActualizarEstadoCuenta() {
        // Datos de prueba
        Long id = 1L;
        EstadoCuentaDTO estadoCuentaDTO = new EstadoCuentaDTO();
        estadoCuentaDTO.setEstadoCuenta(EstadoCuenta.ACTIVA);

        // Llamada al método del controlador
        ResponseEntity<Void> response = cuentaController.actualizarEstadoCuenta(id, estadoCuentaDTO);

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cuentaService, times(1)).actualizarEstadoCuenta(id, estadoCuentaDTO.getEstadoCuenta());
    }

    @Test
    void testObtenerEstadoCuenta_Encontrado() {
        // Datos de prueba
        Long id = 1L;
        CuentaDTO cuentaDTO = new CuentaDTO();

        // Simulamos la respuesta del servicio
        when(cuentaService.obtenerEstadoCuenta(id)).thenReturn(cuentaDTO);

        // Llamada al método del controlador
        ResponseEntity<CuentaDTO> response = cuentaController.obtenerEstadoCuenta(id);

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cuentaDTO, response.getBody());
        verify(cuentaService, times(1)).obtenerEstadoCuenta(id);
    }

    @Test
    void testObtenerEstadoCuenta_NoEncontrado() {
        // Datos de prueba
        Long id = 1L;

        // Simulamos que el servicio no encuentra la cuenta
        when(cuentaService.obtenerEstadoCuenta(id)).thenReturn(null);

        // Llamada al método del controlador
        ResponseEntity<CuentaDTO> response = cuentaController.obtenerEstadoCuenta(id);

        // Verificaciones
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(cuentaService, times(1)).obtenerEstadoCuenta(id);
    }
}
