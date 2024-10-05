package gauzatuEragiketaTest;

import static org.junit.Assert.*;
import org.junit.*;
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

import domain.User;

public class GauzatuEragiketaBDBlackTest {

    static TestDataAccess testDA;

    @Before
    public void setUp() {
        testDA = new TestDataAccess();
        testDA.open();
        testDA.removeAllDrivers();
        testDA.initializeDB();
    }

    @After
    public void tearDown() {
        testDA.close();
    }

    @Test
    public void tes1() {
        boolean result = testDA.gauzatuEragiketa("Zuri2", 150.0, true);
        assertTrue(result); // Debería ser verdadero
        User user = testDA.getUser("Zuri2");
        assertEquals(265.0, user.getMoney(), 0.01); // Verifica que el saldo sea 265
    }

    @Test
    public void tes2() {
        boolean result = testDA.gauzatuEragiketa("Zuri2", 50.0, false);
        assertTrue(result); // Debería ser verdadero
        User user = testDA.getUser("Zuri2");
        assertEquals(65.0, user.getMoney(), 0.01); // Verifica que el saldo sea 65
    }

    @Test
    public void tes3() {
        boolean result = testDA.gauzatuEragiketa("NoExistente", 100.0, true);
        assertFalse(result); // Debería ser falso
    }

    @Test
    public void tes4() {
        boolean result = testDA.gauzatuEragiketa("Urtzi2", -50.0, true);
        assertTrue(result);
    }

    @Test
    public void tes5() {
        boolean result = testDA.gauzatuEragiketa("Urtzi2", 0.0, false);
        assertTrue(result); // Debería ser verdadero
        User user = testDA.getUser("Urtzi2");
        assertEquals(215.0, user.getMoney(), 0.01); // El saldo no cambia
    }

    @Test
    public void tes6() {
        boolean result = testDA.gauzatuEragiketa("Urtzi2", 300.0, false);
        assertTrue(result); // El saldo no debe ser negativo
        User user = testDA.getUser("Urtzi2");
        assertEquals(0.0, user.getMoney(), 0.01); // Verifica que el saldo sea 0
    }
    
    @Test
	public void test7() {
		boolean result = testDA.gauzatuEragiketa("Urtzi2", -50, true); // Depositar -50
		assertTrue(result);
		User user = testDA.getUser("Urtzi2");
		System.out.println(user.getMoney());
		assertEquals(265, user.getMoney(), 0); // Verifica que el saldo sea 65
		System.out.println("Test 7 fin");
	
	}
	
	@Test
	public void test8() {
		boolean result = testDA.gauzatuEragiketa("Urtzi2", -50, false); // Depositar -50
		assertTrue(result);
		User user = testDA.getUser("Urtzi2");
		System.out.println(user.getMoney());
		assertEquals(165, user.getMoney(), 0); // Verifica que el saldo sea 
		System.out.println("Test 8 fin");
	
	}
}
