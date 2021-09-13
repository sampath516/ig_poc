package com.poc.ig.connector.im.service;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eurekify.command.CommandExecutionException;
import com.eurekify.connectors.ccl.ConnectorConfiguration;
import com.eurekify.connectors.ccl.ConnectorConfigurationFactory;
import com.eurekify.connectors.ccl.XMLConnectorConfigurationFactory;
import com.eurekify.connectors.ccl.session.CCLSession;
import com.eurekify.connectors.ccl.session.SessionFactory;
import com.eurekify.connectors.ccl.session.SessionFactoryImpl;
import com.eurekify.connectors.ccl.validation.ConnectorConfigurationValidationException;
import com.eurekify.connectors.common.Utils;
import com.eurekify.traceability.BuildingBlockTrace;

@RestController
@RequestMapping(value = "ig/connector/im/v1/tenants/{tenantName}/certification-data/import", produces = "application/json", consumes = "application/json")
public class CertificationDataService {

	private static final Logger LOG = Logger.getLogger(CertificationDataService.class);
	private static final String CONF_FILE = "C:\\data\\IdentityGovernance\\PoC\\IM_Connector_Config.xml";
	private static final String OUTPUT_PATH = "C:\\data\\IdentityGovernance\\PoC\\import";
	private static final String PASSWORD = "test";

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping()
	public void importCertificationData() {

		try {
			Utils.initLog4J(LOG);
			ConnectorConfigurationFactory connectorConfigurationFactory = new XMLConnectorConfigurationFactory(
					new FileSystemResource(new File(CONF_FILE)));
			ConnectorConfiguration connectorConfiguration = connectorConfigurationFactory
					.createConnectorConfiguration();
			connectorConfiguration.validate(BuildingBlockTrace.EMPTY_TRACER);

			SessionFactory sessionFactory = new SessionFactoryImpl(connectorConfiguration, PASSWORD);
			CCLSession session = sessionFactory.createSession();

			session.setOutputPath(OUTPUT_PATH);
			session.executeImport();
			session.convertCsv2Cfg();

			System.exit(0);
		} catch (ConnectorConfigurationValidationException e) {
			LOG.error("Invalid XML file. " + e.getMessage());
		} catch (CommandExecutionException e) {
			LOG.error("Failed to import", e);
			System.exit(-1);
		} catch (Exception e) {
			LOG.error("Failed to import", e);
			System.exit(-2);
		}

	}

}
