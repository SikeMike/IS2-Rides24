package businessLogic;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class BLFactory {

	public static BLFacade getBusinessLogicFactory() {
		ConfigXML config = ConfigXML.getInstance();

		if (config.isBusinessLogicLocal()) {
			// Lógica de negocio local
			DataAccess dataAccess = new DataAccess();
			return new BLFacadeImplementation(dataAccess);
		} else {
			// Lógica de negocio remota
			try {
				String serviceName = "http://" + config.getBusinessLogicNode() + ":" + config.getBusinessLogicPort()
						+ "/ws/" + config.getBusinessLogicName() + "?wsdl";
				URL url = new URL(serviceName);
				QName qname = new QName("http://businessLogic/", "BLFacadeImplementationService");
				Service service = Service.create(url, qname);
				return service.getPort(BLFacade.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

}
