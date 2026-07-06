package com.ClinicSys.api.controller;

import com.ClinicSys.api.models.entity.EnderecoPaciente;
import com.ClinicSys.api.models.entity.Paciente;
import com.ClinicSys.api.models.repository.EnderecoRepository;
import com.ClinicSys.api.models.repository.PacienteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/atendimento/pacientes")
public class PacienteController {

    private final PacienteRepository pacienteRepository;
    private final EnderecoRepository enderecoRepository;

    public PacienteController(PacienteRepository pacienteRepository, EnderecoRepository enderecoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.enderecoRepository = enderecoRepository;
    }

    @GetMapping
    public String listar(Model model) {
        List<Paciente> pacientes = pacienteRepository.readAll();
        model.addAttribute("pacientes", pacientes);
        return "atendimento/pacientes/lista";
    }

    @GetMapping("/novo")
    public String novoForm() {
        return "atendimento/pacientes/formulario";
    }

    @PostMapping
    public String salvar(
            @RequestParam String cpf,
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam String numero,
            @RequestParam String plano_saude,
            @RequestParam String cep,
            @RequestParam String rua,
            @RequestParam String bairro,
            @RequestParam String municipio,
            @RequestParam String uf,
            RedirectAttributes redirectAttributes
    ) {
        EnderecoPaciente endereco = new EnderecoPaciente(null, cep, rua, bairro, municipio, uf);
        Long idEndereco = enderecoRepository.createEntityReturningId(endereco);

        if (idEndereco != null) {
            EnderecoPaciente enderecoComId = new EnderecoPaciente(idEndereco, cep, rua, bairro, municipio, uf);
            Paciente paciente = new Paciente(null, cpf, nome, enderecoComId, email, numero, plano_saude);
            boolean sucesso = pacienteRepository.createEntity(paciente);

            if (sucesso) {
                redirectAttributes.addFlashAttribute("mensagem", "Paciente cadastrado com sucesso!");
                redirectAttributes.addFlashAttribute("tipoMensagem", "success");
            } else {
                redirectAttributes.addFlashAttribute("mensagem", "Erro ao cadastrar paciente.");
                redirectAttributes.addFlashAttribute("tipoMensagem", "danger");
            }
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao cadastrar endereço do paciente.");
            redirectAttributes.addFlashAttribute("tipoMensagem", "danger");
        }

        return "redirect:/atendimento/pacientes";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteRepository.readEntityId(id);
        if (paciente == null) {
            return "redirect:/atendimento/pacientes";
        }

        EnderecoPaciente endereco = null;
        if (paciente.id_endereco() != null) {
            endereco = enderecoRepository.readEntityId(paciente.id_endereco().id());
        }

        model.addAttribute("paciente", paciente);
        model.addAttribute("endereco", endereco);
        return "atendimento/pacientes/formulario";
    }

    @PostMapping("/editar/{id}")
    public String atualizar(
            @PathVariable Long id,
            @RequestParam String cpf,
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam String numero,
            @RequestParam String plano_saude,
            RedirectAttributes redirectAttributes
    ) {
        Paciente paciente = new Paciente(id, cpf, nome, null, email, numero, plano_saude);
        boolean sucesso = pacienteRepository.updateEntity(paciente);

        if (sucesso) {
            redirectAttributes.addFlashAttribute("mensagem", "Paciente atualizado com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao atualizar paciente.");
            redirectAttributes.addFlashAttribute("tipoMensagem", "danger");
        }

        return "redirect:/atendimento/pacientes";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean sucesso = pacienteRepository.deleteEntity(id);

        if (sucesso) {
            redirectAttributes.addFlashAttribute("mensagem", "Paciente excluído com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao excluir paciente.");
            redirectAttributes.addFlashAttribute("tipoMensagem", "danger");
        }

        return "redirect:/atendimento/pacientes";
    }
}
