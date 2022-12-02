package app.machines.view.data;

import java.util.ArrayList;
import java.util.List;

import app.machines.model.Data;
import app.machines.view.GenericTableModel;

public class TableDataModel extends GenericTableModel<Data>{

		/**
	 * 
	 */
	private static final long serialVersionUID = 3149752764445043745L;


		private final String columns[] = {"Id","DATE","ANGLE","ERROR","VOLTAGEGENERATED"};
		
		private final Integer sizeField[] = {};
		
		
		private static final int Id = 0;
		private static final int DATE = 1;
		private static final int ANGLE = 2;
		private static final int ERROR = 3;
		private static final int VOLTAGEGENERATED = 4;
		
		private List<Data> table;
		
		public TableDataModel() {
			table = new ArrayList<Data>();
			this.column = columns;
		}
		
		public TableDataModel(List<Data> table) {
			super(table);
			this.table = table;
			this.column = columns;
			
		}
		
		
		public Data getData(int index) {
			return getTable().get(index); 
		}
		
		public void salvarData(Data controller) {
			getTable().add(controller);
			fireTableRowsInserted(getRowCount()-1, getColumnCount()-1);
		}
		
		public void alterarData(Data controller, int index) {
			getTable().set(index, controller);
			fireTableRowsUpdated(index, index);
		}
		
		
		public void excluirData(int index) {
			getTable().remove(index);
			fireTableRowsDeleted(index, index);
		}
		
		
		

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Data controller = table.get(rowIndex);
			switch(columnIndex) {
			case Id:
				return  controller.getId();
			case DATE:
				return controller.getDate();
			case ANGLE:
				return controller.getAngle();
			case ERROR:
				return controller.getError();
			case VOLTAGEGENERATED:
				return controller.getVoltageGenerated();
			default:
				return controller;
			}
		}
		
		public Class<?> getColumnClass(int columnIndex){
			switch(columnIndex) {
			case Id:
				return   Integer.class;
			case DATE:
				return String.class;
			case ANGLE:
				return double.class;
			case ERROR:
				return double.class;
			case VOLTAGEGENERATED:
				return double.class;
			default:
				return null;
			}
			
		}
		
		public List<Data> getTable() {
			return table;
		}


		public void setTabela(List<Data> table) {
			this.table = table;
		}
		

		public String[] getColumns() {
			return columns;
		}
		
		public Integer[] getSizeField() {
			return sizeField;
		}
}
	
	
	

