package com.administracionclientesapi.domain.repository;

import com.administracionclientesapi.domain.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
}
