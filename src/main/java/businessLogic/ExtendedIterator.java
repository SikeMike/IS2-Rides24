package businessLogic;

import java.util.Iterator;

public interface ExtendedIterator<T> extends Iterator<T> {
	
	T previous();

	boolean hasPrevious();

	void goFirst();

	void goLast();
}
