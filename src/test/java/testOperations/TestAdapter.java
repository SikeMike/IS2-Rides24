package testOperations;

import businessLogic.BLFacade;
import businessLogic.BLFactory;
import domain.Driver;
import gui.DriverTable;

public class TestAdapter {

	public static void main(String[] args) {
		boolean isLocal = true;
		BLFacade blFacade = BLFactory.getBusinessLogicFactory();
		Driver driver = blFacade.getDriver("Urtzi");

		DriverTable driverTable = new DriverTable(driver);
		driverTable.setVisible(true);
	}

}
