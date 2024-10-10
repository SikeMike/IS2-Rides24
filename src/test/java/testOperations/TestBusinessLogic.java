package testOperations;

import java.util.Date;

import configuration.ConfigXML;
import domain.Driver;
import domain.Ride;

public class TestBusinessLogic {
	TestDataAccess dbManagerTest;
 	
    
	   public TestBusinessLogic()  {
			
			System.out.println("Creating TestBusinessLogic instance");
			@SuppressWarnings("unused")
			ConfigXML c=ConfigXML.getInstance();
			dbManagerTest=new TestDataAccess(); 
			dbManagerTest.close();
		}
		
		 //A
		public boolean removeDriver(String driverEmail) {
			dbManagerTest.open();
			boolean b=dbManagerTest.removeDriver(driverEmail);
			dbManagerTest.close();
			return b;

		}
		
		public Driver createDriver(String email, String name) {
			dbManagerTest.open();
			Driver driver=dbManagerTest.createDriver(email, name);
			dbManagerTest.close();
			return driver;

		}
		
		public boolean existDriver(String email) {
			dbManagerTest.open();
			boolean existDriver=dbManagerTest.existDriver(email);
			dbManagerTest.close();
			return existDriver;

		}
		
		public Driver addDriverWithRide(String name, String from, String to,  Date date, int nPlaces, float price) {
			dbManagerTest.open();
			Driver driver=dbManagerTest.addDriverWithRide(name, from, to, date, nPlaces, price);
			dbManagerTest.close();
			return driver;

		}
		public boolean existRide(String email, String from, String to, Date date) {
			dbManagerTest.open();
			boolean b=dbManagerTest.existRide(email, from, to, date);
			dbManagerTest.close();
			return b;
		}
		public Ride removeRide(String email,String from, String to, Date date ) {
			dbManagerTest.open();
			Ride r=dbManagerTest.removeRide( email, from,  to,  date );
			dbManagerTest.close();
			return r;
		}
		


}
