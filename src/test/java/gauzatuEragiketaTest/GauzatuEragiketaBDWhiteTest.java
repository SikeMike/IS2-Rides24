package gauzatuEragiketaTest;
 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Ride;
import domain.Traveler;
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import testOperations.TestDataAccess;
import domain.Driver;

public class GauzatuEragiketaBDWhiteTest {

	// sut:system under test
	static TestDataAccess testDA = new TestDataAccess();
	//a
	@Before
	public void setUp() {
		// Abre la conexión a la base de datos
		testDA.open();
		// Limpia la base de datos antes de inicializarla
		testDA.removeAllDrivers(); // Método nuevo que debes implementar
		testDA.initializeDB(); // Llama a este método para inicializar los datos de prueba
	}

	@After
	public void tearDown() {
		testDA.close();
	}

	@Test
	// Debería devolver verdadero, porque "Urtzi2" existe
	public void test1() {
		boolean a = false;
		try {
			// define parameters
			String driverUsername = "Urtzi2"; // Un driver que existe

			// invoke System Under Test (sut)
			a = testDA.gauzatuEragiketa(driverUsername, 150, true);

			// imprimir el resultado y el estado del usuario
			System.out.println("Resultado de gauzatuEragiketa: " + a);
			User user = testDA.getUser(driverUsername);
			System.out.println("Dinero de " + driverUsername + ": " + user.getMoney()); // Ver dinero

			assertTrue(a); // Debería ser verdadero
		} catch (Exception e) {
			fail();
		}
		System.out.println("Test 1 fin");
	}

	@Test
	public void test2() {
		try {
			boolean result = testDA.gauzatuEragiketa("Urtzi2", 50, false); // Retirar 50
			assertTrue(result);
			User user = testDA.getUser("Urtzi2");
			assertEquals(160, user.getMoney(), 0); 
		} catch (Exception e) {
			fail(); // Si hay una excepción, la prueba falla
		}
		System.out.println("Test 2 fin");
	}

	@Test
	public void test3() {
		try {
			boolean result = testDA.gauzatuEragiketa("Urtzi2", 20, false); // Retirar 20
			assertTrue(result);
			User user = testDA.getUser("Urtzi2");
			assertEquals(195, user.getMoney(), 0); // Deberia dar 195
		} catch (Exception e) {
			fail();
		}
		System.out.println("Test 3 fin");
	}

	@Test
	public void test4() {
		try {
			boolean result = testDA.gauzatuEragiketa("NoExistente", 100, true); // Usuario no existe
			assertFalse(result); // Debe devolver false
		} catch (Exception e) {
			fail();
		}
		System.out.println("Test 4 fin");
	}

	@Test
	public void test5() {
		boolean result = testDA.gauzatuEragiketa("Urtzi2", 50, true); // Depositar 50
		assertTrue(result);
		User user = testDA.getUser("Urtzi2");
		assertEquals(265, user.getMoney(), 0); // Verifica que el saldo sea 65
		System.out.println("Test 5 fin");
	}
	
	@Test
	public void test6() {
		boolean result = testDA.gauzatuEragiketa("Urtzi2", -50, false); // Depositar -50
		assertTrue(result);
		User user = testDA.getUser("Urtzi2");
		System.out.println("El saldo no deberia dar 265 si no 165"+user.getMoney());
		assertEquals(165, user.getMoney(), 0); // Verifica que el saldo sea 165 y no sea 265
		System.out.println("Test 6 fin");
	
	}
}
