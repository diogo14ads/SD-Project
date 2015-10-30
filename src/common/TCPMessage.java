package common;

import java.io.Serializable;
import java.util.ArrayList;

public class TCPMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1828279559078599563L;
	private TCPMessageType type;
	private ArrayList<String> strings = null;
	private ArrayList<Integer> integers = null;
	private ArrayList<DatabaseRow> table = null;
	
	public TCPMessage(ArrayList<DatabaseRow> table) {
		super();
		this.table = table;
	}

	public TCPMessage(TCPMessageType type, ArrayList<String> strings) {
		super();
		this.type = type;
		this.strings = strings;
	}

	public TCPMessage(TCPMessageType type, ArrayList<String> strings, ArrayList<Integer> integers) {
		super();
		this.type = type;
		this.strings = strings;
		this.integers = integers;
	}

	public TCPMessage(TCPMessageType type) {
		super();
		this.type = type;
		this.strings = new ArrayList<String>();
		this.integers = new ArrayList<Integer>();
	}

	public ArrayList<String> getStrings() {
		return strings;
	}

	public ArrayList<Integer> getIntegers() {
		return integers;
	}

	public TCPMessageType getType() {
		return type;
	}
	
	public ArrayList<DatabaseRow> getTable() {
		return table;
	}

	public void setTable(ArrayList<DatabaseRow> table) {
		this.table = table;
	}

	public void setType(TCPMessageType type) {
		this.type = type;
	}
	
	//para a persistencia dos dados
	//private int id;
	
}
