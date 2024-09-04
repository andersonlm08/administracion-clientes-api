package com.administracionclientesapi.domain.repository;


import com.administracionclientesapi.domain.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
}
