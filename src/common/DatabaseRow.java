package common;

import java.io.Serializable;
import java.util.ArrayList;

public class DatabaseRow implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7139676942093213060L;
	private ArrayList<String> columns;

	public DatabaseRow(ArrayList<String> columns) {
		super();
		this.columns = columns;
	}

	public ArrayList<String> getColumns() {
		return columns;
	}
}
