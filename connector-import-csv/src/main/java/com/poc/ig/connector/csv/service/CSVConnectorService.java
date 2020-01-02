package com.poc.ig.connector.csv.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.poc.ig.connector.csv.dto.ImportCsvRequest;

@RestController
@RequestMapping(value = "ig/import/csv/v1/", produces = "application/json", consumes = "application/json")
public class CSVConnectorService {
	
	
	
	
	@PostMapping(path = "import")
	@ResponseStatus(HttpStatus.OK)
	public void importCsv(@RequestBody ImportCsvRequest importCsvRequest) {
		
		
		
	
	}

}
