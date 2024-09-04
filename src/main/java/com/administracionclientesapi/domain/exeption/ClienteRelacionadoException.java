package com.administracionclientesapi.domain.exeption;

public class ClienteRelacionadoException extends RuntimeException {
    public ClienteRelacionadoException(String mensaje) {
        super(mensaje);
    }
}
