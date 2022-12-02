package app.machines.view.tunning;

import java.util.ArrayList;
import java.util.List;

import app.machines.model.Tunning;
import app.machines.view.GenericTableModel;

public class TableTunningModel extends GenericTableModel<Tunning>{

		/**
	 * 
	 */
	private static final long serialVersionUID = 3149752764445043745L;


		private final String columns[] = {"Id","Inicio","fim","Nome","P","I","D"};
		
		private final Integer sizeField[] = {};
		
		
		private static final int Id = 0;
		private static final int INICIO = 1;
		private static final int FIM = 2;
		private static final int NOME = 3;
		private static final int P = 4;
		private static final int I = 5;
		private static final int D = 6;
		
		private List<Tunning> table;
		
		public TableTunningModel() {
			table = new ArrayList<Tunning>();
			this.column = columns;
		}
		
		public TableTunningModel(List<Tunning> table) {
			super(table);
			this.table = table;
			this.column = columns;
			
		}
		
		
		public Tunning getTunning(int index) {
			return getTable().get(index); 
		}
		
		public void salvarTunning(Tunning tunning) {
			getTable().add(tunning);
			fireTableRowsInserted(getRowCount()-1, getColumnCount()-1);
		}
		
		public void alterarTunning(Tunning tunning, int index) {
			getTable().set(index, tunning);
			fireTableRowsUpdated(index, index);
		}
		
		
		public void excluirTunning(int index) {
			getTable().remove(index);
			fireTableRowsDeleted(index, index);
		}
		
		
		

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Tunning tunning = table.get(rowIndex);
			switch(columnIndex) {
			case Id:
				return  tunning.getId();
			case INICIO:
				return  tunning.getDataStart();
			case FIM:
				return tunning.getDataEnd();
			case NOME:
				return tunning.getController().getName();
			case P:
				return tunning.getController().getP();
			case I:
				return tunning.getController().getI();
			case D:
				return tunning.getController().getD();
			default:
				return tunning;
			}
		}
		
		public Class<?> getColumnClass(int columnIndex){
			switch(columnIndex) {
			case Id:
				return   Integer.class;
			case INICIO:
				return  String.class;
			case FIM:
				return String.class;
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
		
		public List<Tunning> getTable() {
			return table;
		}


		public void setTabela(List<Tunning> table) {
			this.table = table;
		}
		

		public String[] getColumns() {
			return columns;
		}
		
		public Integer[] getSizeField() {
			return sizeField;
		}
}
	
	
	

