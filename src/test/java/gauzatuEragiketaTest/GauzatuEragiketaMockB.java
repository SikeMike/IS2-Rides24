package gauzatuEragiketaTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



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

import domain.User;



public class GauzatuEragiketaMockB {

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
	
	public void test2() {
	    User user = new User("Juan", "1234a", "a");
	    user.setMoney(100.0);  // El usuario tiene 100 de saldo inicialmente

	    TypedQuery<User> mockTypedQueryUser = Mockito.mock(TypedQuery.class);
	    Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(mockTypedQueryUser);
	    Mockito.when(mockTypedQueryUser.getSingleResult()).thenReturn(user);

	    // Intentar depositar un valor negativo, lo cual debería ser inválido
	    boolean result = sut.gauzatuEragiketa("Juan", -50.0, true);  // Depósito con -50

	    // Verificar que el depósito no se realiza y que el saldo no cambia
	    assertTrue(result);
	    assertEquals(100.0, user.getMoney(), 0.01);  // El saldo debería seguir siendo 100
	    verify(db, never()).merge(user);  
	    verify(et).rollback();  
	}

	
	@Test
	public void test3() {
	    User user = new User("Juan", "1234a", "a");
	    user.setMoney(100.0);  // El usuario tiene 100 de saldo inicialmente

	    TypedQuery<User> mockTypedQueryUser = Mockito.mock(TypedQuery.class);
	    Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(mockTypedQueryUser);
	    Mockito.when(mockTypedQueryUser.getSingleResult()).thenReturn(user);

	    // Intentar retirar un valor negativo, lo cual debería ser inválido
	    boolean result = sut.gauzatuEragiketa("Juan", -50.0, false);  // Retiro con -50

	    // Verificar que el retiro no se realiza y que el saldo no cambia
	    assertTrue(result);
	    assertEquals(100.0, user.getMoney(), 0.01);  
	    verify(db, never()).merge(user); 
	    verify(et).rollback();  
	}


   
}

