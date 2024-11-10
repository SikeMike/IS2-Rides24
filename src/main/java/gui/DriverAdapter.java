package gui;

import javax.swing.table.AbstractTableModel;
import java.util.List;

import domain.Driver;
import domain.Ride;

public class DriverAdapter extends AbstractTableModel {
	private static final String[] COLUMN_NAMES = { "from", "to", "date", "places", "price" };
	private List<Ride> rides;

	public DriverAdapter(Driver driver) {
		this.rides = driver.getCreatedRides();
	}

	@Override
	public int getRowCount() {
		return rides.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Ride ride = rides.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return ride.getFrom();
		case 1:
			return ride.getTo();
		case 2:
			return ride.getDate();
		case 3:
			return ride.getnPlaces();
		case 4:
			return ride.getPrice();
		default:
			return null;
		}
	}
}
