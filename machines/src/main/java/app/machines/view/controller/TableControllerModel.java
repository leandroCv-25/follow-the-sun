package app.machines.view.controller;

import java.util.ArrayList;
import java.util.List;

import app.machines.model.Controller;
import app.machines.view.GenericTableModel;

public class TableControllerModel extends GenericTableModel<Controller>{

		/**
	 * 
	 */
	private static final long serialVersionUID = 3149752764445043745L;


		private final String columns[] = {"Id","Nome","P","I","D"};
		
		private final Integer sizeField[] = {};
		
		
		private static final int Id = 0;
		private static final int NOME = 1;
		private static final int P = 2;
		private static final int I = 3;
		private static final int D = 4;
		
		private List<Controller> table;
		
		public TableControllerModel() {
			table = new ArrayList<Controller>();
			this.column = columns;
		}
		
		public TableControllerModel(List<Controller> table) {
			super(table);
			this.table = table;
			this.column = columns;
			
		}
		
		
		public Controller getController(int index) {
			return getTable().get(index); 
		}
		
		public void salvarController(Controller controller) {
			getTable().add(controller);
			fireTableRowsInserted(getRowCount()-1, getColumnCount()-1);
		}
		
		public void alterarController(Controller controller, int index) {
			getTable().set(index, controller);
			fireTableRowsUpdated(index, index);
		}
		
		
		public void excluirController(int index) {
			getTable().remove(index);
			fireTableRowsDeleted(index, index);
		}
		
		
		

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Controller controller = table.get(rowIndex);
			switch(columnIndex) {
			case Id:
				return  controller.getId();
			case NOME:
				return controller.getName();
			case P:
				return controller.getP();
			case I:
				return controller.getI();
			case D:
				return controller.getD();
			default:
				return controller;
			}
		}
		
		public Class<?> getColumnClass(int columnIndex){
			switch(columnIndex) {
			case Id:
				return   Integer.class;
			case NOME:
				return String.class;
			case P:
				return double.class;
			case I:
				return double.class;
			case D:
				return double.class;
			default:
				return null;
			}
			
		}
		
		public List<Controller> getTable() {
			return table;
		}


		public void setTabela(List<Controller> table) {
			this.table = table;
		}
		

		public String[] getColumns() {
			return columns;
		}
		
		public Integer[] getSizeField() {
			return sizeField;
		}
}
	
	
	

