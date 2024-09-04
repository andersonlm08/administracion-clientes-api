package com.administracionclientesapi.controller;

import com.administracionclientesapi.dto.TransaccionDTO;
import com.administracionclientesapi.service.TransaccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping
    public ResponseEntity<TransaccionDTO> realizarTransaccion(@RequestBody TransaccionDTO transaccionDTO) {
        TransaccionDTO nuevaTransaccion = transaccionService.realizarTransaccion(transaccionDTO);
        return new ResponseEntity<>(nuevaTransaccion, HttpStatus.CREATED);
    }
}
