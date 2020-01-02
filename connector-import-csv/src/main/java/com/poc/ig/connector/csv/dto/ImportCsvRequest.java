package com.poc.ig.connector.csv.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ImportCsvRequest {

	private String sourcePath;
	private String tenant;
	private FileType fileType;
	
	
}
