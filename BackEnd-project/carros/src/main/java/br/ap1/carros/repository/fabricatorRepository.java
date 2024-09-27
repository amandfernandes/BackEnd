package br.ap1.carros.repository; // Define o pacote onde esta interface está localizada.

import org.springframework.data.jpa.repository.JpaRepository; // Importa a interface JpaRepository para operações de CRUD.
import org.springframework.stereotype.Repository; // Importa a anotação Repository para indicar que é um repositório.

import br.ap1.carros.model.fabricator; // Importa a classe Fabricator.

@Repository // Marca a interface como um repositório do Spring.
public interface fabricatorRepository extends JpaRepository<fabricator, Integer> { 
    // Extende JpaRepository, permitindo operações de CRUD para a entidade Fabricator.
    // O primeiro parâmetro é a entidade e o segundo é o tipo da chave primária (Integer).
}
