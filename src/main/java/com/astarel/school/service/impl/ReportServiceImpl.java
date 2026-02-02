package com.astarel.school.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.astarel.school.service.ReportService;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportServiceImpl implements ReportService {

	@Value("${report.directory}")
	private String reportDirectory;

	@Override
	public String generateReport() {
		// TODO Auto-generated method stub
		String fileName = "/School.pdf";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("header", "This is the Report Header");
		try {
			JRDataSource dataSource = new JRBeanCollectionDataSource(new ArrayList<String>());
			InputStream inputStream = getClass().getResourceAsStream("/reports/sample.jasper");

			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, dataSource);
			ClassPathResource resource = new ClassPathResource(reportDirectory);

			JasperExportManager.exportReportToPdfFile(jasperPrint, resource.getFile().getAbsolutePath() + fileName);
			return "reports/" + fileName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// Handle exception
			e.printStackTrace();
			return null;
		}
		return "";
	}

}
