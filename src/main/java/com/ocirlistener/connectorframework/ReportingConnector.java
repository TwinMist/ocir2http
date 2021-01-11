package com.ocirlistener.connectorframework;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ocirlistener.config.ApplicationConfig;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReportingConnector extends ThirdPartyConnector<Void> {
	private static Logger HTTP_LOGGER = Logger.getLogger("httpLogger");
	@Autowired
	private RestTemplate restTemplate;

	@Override
	protected ResponseEntity<Void> sendRecv(String url,String report) throws ConnectorException {
		try {
			HTTP_LOGGER.info("Url:"+url);
			HTTP_LOGGER.info("Body:"+report);
			System.out.println(restTemplate);
			HttpEntity<String> entity = new HttpEntity<String>(report);
			return restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
		}catch(Exception e) {
			System.out.println(ApplicationConfig.getStackTraceString(e));
			throw new ConnectorException();
		}
	}

	@Override
	protected boolean validateResponse(ResponseEntity<Void> response) {
		return response.getStatusCode().is2xxSuccessful();
	}

	public static void main(String[] args)  {
		//ApplicationConfig.load();
		ApplicationConfig.thirdPartyConfiguration.setRemoteEndPoint("http://localhost:8080/api/report/12345678");
		try {
			String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + 
					"<BroadsoftOCIReportingDocument xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" protocol=\"OCIReporting\">\r\n" + 
					"<command xsi:type=\"OCIReportingReportNotification\">\r\n" + 
					"    <id>write154</id>\r\n" + 
					"    <userId>admin</userId>\r\n" + 
					"    <loginType>System</loginType>\r\n" + 
					"    <request>\r\n" + 
					"        <![CDATA[<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + 
					"        <BroadsoftDocument protocol=\"OCI\" xmlns=\"C\">\r\n" + 
					"            <userId xmlns=\"\">admin</userId>\r\n" + 
					"            <command xsi:type=\"UserModifyRequest17sp4\" xmlns=\"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
					"            <userId>usr001@lab24.timezone4.com</userId>\r\n" + 
					"            <phoneNumber xsi:nil=\"true\"/>\r\n" + 
					"            <extension>1010</extension>\r\n" + 
					"            <sipAliasList xsi:nil=\"true\"/>\r\n" + 
					"            <endpoint>\r\n" + 
					"                <accessDeviceEndpoint>\r\n" + 
					"                    <accessDevice>\r\n" + 
					"                        <deviceLevel>Group</deviceLevel>\r\n" + 
					"                        <deviceName>bruger001</deviceName>\r\n" + 
					"                    </accessDevice>\r\n" + 
					"                    <linePort>usr001@lab24.timezone4.com</linePort>\r\n" + 
					"                    <contactList xsi:nil=\"true\"/>\r\n" + 
					"                </accessDeviceEndpoint>\r\n" + 
					"            </endpoint>\r\n" + 
					"        </command>\r\n" + 
					"    </BroadsoftDocument>]]>\r\n" + 
					"</request>\r\n" + 
					"</command>\r\n" + 
					"<command xsi:type=\"OCIReportingReportPublicIdentityNotification\">\r\n" + 
					"    <modifiedUser>\r\n" + 
					"        <userId>usr001@lab24.timezone4.com</userId>\r\n" + 
					"        <addedSipURI>usr001@lab24.timezone4.com</addedSipURI>\r\n" + 
					"    </modifiedUser>\r\n" + 
					"</command>\r\n" + 
					"</BroadsoftOCIReportingDocument>";
			//XmlConvertor<List<OCIReportingCommand>> convertor = new XmlJaxbConvertor();
			//OciReport report = convertor.convert(xml);
			//report.setActor("yugal");
			//report.setReportId("12345678");
			ThirdPartyConnector<Void> httpConnector = new ReportingConnector();
			httpConnector.transact("http://188.181.136.23:8080/api/report/98765432",xml);
		} catch (MaximumRetriesReachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
}
