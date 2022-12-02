package app.machines.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class GenericTableModel<T> extends AbstractTableModel {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 3082252407798208337L;
	protected List<T> table;
	protected String column[];
	
	
	public GenericTableModel() {
		table = new ArrayList<T>();
	}
	
	
	public GenericTableModel(List<T> table) {
		this.table = table;
	}
	
		
	@Override
	public int getRowCount() {
		return table.size();
	}

	@Override
	public int getColumnCount() {
		return column.length;
	}
	
	
	@Override
	public String getColumnName(int col) {
       if ( col < getColumnCount()) {
    	   return column[col];
       }
       return super.getColumnName(col);
	}
	
	
	
	public T getValueAt(int row) {
		return table.get(row);
	}
	
	public void setValueAt(int row, T objeto) {
		table.set(row, objeto);
	}


	public List<T> getTable() {
		return table;
	}


	public void setTable(List<T> table) {
		this.table = table;
	}


	public String[] getColumn() {
		return column;
	}


	public void setColumn(String[] column) {
		this.column = column;
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return null;
	}
	
	
}
