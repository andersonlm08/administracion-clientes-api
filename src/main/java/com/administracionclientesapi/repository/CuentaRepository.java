package com.administracionclientesapi.repository;


import com.administracionclientesapi.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    boolean existsByClienteId(Long id);
}
