package com.administracionclientesapi.util;

import com.administracionclientesapi.entity.Cuenta;
import com.administracionclientesapi.exeption.ValidacionDataException;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorUtil {

    public static void validarEdad(LocalDate fechaNacimiento) {
        LocalDate hoy = LocalDate.now();
        Period edad = Period.between(fechaNacimiento, hoy);

        if (edad.getYears() < 18) {
            throw new ValidacionDataException("El cliente debe ser mayor de 18 años");
        }
    }

    public static void validarCorreoElectronico(String correo) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);

        if (!matcher.matches()) {
            throw new ValidacionDataException("El formato del correo electrónico es inválido");
        }
    }
    public static void validarNombreYApellido(String nombre, String apellido) {
        if (nombre == null || nombre.length() <= 1) {
            throw new ValidacionDataException("El nombre debe tener más de 1 carácter");
        }
        if (apellido == null || apellido.length() <= 1) {
            throw new ValidacionDataException("El apellido debe tener más de 1 carácter");
        }
    }
    public static void validarSaldoInicial(double saldo) {
        if (saldo <= 0) {
            throw new ValidacionDataException("El saldo inicial debe ser mayor a 0 para una cuenta de ahorros");
        }
    }
    public static void  validarSaldoParaCancelarCuenta(Cuenta cuenta) {
        if (cuenta.getSaldo() != 0) {
            throw new ValidacionDataException("El saldo de la cuenta debe ser 0 para cancelarla");
        }
    }
}
