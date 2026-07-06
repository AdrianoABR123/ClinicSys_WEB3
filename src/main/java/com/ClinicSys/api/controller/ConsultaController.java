package com.ClinicSys.api.controller;

import com.ClinicSys.api.models.entity.Consulta;
import com.ClinicSys.api.models.entity.Medico;
import com.ClinicSys.api.models.entity.Paciente;
import com.ClinicSys.api.models.repository.ConsultaRepository;
import com.ClinicSys.api.models.repository.MedicoRepository;
import com.ClinicSys.api.models.repository.PacienteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/atendimento/consultas")
public class ConsultaController {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public ConsultaController(ConsultaRepository consultaRepository, MedicoRepository medicoRepository, PacienteRepository pacienteRepository) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping
    public String listar(Model model) {
        List<Consulta> consultas = consultaRepository.readAll();
        model.addAttribute("consultas", consultas);
        return "atendimento/consultas/lista";
    }

    @GetMapping("/nova")
    public String novaForm(Model model) {
        List<Medico> medicos = medicoRepository.readALL();
        List<Paciente> pacientes = pacienteRepository.readAll();
        model.addAttribute("medicos", medicos);
        model.addAttribute("pacientes", pacientes);
        return "atendimento/consultas/formulario";
    }

    @PostMapping
    public String salvar(
            @RequestParam Long id_medico,
            @RequestParam Long id_paciente,
            @RequestParam String data_hora,
            @RequestParam(required = false) String data_hora_volta,
            @RequestParam(required = false) String observacao,
            RedirectAttributes redirectAttributes
    ) {
        Integer codigo = consultaRepository.getNextCodigo();

        Medico medico = medicoRepository.readEntityId(id_medico);
        Paciente paciente = pacienteRepository.readEntityId(id_paciente);

        LocalDateTime dataHora = LocalDateTime.parse(data_hora, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Timestamp tsDataHora = Timestamp.valueOf(dataHora);

        Timestamp tsDataHoraVolta = null;
        if (data_hora_volta != null && !data_hora_volta.isEmpty()) {
            LocalDateTime dataHoraVolta = LocalDateTime.parse(data_hora_volta, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            tsDataHoraVolta = Timestamp.valueOf(dataHoraVolta);
        }

        Consulta consulta = new Consulta(null, codigo, tsDataHora, tsDataHoraVolta, observacao, paciente, medico);
        boolean sucesso = consultaRepository.createEntity(consulta);

        if (sucesso) {
            redirectAttributes.addFlashAttribute("mensagem", "Consulta cadastrada com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao cadastrar consulta.");
            redirectAttributes.addFlashAttribute("tipoMensagem", "danger");
        }

        return "redirect:/atendimento/consultas";
    }
}
