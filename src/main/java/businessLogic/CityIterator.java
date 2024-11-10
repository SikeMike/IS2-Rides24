package businessLogic;

import java.util.List;
import java.util.NoSuchElementException;

public class CityIterator<T> implements ExtendedIterator<T>{

	private List<T> items;
    private int currentPosition = -1;

    public CityIterator(List<T> items) {
        this.items = items;
        if (!items.isEmpty()) {
            this.currentPosition = 0; 
        }
    }

    @Override
    public void goFirst() {
        if (!items.isEmpty()) {
            currentPosition = -1; 
        }
    }

    @Override
    public void goLast() {
        if (!items.isEmpty()) {
            currentPosition = items.size(); 
        }
    }

    @Override
    public boolean hasNext() {
        return currentPosition < items.size() - 1;
    }

    @Override
    public T next() {
        if (hasNext()) {
            currentPosition++;
            return items.get(currentPosition);
        }
        throw new NoSuchElementException();
    }

    @Override
    public boolean hasPrevious() {
        return currentPosition > 0;
    }

    @Override
    public T previous() {
        if (hasPrevious()) {
            currentPosition--;
            return items.get(currentPosition);
        }
        throw new NoSuchElementException();
    }

}
