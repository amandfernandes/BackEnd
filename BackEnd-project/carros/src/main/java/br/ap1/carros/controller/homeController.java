package br.ap1.carros.controller; // Define o pacote onde esta classe está localizada.

import org.springframework.web.bind.annotation.*; // Importa as anotações para mapeamento de requisições.

public class homeController { // Declara a classe homeController, que gerencia requisições para a página inicial.

    @GetMapping("/") // Mapeia requisições GET para a raiz do aplicativo ("/").
    public String index() { // Método que será chamado quando a rota raiz for acessada.
        return "index"; // Retorna o nome do template HTML "index", que será renderizado.
    }
}
