package model;

public class DBTable {

	/**
	 * @param args
	 */
	String columnName="";
	String columnDataType="";
	
	public DBTable(String columnName, String columnDataType) {
		super();
		this.columnName = columnName;
		this.columnDataType =columnDataType;
	}
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnDataType() {
		return columnDataType;
	}

	public void setColumnDataType(String columnDataType) {
		this.columnDataType = columnDataType;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}
