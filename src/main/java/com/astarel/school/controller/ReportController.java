package com.astarel.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astarel.school.service.ReportService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/report")
public class ReportController {
	
	@Autowired
	ReportService reportService;
	
	@GetMapping("/generate-report")
    public ResponseEntity<String> generateReport() {
    	log.info("Generating Report");
        String reportUrl = reportService.generateReport();
        if (reportUrl != null) {
            return ResponseEntity.ok(reportUrl);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate report");
        }
    }

}
