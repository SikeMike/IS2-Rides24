package testOperations;

import businessLogic.BLFacade;
import businessLogic.BLFactory;
import businessLogic.ExtendedIterator;

public class IteratorTest {
	
	public static void main(String[] args) {
	    BLFacade blFacade = BLFactory.getBusinessLogicFactory();
	    ExtendedIterator<String> i = blFacade.getDepartingCitiesIterator();
	    String c;
	    
	    System.out.println("_____________________");
	    System.out.println("FROM LAST TO FIRST");
	    i.goLast();
	    while (i.hasPrevious()) {
	        c = i.previous();
	        System.out.println(c);
	    }
	    
	    System.out.println();
	    System.out.println("_____________________");
	    System.out.println("FROM FIRST TO LAST");
	    i.goFirst(); 
	    while (i.hasNext()) {
	        c = i.next();
	        System.out.println(c);
	    }
	}




}
