package com.administracionclientesapi.controller;

import com.administracionclientesapi.dto.CuentaDTO;
import com.administracionclientesapi.dto.EstadoCuentaDTO;
import com.administracionclientesapi.service.CuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping
    public ResponseEntity<CuentaDTO> crearCuenta(@RequestBody CuentaDTO cuentaDTO) {
        CuentaDTO nuevaCuenta = cuentaService.crearCuenta(cuentaDTO);
        return new ResponseEntity<>(nuevaCuenta, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstadoCuenta(@PathVariable Long id, @RequestBody EstadoCuentaDTO estadoCuentaDTO) {
        cuentaService.actualizarEstadoCuenta(id, estadoCuentaDTO.getEstadoCuenta());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> obtenerEstadoCuenta(@PathVariable Long id) {
        CuentaDTO cuentaDTO = cuentaService.obtenerEstadoCuenta(id);
        if (cuentaDTO != null) {
            return ResponseEntity.ok(cuentaDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
