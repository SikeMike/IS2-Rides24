package gauzatuEragiketaTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class GauzatuEragiketaMockW {

	static DataAccess sut;

	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected EntityManagerFactory entityManagerFactory;
	@Mock
	protected EntityManager db;
	@Mock
	protected EntityTransaction et;
	@Mock
	TypedQuery<Double> typedQueryDouble;
	@Mock
	TypedQuery<User> typedQueryUser;

	@SuppressWarnings("unused")

	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
		persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
				.thenReturn(entityManagerFactory);

		Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
		sut = new DataAccess(db);
	}

	@After
	public void tearDown() {
		persistenceMock.close();
	}
 
	User user;

	@Test
	public void test1() {

		try {

			when(db.find(User.class, "username")).thenReturn(null);
			Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryDouble);
			Mockito.when(typedQueryDouble.getSingleResult()).thenReturn(null);

			boolean result = sut.gauzatuEragiketa("username", 100.0, true);

			// Verificar
			assertFalse(result);

			verify(et).begin();
			verify(et).commit();
			verify(et, never()).rollback();

		} catch (NullPointerException e) {
			System.out.println("Error: NullPointerException.");
			e.printStackTrace();
			fail("NullPointerException fue lanzada");
		}
	}

	@Test
	public void test2() {

		try {

			Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryDouble);
			Mockito.when(typedQueryDouble.getSingleResult()).thenReturn(null);

			boolean result = sut.gauzatuEragiketa(null, 100.0, true);

			// Verificar
			assertFalse(result);

			verify(et).begin();
			verify(et).commit();
			verify(et, never()).rollback();

		} catch (NullPointerException e) {
			System.out.println("Error: NullPointerException.");
			e.printStackTrace();
			fail("NullPointerException fue lanzada");
		}

	}

	@Test
	public void test3() {

		User user = new User("Juan", "1234a", "a");

		try {
			when(db.find(User.class, "Juan")).thenReturn(user);
			Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryUser);
			Mockito.when(typedQueryUser.getSingleResult()).thenReturn(user);

			boolean result = sut.gauzatuEragiketa("Juan", 100.0, true);

			// Verificar
			assertTrue(result);

			verify(et).begin();
			verify(et).commit();
			verify(et, never()).rollback();

		} catch (NullPointerException e) {
			System.out.println("Error: NullPointerException.");
			e.printStackTrace();
			fail("NullPointerException fue lanzada");
		}

	}

	@Test
	public void testDepositUserFound() {
	    try {
	        // Crear un objeto User real 
	        User user = new User("Juan", "1234a", "a");
	        user.setMoney(100.0); // Establecer saldo inicial

	        // Mockear el TypedQuery<User>
	        TypedQuery<User> typedQueryUser = Mockito.mock(TypedQuery.class);

	        // Configurar el comportamiento de la base de datos para crear la consulta
	        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryUser);

	        // Configurar el comportamiento del query para retornar el usuario simulado
	        Mockito.when(typedQueryUser.getSingleResult()).thenReturn(user);

	        // Simular que el parámetro de la query también se establece correctamente
	        Mockito.when(typedQueryUser.setParameter(Mockito.anyString(), Mockito.any())).thenReturn(typedQueryUser);

	        // Llamar a la operación
	        boolean result = sut.gauzatuEragiketa("username", 50.0, true);

	        // Verificar resultado esperado
	        assertTrue(result);  // Asegurar que la operación retorna true
	        assertEquals(150.0, user.getMoney(), 0.01);  // Verificar que el dinero se sumó correctamente

	        // Verificar interacciones
	        verify(db).merge(user);
	        verify(et).begin();
	        verify(et).commit();
	        verify(et, never()).rollback();
	    } catch (NullPointerException e) {
	        fail("NullPointerException fue lanzada.");
	    }
	}
	
	@Test
	public void testDepositUserSufficientFunds() {
	    try {
	        // Crear un objeto User real
	        User user = new User("Juan", "1234a", "a");
	        user.setMoney(100.0); // Establecer saldo inicial

	        // Mockear el TypedQuery<User>
	        TypedQuery<User> typedQueryUser = Mockito.mock(TypedQuery.class);
	        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryUser);
	        Mockito.when(typedQueryUser.getSingleResult()).thenReturn(user);
	        Mockito.when(typedQueryUser.setParameter(Mockito.anyString(), Mockito.any())).thenReturn(typedQueryUser);

	        // Llamar a la operación con un monto que no exceda el saldo
	        boolean result = sut.gauzatuEragiketa("username", 50.0, true);

	        // Verificar que el resultado sea true
	        assertTrue(result);
	        assertEquals(150.0, user.getMoney(), 0.01); // Verificar que el dinero se aumentó correctamente

	        // Verificar interacciones
	        verify(db).merge(user);
	        verify(et).begin();
	        verify(et).commit();
	        verify(et, never()).rollback();
	    } catch (NullPointerException e) {
	        fail("NullPointerException fue lanzada.");
	    }
	}
	
	@Test
	public void testWithdrawUserInsufficientFunds() {
	    User user = new User("Juan", "1234a", "a");
	    user.setMoney(30.0); // Establecer saldo inicial

	    // Mockear el TypedQuery<User>
	    TypedQuery<User> typedQueryUser = Mockito.mock(TypedQuery.class);
	    Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryUser);
	    Mockito.when(typedQueryUser.getSingleResult()).thenReturn(user);
	    
	    boolean result = sut.gauzatuEragiketa("Juan", 50.0, false); // Retirar 50

	    assertTrue(result);
	    assertEquals(0.0, user.getMoney(), 0.01); // Verificar que el dinero se ha ajustado a 0
	    verify(db).merge(user); // Verificar que se haya guardado el usuario
	}
	
	@Test
	public void testDepositUserSufficientFunds1() {
	    User user = new User("Juan", "1234a", "a");
	    user.setMoney(100.0); // Establecer saldo inicial

	    // Mockear el TypedQuery<User>
	    TypedQuery<User> typedQueryUser = Mockito.mock(TypedQuery.class);
	    Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryUser);
	    Mockito.when(typedQueryUser.getSingleResult()).thenReturn(user);
	    
	    boolean result = sut.gauzatuEragiketa("Juan", 50.0, true); // Depositar 50

	    assertTrue(result);
	    assertEquals(150.0, user.getMoney(), 0.01); // Verificar que el dinero se aumentó correctamente
	    verify(db).merge(user); // Verificar que se haya guardado el usuario
	}
	
	@Test
	public void testWithdrawUserExactFunds() {
	    User user = new User("Juan", "1234a", "a");
	    user.setMoney(50.0); // Establecer saldo inicial

	    // Mockear el TypedQuery<User>
	    TypedQuery<User> typedQueryUser = Mockito.mock(TypedQuery.class);
	    Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryUser);
	    Mockito.when(typedQueryUser.getSingleResult()).thenReturn(user);
	    
	    boolean result = sut.gauzatuEragiketa("Juan", 50.0, false); // Retirar 50

	    assertTrue(result);
	    assertEquals(0.0, user.getMoney(), 0.01); // Verificar que el dinero se ha ajustado a 0
	    verify(db).merge(user); // Verificar que se haya guardado el usuario
	}


	@Test
	public void testGauzatuEragiketaException() {
	    User user = new User("Juan", "1234a", "a");
	    
	    // Mockear el TypedQuery<User>
	    TypedQuery<User> typedQueryUser = Mockito.mock(TypedQuery.class);
	    Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryUser);
	    Mockito.when(typedQueryUser.getSingleResult()).thenReturn(user);
	    
	    // Simular una excepción al llamar a db.merge(user)
	    doThrow(new RuntimeException("Database error")).when(db).merge(user);

	    boolean result = sut.gauzatuEragiketa("Juan", 50.0, true); // Intentar depositar

	    // Verificar que el resultado sea false
	    assertFalse(result);
	    
	    // Verificar que se haya hecho rollback
	    verify(db.getTransaction()).rollback(); // Asegurarse de que se llama a rollback
	}


	
	




	





	

}
