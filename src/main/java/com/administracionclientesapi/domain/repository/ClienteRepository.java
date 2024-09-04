package com.administracionclientesapi.domain.repository;


import com.administracionclientesapi.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}