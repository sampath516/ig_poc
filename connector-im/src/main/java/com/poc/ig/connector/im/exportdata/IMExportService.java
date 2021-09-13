package com.poc.ig.connector.im.exportdata;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.eurekify.command.CommandExecutionException;
import com.eurekify.connectors.ccl.ConnectorConfiguration;
import com.eurekify.connectors.ccl.ConnectorConfigurationFactory;
import com.eurekify.connectors.ccl.RemovedRedundantUpdateEntriesXmlDiffParser;
import com.eurekify.connectors.ccl.XMLConnectorConfigurationFactory;
import com.eurekify.connectors.ccl.im_export.redesign.LogOnlyExportListener;
import com.eurekify.connectors.ccl.session.CCLSession;
import com.eurekify.connectors.ccl.session.SessionFactory;
import com.eurekify.connectors.ccl.session.SessionFactoryImpl;
import com.eurekify.connectors.common.diffFileParser.XmlDiffFileParser;
import com.poc.ig.connector.im.events.dto.EntitlementsRejectedEvent.Entitlement;
import com.poc.ig.connector.im.events.dto.EntitlementsRejectedEvent.SecondaryEntity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@Component
public class IMExportService {
	private static final Logger LOG = Logger.getLogger(IMExportService.class);
	private static final String CONF_FILE = "C:\\data\\IdentityGovernance\\PoC\\IM_Connector_Config.xml";
	private static final String PASSWORD = "test";
	
	private void exportData(String diffFile) throws Exception {
        try {
            ConnectorConfigurationFactory connectorConfigurationFactory = new XMLConnectorConfigurationFactory(new FileSystemResource(CONF_FILE));
            ConnectorConfiguration connectorConfiguration = connectorConfigurationFactory.createConnectorConfiguration();

            SessionFactory sessionFactory = new SessionFactoryImpl(connectorConfiguration, PASSWORD);
            CCLSession session = sessionFactory.createSession();
            session.addExportListener(new LogOnlyExportListener());
            session.executeExport(new RemovedRedundantUpdateEntriesXmlDiffParser(new XmlDiffFileParser(diffFile)));
        } catch (CommandExecutionException e) {
            LOG.error("Failed to export", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Failed to export", e);
            throw e;
        }
	}
	
	public void exportUserResourceEntitlementRejections(String tenantName, String certificationName, List<Entitlement> entitlements) throws Exception {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			
			Document doc = docBuilder.newDocument();
			
			// root elements (DIFFS)
			Element rootElement = doc.createElement("DIFFS");
			rootElement.setAttribute("VERSION", "1.0");
			doc.appendChild(rootElement);
			
			for(Entitlement ent: entitlements) {
				
				SecondaryEntity secondary = ent.getSecondaryEntity();
				
				// DIFF elements
				Element diff = doc.createElement("DIFF");
				rootElement.appendChild(diff);

				// set attributes to DIFF element
				diff.setAttribute("COMMAND", "REMOVE_USER_RESOURCE_LINK");
				diff.setAttribute("RESOURCE_UNIQUE_ID", secondary.getExternalId());
				diff.setAttribute("RESOURCE_TYPE", secondary.getProperties().get("type"));
				diff.setAttribute("PERSON_ID", ent.getPrimaryEntity().getExternalId());
				diff.setAttribute("RES_NAME_1", secondary.getProperties().get("name"));
				diff.setAttribute("RES_NAME_2", secondary.getProperties().get("name2"));
				diff.setAttribute("RES_NAME_3", secondary.getProperties().get("name3"));			
			}
			
			File tempFolder = new File(System.getProperty("java.io.tmpdir"), "export"+System.getProperty("file.separator")+tenantName+System.getProperty("file.separator")+certificationName);
			tempFolder.mkdirs();
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			File diffFile = new File(tempFolder+System.getProperty("file.separator")+"diff_file"+new Date().getTime()+".xml");
			StreamResult result = new StreamResult(diffFile);
			transformer.transform(source, result);
			exportData(diffFile.getAbsolutePath());
		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
			throw pce;
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
			throw tfe;
		  } catch (Exception e) {
			e.printStackTrace();
			throw e;
		}		
	}
}
