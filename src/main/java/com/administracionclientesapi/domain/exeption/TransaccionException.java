package com.administracionclientesapi.domain.exeption;

public class TransaccionException extends RuntimeException {
    public TransaccionException(String mensaje) {
        super(mensaje);
    }
}
