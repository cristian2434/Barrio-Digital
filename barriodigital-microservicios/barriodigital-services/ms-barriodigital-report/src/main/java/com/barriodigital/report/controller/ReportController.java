package com.barriodigital.report.controller;

import com.barriodigital.report.dto.KpiResponse;
import com.barriodigital.report.dto.TopProcedureResponse;
import com.barriodigital.report.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Endpoints de solo lectura para el rol Admin (panel de KPIs). */
@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping("/kpis")
    public KpiResponse kpis(@RequestParam(required = false, defaultValue = "last24h") String range) {
        return service.kpis(range);
    }

    @GetMapping("/top-procedures")
    public List<TopProcedureResponse> topProcedures(@RequestParam(required = false, defaultValue = "last7d") String range) {
        return service.topProcedures(range);
    }
}
