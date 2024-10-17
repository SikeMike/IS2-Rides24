

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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

			User user = new User("Juan", "1234a", "a");
			user.setMoney(100.0);

			TypedQuery<User> typedQueryUser = Mockito.mock(TypedQuery.class);

			Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryUser);

			Mockito.when(typedQueryUser.getSingleResult()).thenReturn(user);

			Mockito.when(typedQueryUser.setParameter(Mockito.anyString(), Mockito.any())).thenReturn(typedQueryUser);

			boolean result = sut.gauzatuEragiketa("username", 50.0, true);

			assertTrue(result);
			assertEquals(150.0, user.getMoney(), 0.01);

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

			User user = new User("Juan", "1234a", "a");
			user.setMoney(100.0);

			TypedQuery<User> typedQueryUser = Mockito.mock(TypedQuery.class);
			Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryUser);
			Mockito.when(typedQueryUser.getSingleResult()).thenReturn(user);
			Mockito.when(typedQueryUser.setParameter(Mockito.anyString(), Mockito.any())).thenReturn(typedQueryUser);

			boolean result = sut.gauzatuEragiketa("username", 50.0, true);

			assertTrue(result);
			assertEquals(150.0, user.getMoney(), 0.01);

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
		user.setMoney(30.0);

		TypedQuery<User> typedQueryUser = Mockito.mock(TypedQuery.class);
		Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryUser);
		Mockito.when(typedQueryUser.getSingleResult()).thenReturn(user);

		boolean result = sut.gauzatuEragiketa("Juan", 50.0, false);

		assertTrue(result);
		assertEquals(0.0, user.getMoney(), 0.01);
		verify(db).merge(user);
	}

	@Test
	public void testDepositUserSufficientFunds1() {
		User user = new User("Juan", "1234a", "a");
		user.setMoney(100.0);

		TypedQuery<User> typedQueryUser = Mockito.mock(TypedQuery.class);
		Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryUser);
		Mockito.when(typedQueryUser.getSingleResult()).thenReturn(user);

		boolean result = sut.gauzatuEragiketa("Juan", 50.0, true);

		assertTrue(result);
		assertEquals(150.0, user.getMoney(), 0.01);
		verify(db).merge(user);
	}

	@Test
	public void testWithdrawUserExactFunds() {
		User user = new User("Juan", "1234a", "a");
		user.setMoney(50.0);

		TypedQuery<User> typedQueryUser = Mockito.mock(TypedQuery.class);
		Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryUser);
		Mockito.when(typedQueryUser.getSingleResult()).thenReturn(user);

		boolean result = sut.gauzatuEragiketa("Juan", 50.0, false);

		assertTrue(result);
		assertEquals(0.0, user.getMoney(), 0.01);
		verify(db).merge(user);
	}

	@Test
	public void testGauzatuEragiketaException() {
		// Capturar la salida estándar de errores
		PrintStream originalErr = System.err;
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		System.setErr(new PrintStream(errContent));

		when(db.getTransaction()).thenReturn(et);
		when(db.find(User.class, "username")).thenThrow(new RuntimeException("Database error"));

		boolean result = sut.gauzatuEragiketa("username", 50.0, true);

		assertFalse(result);

		verify(et).rollback();

		System.setErr(originalErr);

	}

	@Test
	public void testGauzatuEragiketa0amount() {
		User user = new User("Juan", "1234a", "a");
		user.setMoney(100.0);

		when(db.getTransaction()).thenReturn(et);
		Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryUser);
		Mockito.when(typedQueryUser.getSingleResult()).thenReturn(user);

		boolean result = sut.gauzatuEragiketa("Juan", 0.0, true);

		// Verificar que no se modifica el dinero del usuario
		assertTrue(result);
		assertEquals(100.0, user.getMoney(), 0.01);
		verify(db).merge(user);
		verify(et).commit();
	}

}
