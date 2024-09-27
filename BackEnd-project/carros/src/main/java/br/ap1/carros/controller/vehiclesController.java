package br.ap1.carros.controller; // Define o pacote onde esta classe está localizada.

import br.ap1.carros.model.fabricator; // Importa a classe model para o fabricante.
import br.ap1.carros.model.vehicles; // Importa a classe model para veículos.
import br.ap1.carros.repository.vehiclesRepository; // Importa a interface de repositório para veículos.
import jakarta.annotation.PostConstruct; // Importa a anotação para inicialização pós-construção.
import br.ap1.carros.repository.fabricatorRepository; // Importa a interface de repositório para fabricantes.
import org.springframework.beans.factory.annotation.Autowired; // Importa a anotação para injeção de dependência.
import org.springframework.stereotype.Controller; // Importa a anotação que marca a classe como um controlador Spring.
import org.springframework.ui.Model; // Importa a classe Model para passar dados ao template.
import org.springframework.web.bind.annotation.*; // Importa as anotações para mapeamento de requisições.
import org.springframework.web.multipart.MultipartFile; // Importa a classe para manipulação de arquivos enviados.
import java.io.IOException; // Importa a exceção para tratamento de erros de entrada/saída.
import java.nio.file.Files; // Importa a classe para manipulação de arquivos.
import java.nio.file.Path; // Importa a classe para caminhos de arquivos.
import java.nio.file.Paths; // Importa a classe para manipulação de caminhos de arquivos.

import java.util.List; // Importa a classe List para listas.
import java.util.Optional; // Importa a classe Optional para tratamento de valores ausentes.

@Controller // Marca a classe como um controlador Spring MVC.
@RequestMapping("/vehicles") // Mapeia todas as requisições para o caminho "/vehicles".
public class vehiclesController { // Declara a classe vehiclesController.

    @Autowired // Permite a injeção automática do repositório de veículos.
    private vehiclesRepository vehiclesRepository; // Declara uma instância do repositório de veículos.

    @Autowired // Permite a injeção automática do repositório de fabricantes.
    private fabricatorRepository fabricatorRepository; // Declara uma instância do repositório de fabricantes.

    private static final String UPLOADED_FOLDER = "uploads/"; // Define o caminho para a pasta de uploads.

    @PostConstruct // Método que é executado após a construção da classe.
    public void init() { // Método para inicialização.
        Path uploadPath = Paths.get(UPLOADED_FOLDER); // Cria um objeto Path para o diretório de uploads.
        if (!Files.exists(uploadPath)) { // Verifica se o diretório de uploads não existe.
            try {
                Files.createDirectories(uploadPath); // Cria o diretório se não existir.
            } catch (IOException e) { // Captura exceção de entrada/saída.
                e.printStackTrace(); // Imprime a pilha de erros.
            }
        }
    }

    // Método para exibir a lista de veículos.
    @GetMapping // Mapeia requisições GET para "/vehicles".
    public String getAllvehicles(Model model) { // Recebe um objeto Model para passar dados ao template.
        List<vehicles> vehicles = vehiclesRepository.findAll(); // Obtém a lista de todos os veículos do repositório.
        model.addAttribute("vehicles", vehicles); // Adiciona a lista de veículos ao modelo.
        return "vehicles"; // Retorna o nome do template Thymeleaf que exibirá a lista de veículos.
    }

    // Método para exibir o formulário de criação de um novo veículo.
    @GetMapping("/new") // Mapeia requisições GET para "/vehicles/new".
    public String showCreateForm(Model model) { // Recebe um objeto Model para passar dados ao template.
        model.addAttribute("vehicle", new vehicles()); // Cria uma nova instância de vehicles e a adiciona ao modelo.
        model.addAttribute("fabricators", fabricatorRepository.findAll()); // Adiciona a lista de fabricantes ao modelo.
        return "vehiclesForm"; // Retorna o nome do template Thymeleaf do formulário de criação.
    }

    // Método para processar a criação de um novo veículo.
    @PostMapping // Mapeia requisições POST para "/vehicles".
    public String createVehicle(@ModelAttribute vehicles vehicle, @RequestParam("imageFile") MultipartFile imageFile) throws IOException { // Recebe um objeto vehicles do formulário e o arquivo de imagem.
        // Manipulação do upload da imagem.
        if (!imageFile.isEmpty()) { // Verifica se o arquivo de imagem não está vazio.
            byte[] bytes = imageFile.getBytes(); // Obtém os bytes do arquivo de imagem.

            // Garante nomes de arquivos únicos para evitar sobrescritas.
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename(); // Gera um nome de arquivo único.

            Path path = Paths.get(UPLOADED_FOLDER + fileName); // Cria um objeto Path para o novo arquivo.
            Files.write(path, bytes); // Escreve os bytes no arquivo no diretório de uploads.

            // Define o caminho da imagem relativo ao contexto do servidor.
            vehicle.setImage("/uploads/" + fileName); // Define o caminho da imagem no objeto vehicle.
        }

        // Associa o fabricante ao veículo.
        Optional<fabricator> fabricatorOptional = fabricatorRepository.findById(vehicle.getFabricator().getId()); // Busca o fabricante pelo ID associado ao veículo.
        if (fabricatorOptional.isPresent()) { // Verifica se o fabricante foi encontrado.
            vehicle.setFabricator(fabricatorOptional.get()); // Associa o fabricante encontrado ao veículo.
            vehiclesRepository.save(vehicle); // Salva o novo veículo no repositório.
            return "redirect:/vehicles"; // Redireciona para a lista de veículos após a criação.
        } else {
            return "redirect:/vehicles?error=fabricanteNaoEncontrado"; // Redireciona com um erro se o fabricante não for encontrado.
        }
    }

    // Método para exibir o formulário de edição de um veículo.
    @GetMapping("/{id}") // Mapeia requisições GET para "/vehicles/{id}" onde {id} é o ID do veículo.
    public String showEditForm(@PathVariable Integer id, Model model) { // Recebe o ID do veículo como parâmetro.
        Optional<vehicles> vehicleOptional = vehiclesRepository.findById(id); // Busca o veículo pelo ID.
        if (vehicleOptional.isPresent()) { // Verifica se o veículo foi encontrado.
            vehicles vehicle = vehicleOptional.get(); // Obtém o veículo encontrado.
            model.addAttribute("vehicle", vehicle); // Adiciona o veículo ao modelo.
            model.addAttribute("fabricators", fabricatorRepository.findAll()); // Adiciona a lista de fabricantes ao modelo.
            return "vehiclesEdit"; // Retorna o nome do template Thymeleaf para o formulário de edição.
        } else {
            return "redirect:/vehicles?error=vehicleNaoEncontrado"; // Redireciona em caso de erro se o veículo não for encontrado.
        }
    }

    // Método para processar a atualização de um veículo.
    @PostMapping("/{id}") // Mapeia requisições POST para "/vehicles/{id}" para atualizar o veículo.
    public String editVehicle(@PathVariable Integer id, @ModelAttribute vehicles vehicleDetails, @RequestParam("imageFile") MultipartFile imageFile) throws IOException { // Recebe o ID e os novos dados do veículo.
        Optional<vehicles> vehicleOptional = vehiclesRepository.findById(id); // Busca o veículo pelo ID.

        if (vehicleOptional.isPresent()) { // Verifica se o veículo foi encontrado.
            vehicles vehicle = vehicleOptional.get(); // Obtém o veículo encontrado.
            vehicle.setName(vehicleDetails.getName()); // Atualiza o nome do veículo.
            vehicle.setType(vehicleDetails.getType()); // Atualiza o tipo do veículo.
            vehicle.setCreateDate(vehicleDetails.getCreateDate()); // Atualiza a data de criação do veículo.
            vehicle.setDescription(vehicleDetails.getDescription()); // Atualiza a descrição do veículo.
            vehicle.setImage(vehicleDetails.getImage()); // Mantém o caminho da imagem existente.

            // Manipulação do upload da nova imagem.
            if (!imageFile.isEmpty()) { // Verifica se o novo arquivo de imagem não está vazio.
                // Opcionalmente, exclui o arquivo de imagem antigo.
                if (vehicle.getImage() != null) { // Verifica se há uma imagem existente.
                    String oldImagePath = vehicle.getImage().replace("/uploads/", ""); // Obtém o caminho da imagem antiga.
                    Path oldImage = Paths.get(UPLOADED_FOLDER + oldImagePath); // Cria um objeto Path para a imagem antiga.
                    Files.deleteIfExists(oldImage); // Exclui o arquivo de imagem antigo se existir.
                }

                byte[] bytes = imageFile.getBytes(); // Obtém os bytes do novo arquivo de imagem.
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename(); // Gera um nome de arquivo único para a nova imagem.
                Path path = Paths.get(UPLOADED_FOLDER + fileName); // Cria um objeto Path para o novo arquivo.
                Files.write(path, bytes); // Escreve os bytes no novo arquivo no diretório de uploads.
                vehicle.setImage("/uploads/" + fileName); // Define o novo caminho da imagem no objeto vehicle.
            }

            // Associa o fabricante atualizado.
            Optional<fabricator> fabricatorOptional = fabricatorRepository.findById(vehicleDetails.getFabricator().getId()); // Busca o fabricante pelo ID.
            fabricatorOptional.ifPresent(vehicle::setFabricator); // Se encontrado, associa o fabricante ao veículo.

            vehiclesRepository.save(vehicle); // Salva o veículo atualizado no repositório.
            return "redirect:/vehicles"; // Redireciona para a lista de veículos após a edição.
        } else {
            return "redirect:/vehicles?error=vehicleNaoEncontrado"; // Redireciona em caso de erro se o veículo não for encontrado.
        }
    }

    // Método para excluir um veículo.
    @PostMapping("/{id}/delete") // Mapeia requisições POST para "/vehicles/{id}/delete" para deletar o veículo.
    public String deleteVehicle(@PathVariable Integer id) { // Recebe o ID do veículo a ser excluído.
        Optional<vehicles> vehicleOptional = vehiclesRepository.findById(id); // Busca o veículo pelo ID.
        if (vehicleOptional.isPresent()) { // Verifica se o veículo foi encontrado.
            vehicles vehicle = vehicleOptional.get(); // Obtém o veículo encontrado.
            // Exclui o arquivo de imagem associado.
            if (vehicle.getImage() != null) { // Verifica se há uma imagem associada.
                String imagePath = vehicle.getImage().replace("/uploads/", ""); // Obtém o caminho da imagem.
                Path path = Paths.get(UPLOADED_FOLDER + imagePath); // Cria um objeto Path para o arquivo de imagem.
                try {
                    Files.deleteIfExists(path); // Exclui o arquivo de imagem se existir.
                } catch (IOException e) { // Captura exceção de entrada/saída.
                    e.printStackTrace(); // Imprime a pilha de erros.
                }
            }
            vehiclesRepository.delete(vehicle); // Exclui o veículo do repositório.
        }
        return "redirect:/vehicles"; // Redireciona para a lista de veículos após a exclusão.
    }
}
