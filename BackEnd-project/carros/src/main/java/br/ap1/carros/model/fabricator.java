package br.ap1.carros.model; // Define o pacote onde esta classe está localizada.

import java.time.LocalDate; // Importa a classe LocalDate para manipulação de datas.

import jakarta.persistence.*; // Importa as anotações JPA para mapeamento de entidades.
import lombok.Data; // Importa a anotação Lombok para geração automática de getters, setters, etc.

@Data // Gera automaticamente os métodos getters, setters, toString, equals e hashCode.
@Entity(name = "fabricator") // Marca a classe como uma entidade JPA e define o nome da tabela.
public class fabricator { // Declara a classe fabricator.

    @Id // Marca o campo id como a chave primária da entidade.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Define a estratégia de geração de valor da chave primária.
    private Integer id; // Declara o campo id.

    @Column // Marca o campo name como uma coluna da tabela.
    private String name; // Declara o campo name.

    @Column // Marca o campo date como uma coluna da tabela.
    private LocalDate date; // Declara o campo date para armazenar a data relacionada ao fabricante.
}
