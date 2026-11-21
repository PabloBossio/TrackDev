package com.Trackdev.control.Controller;

import com.Trackdev.control.Dto.AlertaDTO;
import com.Trackdev.control.Service.AlertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
public class AlertaController {

    private final AlertaService alertaService;

    @GetMapping
    public List<AlertaDTO> listar() {
        return alertaService.listarAlertas();
    }
}