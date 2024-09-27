package br.ap1.carros.model; // Define o pacote onde esta classe está localizada.

import java.time.LocalDate; // Importa a classe LocalDate para manipulação de datas.
import jakarta.persistence.*; // Importa as anotações JPA para mapeamento de entidades.
import lombok.Data; // Importa a anotação Lombok para geração automática de métodos.

@Data // Gera automaticamente os métodos getters, setters, toString, equals e hashCode.
@Entity // Marca a classe como uma entidade JPA.
@Table(name = "vehicles") // Define o nome da tabela como "vehicles".
public class vehicles { // Declara a classe Vehicles. (Convencionalmente, as classes devem ter a primeira letra maiúscula.)

    @Id // Marca o campo id como a chave primária da entidade.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Define a estratégia de geração de valor da chave primária.
    private Integer id; // Declara o campo id.

    @Column // Marca o campo name como uma coluna da tabela.
    private String name; // Declara o campo name para armazenar o nome do veículo.

    @Column // Marca o campo type como uma coluna da tabela.
    private String type; // Declara o campo type para armazenar o tipo do veículo.

    @Column // Marca o campo createDate como uma coluna da tabela.
    private LocalDate createDate; // Declara o campo createDate para armazenar a data de criação do veículo.

    @Column // Marca o campo description como uma coluna da tabela.
    private String description; // Declara o campo description para armazenar uma descrição do veículo.

    @Column // Marca o campo image como uma coluna da tabela.
    private String image; // Declara o campo image para armazenar o caminho da imagem do veículo.

    @ManyToOne // Define uma relação Many-to-One com a entidade Fabricator.
    @JoinColumn(name = "fabricatorId") // Especifica a coluna que faz a junção com a tabela Fabricator.
    private fabricator fabricator; // Declara o campo fabricator para armazenar a referência ao fabricante associado ao veículo.
}
