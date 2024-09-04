package com.administracionclientesapi.exeption;

public class ClienteRelacionadoException extends RuntimeException {
    public ClienteRelacionadoException(String mensaje) {
        super(mensaje);
    }
}
