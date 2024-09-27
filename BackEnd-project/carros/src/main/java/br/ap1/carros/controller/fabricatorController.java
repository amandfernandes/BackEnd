package br.ap1.carros.controller; // Define o pacote onde esta classe está localizada.

import org.springframework.beans.factory.annotation.Autowired; // Importa a anotação para injeção de dependência.
import org.springframework.stereotype.Controller; // Importa a anotação que marca a classe como um controlador Spring.
import org.springframework.ui.Model; // Importa a classe Model para passar dados ao template.
import org.springframework.web.bind.annotation.*; // Importa as anotações para mapeamento de requisições.
import br.ap1.carros.model.fabricator; // Importa a classe model para o fabricante.
import br.ap1.carros.repository.fabricatorRepository; // Importa a interface de repositório para o fabricante.

import java.util.*; // Importa as classes de utilidades, como List e Optional.

@Controller // Marca a classe como um controlador Spring MVC.
@RequestMapping("/fabricator") // Mapeia todas as requisições para o caminho "/fabricator".
public class fabricatorController { // Declara a classe fabricatorController.

    @Autowired // Permite a injeção automática do repositório de fabricantes.
    private fabricatorRepository fabricatorRepository; // Declara uma instância do repositório de fabricantes.

    // Método para exibir a lista de fabricantes.
    @GetMapping // Mapeia requisições GET para "/fabricator".
    public String getAllfabricators(Model model) { // Recebe um objeto Model para passar dados ao template.
        List<fabricator> fabricators = fabricatorRepository.findAll(); // Obtém a lista de todos os fabricantes do repositório.
        model.addAttribute("fabricators", fabricators); // Adiciona a lista de fabricantes ao modelo.
        return "fabricator"; // Retorna o nome do template HTML que exibirá a lista de fabricantes.
    }

    // Método para exibir o formulário de criação de um novo fabricante.
    @GetMapping("/new") // Mapeia requisições GET para "/fabricator/new".
    public String showCreateForm(Model model) { // Recebe um objeto Model para passar dados ao template.
        model.addAttribute("fabricator", new fabricator()); // Cria uma nova instância de fabricator e a adiciona ao modelo.
        return "fabricatorForm"; // Retorna o nome do template HTML do formulário de criação.
    }

    // Método para processar a criação de um novo fabricante.
    @PostMapping // Mapeia requisições POST para "/fabricator".
    public String createfabricator(@ModelAttribute fabricator fabricator) { // Recebe um objeto fabricator do formulário.
        fabricatorRepository.save(fabricator); // Salva o novo fabricante no repositório.
        return "redirect:/fabricator"; // Redireciona para a lista de fabricantes após a criação.
    }

    // Método para exibir o formulário de edição de um fabricante.
    @GetMapping("/{id}") // Mapeia requisições GET para "/fabricator/{id}" onde {id} é o ID do fabricante.
    public String getfabricatorById(@PathVariable Integer id, Model model) { // Recebe o ID do fabricante como parâmetro.
        Optional<fabricator> fabricator = fabricatorRepository.findById(id); // Busca o fabricante pelo ID.
        if (fabricator.isPresent()) { // Verifica se o fabricante foi encontrado.
            model.addAttribute("fabricator", fabricator.get()); // Adiciona o fabricante ao modelo.
            return "fabricatorEdit"; // Retorna o nome do template HTML do formulário de edição.
        } else {
            return "redirect:/fabricator"; // Redireciona para a lista se o fabricante não for encontrado.
        }
    }

    // Método para processar a atualização de um fabricante.
    @PostMapping("/{id}") // Mapeia requisições POST para "/fabricator/{id}" para atualizar o fabricante.
    public String updatefabricator(@PathVariable Integer id, @ModelAttribute fabricator fabricatorDetails) { // Recebe o ID e os novos dados do fabricante.
        Optional<fabricator> fabricator = fabricatorRepository.findById(id); // Busca o fabricante pelo ID.
        if (fabricator.isPresent()) { // Verifica se o fabricante foi encontrado.
            fabricator updatedfabricator = fabricator.get(); // Obtém o fabricante encontrado.
            updatedfabricator.setName(fabricatorDetails.getName()); // Atualiza o nome do fabricante.
            updatedfabricator.setDate(fabricatorDetails.getDate()); // Atualiza a data do fabricante.
            fabricatorRepository.save(updatedfabricator); // Salva as alterações no repositório.
        }
        return "redirect:/fabricator"; // Redireciona para a lista de fabricantes após a atualização.
    }

    // Método para excluir um fabricante.
    @PostMapping("/{id}/delete") // Mapeia requisições POST para "/fabricator/{id}/delete" para excluir o fabricante.
    public String deletefabricator(@PathVariable Integer id) { // Recebe o ID do fabricante a ser excluído.
        Optional<fabricator> fabricator = fabricatorRepository.findById(id); // Busca o fabricante pelo ID.
        if (fabricator.isPresent()) { // Verifica se o fabricante foi encontrado.
            fabricatorRepository.delete(fabricator.get()); // Exclui o fabricante do repositório.
        }
        return "redirect:/fabricator"; // Redireciona para a lista de fabricantes após a exclusão.
    }
}
