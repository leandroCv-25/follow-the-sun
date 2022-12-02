package app.machines.view.data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;

import app.machines.config.Page;
import app.machines.model.Data;
import app.machines.model.Machine;
import app.machines.service.DataService;
import app.machines.view.RenderHeaderTable;
import app.machines.view.RenderTable;
import app.machines.view.decoration.Decoration;

public class TableData extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8811519273070611617L;
	private JPanel contentPane;
	private JTable  tabelaData;
	private JButton btnPrimeiro;
	private JButton btnAnterior;
	private JButton btnProximo;
	private JButton btnUltimo;
	private JButton btnClose;
	private JButton btnReport;
	
	private TableDataModel model;
	private Page<Data> page;
	private Machine machine;
	
	private static TableData TABLE_DATA;
	
	private int row = 0; 
	private int column = 0;
	private int tamanhoPagina = 10;
	private int paginaAtual = 0;

	/**
	 * Create the frame.
	 */
	private TableData(Machine machine) {
		this.machine = machine;
		initComponents();
		eventHandlers();
		iniciarTable();
	}
	
	public static TableData getInstancia(Machine machine) {
		if (Objects.isNull(TABLE_DATA)) {
			TABLE_DATA = new TableData(machine);
		}
		TABLE_DATA.setMachine(machine);
		return TABLE_DATA;
	}
	
private void eventHandlers() {
		
		btnPrimeiro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paginaAtual = 1;
				iniciarTable();
			}
		});
		btnAnterior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (paginaAtual > 1) {
					paginaAtual = paginaAtual - 1;
					iniciarTable();
				}
				
			}
		});
		btnProximo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (paginaAtual < page.getTotalPage() ) {
					paginaAtual = paginaAtual + 1;
					iniciarTable();
				}
				
			}
		});
		btnUltimo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paginaAtual = page.getTotalPage();
				iniciarTable();
			}
		});
	
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TABLE_DATA.dispose();
			}
		});
		
		btnReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DataReportFill(machine);
			}
		});
	}
	
	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 519, 570);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelTable = new JPanel();
		panelTable.setBounds(0, 78, 789, 373);
		contentPane.add(panelTable);
		panelTable.setLayout(null);
		
		JScrollPane scrollPaneTable = new JScrollPane();
		scrollPaneTable.setBounds(0, 0, 789, 305);
		panelTable.add(scrollPaneTable);
		
		tabelaData = new JTable();
		scrollPaneTable.setViewportView(tabelaData);
		
		btnPrimeiro = new JButton("Primeiro");
	
		btnPrimeiro.setFont(Decoration.fontTextBold);
		btnPrimeiro.setBounds(10, 316, 135, 36);
		panelTable.add(btnPrimeiro);
		
		btnAnterior = new JButton("Anterior");
	
		btnAnterior.setFont(Decoration.fontTextBold);
		btnAnterior.setBounds(155, 316, 105, 36);
		panelTable.add(btnAnterior);
		
		btnProximo = new JButton("Próximo");
	
		btnProximo.setFont(Decoration.fontTextBold);
		btnProximo.setBounds(270, 316, 105, 36);
		panelTable.add(btnProximo);
		
		btnUltimo = new JButton("Último");
	
		btnUltimo.setFont(Decoration.fontTextBold);
		btnUltimo.setBounds(385, 316, 105, 36);
		panelTable.add(btnUltimo);
		
		JPanel panelPesquisa = new JPanel();
		panelPesquisa.setBounds(0, 0, 789, 75);
		contentPane.add(panelPesquisa);
		panelPesquisa.setLayout(null);
		
		btnClose = new JButton("Fechar");
		btnClose.setBounds(383, 489, 110, 31);
		btnClose.setFont(Decoration.fontButtonBold);
		contentPane.add(btnClose);
		
		btnReport = new JButton("Relatório");
		btnReport.setBounds(250, 489, 123, 31);
		btnReport.setFont(Decoration.fontButtonBold);
		contentPane.add(btnReport);
		
	}

public void iniciarTable() {
    	
    	listarData();
   	
    	model = new TableDataModel(page.getContent());
    	
    	model.fireTableDataChanged();
    	
    	tabelaData.setModel(model);
    	
    	RenderHeaderTable renderHeader = new RenderHeaderTable();
    	
    	tabelaData.getTableHeader().setDefaultRenderer(renderHeader);
    	
    	RenderTable render = new RenderTable();
    	
    	for (int column=0; column < model.getColumnCount(); column++) {
    		tabelaData.setDefaultRenderer(model.getColumnClass(column), render);
    	}
    	
    	
    	for (int col = 0; col < model.getColumnCount(); col++) {
    		TableColumn column = tabelaData.getColumnModel().getColumn(col);
    		column.setMinWidth(100);
    		column.setMaxWidth(100);
    		column.setPreferredWidth(100);
    	}
    	
    }
	
	
	
	private void listarData() {
		
		page = getDataService().findByMachine(paginaAtual, tamanhoPagina, machine);
		
    	if ( paginaAtual == 1  ) {
    		btnPrimeiro.setEnabled(false);
    		btnAnterior.setEnabled(false);
     	} else {
    		btnPrimeiro.setEnabled(true);
    		btnAnterior.setEnabled(true);
     	}
    
    	if (paginaAtual == page.getTotalPage()) {
    		btnProximo.setEnabled(false);
    		btnUltimo.setEnabled(false);
     	} else {
    		btnProximo.setEnabled(true);
    		btnUltimo.setEnabled(true);
     	}
    		
    
    	if (paginaAtual > page.getTotalPage()) {
    		paginaAtual = page.getTotalPage();
    	}
    	
    	paginaAtual = page.getPage();
    	setTamanhoPagina(page.getPageSize());
    	
    	
    }

	public int getRow() {
		return row;
	}


	public void setRow(int row) {
		this.row = row;
	}


	public int getColumn() {
		return column;
	}


	public void setColumn(int column) {
		this.column = column;
	}

	public int getTamanhoPagina() {
		return tamanhoPagina;
	}

	public void setTamanhoPagina(int tamanhoPagina) {
		this.tamanhoPagina = tamanhoPagina;
	}

	public DataService getDataService() {
		return new DataService();
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}
}
