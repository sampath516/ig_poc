package com.poc.ig.connector.im.importdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.eurekify.connectors.ccl.ConnectorConfiguration;
import com.eurekify.connectors.ccl.ConnectorConfigurationFactory;
import com.eurekify.connectors.ccl.XMLConnectorConfigurationFactory;
import com.eurekify.connectors.ccl.session.CCLSession;
import com.eurekify.connectors.ccl.session.SessionFactory;
import com.eurekify.connectors.ccl.session.SessionFactoryImpl;
import com.eurekify.connectors.ccl.validation.ConnectorConfigurationValidationException;
import com.eurekify.traceability.BuildingBlockTrace;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.poc.ig.connector.im.dto.UserResourceEntitlement;
import com.poc.ig.connector.im.dto.UserResourceEntitlement.ResourceDto;
import com.poc.ig.connector.im.dto.UserResourceEntitlement.UserDto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@Component
public class IMImportService {
	private static final Logger LOG = Logger.getLogger(IMImportService.class);

	private static final String CONF_FILE = "C:\\data\\IdentityGovernance\\PoC\\IM_Connector_Config.xml";
	private static final String OUTPUT_PATH = "C:\\data\\IdentityGovernance\\PoC\\import";
	private static final String PASSWORD = "test";
	
	private String importData(String tenantName, String certificationName) throws Exception {
		String outputPath = null;
		try {			
			ConnectorConfigurationFactory connectorConfigurationFactory = new XMLConnectorConfigurationFactory(
					new FileSystemResource(new File(CONF_FILE)));
			ConnectorConfiguration connectorConfiguration = connectorConfigurationFactory
					.createConnectorConfiguration();
			connectorConfiguration.validate(BuildingBlockTrace.EMPTY_TRACER);

			SessionFactory sessionFactory = new SessionFactoryImpl(connectorConfiguration, PASSWORD);
			CCLSession session = sessionFactory.createSession();
			File tempFolder = new File(System.getProperty("java.io.tmpdir"), tenantName+System.getProperty("file.separator")+certificationName);
			tempFolder.mkdirs();
			outputPath = tempFolder.getAbsolutePath() + System.getProperty("file.separator");
			session.setOutputPath(outputPath);
			session.executeImport();
			session.convertCsv2Cfg();

		} catch (ConnectorConfigurationValidationException e) {
			LOG.error("Invalid XML file. " + e.getMessage());
			throw e;
		} catch (Exception e) {
			LOG.error("Failed to import", e);
			throw e;
		}
		return outputPath;
	}
	
	public List<UserResourceEntitlement> getUserResourceEntitlementsEvent(String tenantName, String organizationName, String certificationName) {
		
		List<UserResourceEntitlement> entitlements = new ArrayList<UserResourceEntitlement>();
		Map<String, UserDto> usersMap = new HashMap<String, UserDto>();
		Map<String, ResourceDto> resourceMap = new HashMap<String, ResourceDto>();
		CSVReader csvReader = null;
		String[] nextRecord;
		String outputPath = null;
		try {
			outputPath = importData(tenantName, certificationName);
			//Users
			csvReader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(outputPath+"Users.udb"), "UTF-16")).withSkipLines(1).build();
			while ((nextRecord = csvReader.readNext()) != null) { 
				UserDto user = new UserDto(nextRecord[0], nextRecord[1], nextRecord[1], "", nextRecord[6], nextRecord[5]);
	            usersMap.put(nextRecord[0], user) ;
	        } 
			csvReader.close();
			//Resources
			csvReader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(outputPath+"Resources.rdb"), "UTF-16")).withSkipLines(1).build();
			while ((nextRecord = csvReader.readNext()) != null) { 
				ResourceDto resource = new ResourceDto(nextRecord[6], nextRecord[0],nextRecord[5], nextRecord[1], nextRecord[4], nextRecord[7]);
				resource.setName2(nextRecord[1]); 
				resource.setName3(nextRecord[2]);
				resourceMap.put(nextRecord[2], resource);
	        } 
			csvReader.close();
			
			//User Resource Links
			csvReader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(outputPath+"UserResource.csv"), "UTF-16")).build();
			UserDto userDto;
			ResourceDto resourceDto;
			UserResourceEntitlement entitlement;
			while ((nextRecord = csvReader.readNext()) != null) { 
				entitlement = new UserResourceEntitlement();
				entitlement.setTenantName(tenantName);
				entitlement.setOrganization(organizationName);
				if((userDto = usersMap.get(nextRecord[0])) != null && (resourceDto = resourceMap.get(nextRecord[3])) != null) {
					entitlement.setPrimaryEntity(userDto);
					entitlement.setSecondaryEntity(resourceDto);
					entitlements.add(entitlement);
				}
	        } 
			csvReader.close();			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entitlements;
	}
}
