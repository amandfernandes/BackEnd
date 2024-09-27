package br.ap1.carros.repository;

import org.springframework.data.jpa.repository.JpaRepository; // Importa a interface JpaRepository para operações de CRUD.
import org.springframework.stereotype.Repository; // Importa a anotação Repository para indicar que é um repositório.

import br.ap1.carros.model.vehicles; // Importa a classe Vehicles.

@Repository // Marca a interface como um repositório do Spring.
public interface vehiclesRepository extends JpaRepository<vehicles, Integer> { 
    // Extende JpaRepository, permitindo operações de CRUD para a entidade Vehicles.
}
