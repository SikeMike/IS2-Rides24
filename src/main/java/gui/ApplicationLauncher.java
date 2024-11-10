package gui;

import java.net.URL;
import java.util.Locale;

import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import businessLogic.BLFactory;

public class ApplicationLauncher {

	public static void main(String[] args) {

		ConfigXML c = ConfigXML.getInstance();

		System.out.println(c.getLocale());

		Locale.setDefault(new Locale(c.getLocale()));

		System.out.println("Locale: " + Locale.getDefault());

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

			BLFacade appFacadeInterface = BLFactory.getBusinessLogicFactory();

			MainGUI.setBussinessLogic(appFacadeInterface);

			MainGUI mainGUI = new MainGUI();
			mainGUI.setVisible(true);

		} catch (Exception e) {
			// a.jLabelSelectOption.setText("Error: "+e.toString());
			// a.jLabelSelectOption.setForeground(Color.RED);

			System.out.println("Error in ApplicationLauncher: " + e.toString());
		}
		// a.pack();

	}

}
