package com.astarel.school.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.astarel.school.service.ReportService;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

	@Mock
	private ReportService reportService;

	private ReportController controller;

	@BeforeEach
	void setUp() {
		controller = new ReportController();
		controller.reportService = reportService;
	}

	@Test
	void generateReportReturnsOkWhenServiceProvidesAUrl() {
		when(reportService.generateReport()).thenReturn("reports/School.pdf");

		ResponseEntity<String> response = controller.generateReport();

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo("reports/School.pdf");
	}

	@Test
	void generateReportReturnsInternalServerErrorWhenServiceFails() {
		when(reportService.generateReport()).thenReturn(null);

		ResponseEntity<String> response = controller.generateReport();

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(response.getBody()).isEqualTo("Failed to generate report");
	}
}
