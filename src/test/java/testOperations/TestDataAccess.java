package testOperations;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import domain.Driver;
import domain.Ride;
import domain.User;


public class TestDataAccess {
	protected  EntityManager  db;
	protected  EntityManagerFactory emf;

	ConfigXML  c=ConfigXML.getInstance();


	public TestDataAccess()  {
		
		System.out.println("TestDataAccess created");
		System.out.println("");
		//open();
		
	}

	       
	public void open(){ 
		
  
		String fileName=c.getDbFilename();
		
		if (c.isDatabaseLocal()) {
			  emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			  db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);

			  db = emf.createEntityManager();
    	   }
		System.out.println("TestDataAccess opened");

		
	}
	
	
	public void initializeDB() {
	    db.getTransaction().begin(); // Asegúrate de iniciar la transacción
	    Driver driver1 = new Driver("Urtzi2", "123");
	    driver1.setMoney(215); 
	    driver1.setBalorazioa(14);
	    driver1.setBalkop(3);
	    Driver driver2 = new Driver("Zuri2", "456");
	    driver2.setMoney(115); 
	    driver2.setBalorazioa(10);
	    driver2.setBalkop(3);
	    db.persist(driver1);
	    db.persist(driver2);
	    db.getTransaction().commit(); // Asegúrate de hacer commit después de persistir
	}
	

	
	public void close(){
		db.close();
		System.out.println("TestDataAccess closed");
	}

	public boolean removeDriver(String name) {
		System.out.println(">> TestDataAccess: removeDriver");
		Driver d = db.find(Driver.class, name);
		if (d!=null) {
			db.getTransaction().begin();
			db.remove(d);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
    }
	public Driver createDriver(String name, String pass) {
		System.out.println(">> TestDataAccess: addDriver");
		Driver driver=null;
			db.getTransaction().begin();
			try {
			    driver=new Driver(name,pass);
				db.persist(driver);
				db.getTransaction().commit();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return driver;
    }
	public boolean existDriver(String email) {
		 return  db.find(Driver.class, email)!=null;
		 

	}
		
		public Driver addDriverWithRide(String name, String from, String to,  Date date, int nPlaces, float price) {
			System.out.println(">> TestDataAccess: addDriverWithRide");
				Driver driver=null;
				db.getTransaction().begin();
				try {
					 driver = db.find(Driver.class, name);
					if (driver==null) {
						System.out.println("Entra en null");
						driver=new Driver(name,null);
				    	db.persist(driver);
					}
				    driver.addRide(from, to, date, nPlaces, price);
					db.getTransaction().commit();
					System.out.println("Driver created "+driver);
					
					return driver;
					
				}
				catch (Exception e){
					e.printStackTrace();
				}
				return null;
	    }
		
		
		public boolean existRide(String name, String from, String to, Date date) {
			System.out.println(">> TestDataAccess: existRide");
			Driver d = db.find(Driver.class, name);
			if (d!=null) {
				return d.doesRideExists(from, to, date);
			} else 
			return false;
		}
		public Ride removeRide(String name, String from, String to, Date date ) {
			System.out.println(">> TestDataAccess: removeRide");
			Driver d = db.find(Driver.class, name);
			if (d!=null) {
				db.getTransaction().begin();
				Ride r= d.removeRide(from, to, date);
				db.getTransaction().commit();
				System.out.println("created rides" +d.getCreatedRides());
				return r;

			} else 
			return null;

		}
		
		public User getUser(String erab) {
			try {
				TypedQuery<User> query = db.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
				query.setParameter("username", erab);
				return query.getSingleResult();
			}catch (Exception e) {
				System.out.println(e);
			}
			return null;
		}

		
		public boolean gauzatuEragiketa(String username, double amount, boolean deposit) {
			try {
				db.getTransaction().begin();
				User user = getUser(username);
				System.out.println(user);
				if (user != null) {
					double currentMoney = user.getMoney();
					if (deposit) {
						user.setMoney(currentMoney + amount);
					} else {
						if ((currentMoney - amount) < 0)
							user.setMoney(0);
						else
							user.setMoney(currentMoney - amount);
					}
					db.merge(user);
					db.getTransaction().commit();
					return true;
				}
				db.getTransaction().commit();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				db.getTransaction().rollback();
				return false;
			}	
		}
		
		public void removeAllDrivers() {
		    db.getTransaction().begin();
		    try {
		        db.createQuery("DELETE FROM Driver").executeUpdate(); // Elimina todos los drivers
		        db.getTransaction().commit();
		    } catch (Exception e) {
		        db.getTransaction().rollback();
		        e.printStackTrace();
		    }
		}


		
}