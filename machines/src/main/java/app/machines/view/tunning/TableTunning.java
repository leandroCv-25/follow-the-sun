package app.machines.view.tunning;

import java.awt.Font;
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

import app.machines.config.Constants;
import app.machines.config.Page;
import app.machines.model.Machine;
import app.machines.model.Tunning;
import app.machines.service.TunningService;
import app.machines.view.RenderHeaderTable;
import app.machines.view.RenderTable;
import app.machines.view.controller.TableController;

public class TableTunning extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8195950197183379799L;
	private JPanel  contentPane;
	private JTable  tabelaController;
	private JButton btnPrimeiro;
	private JButton btnAnterior;
	private JButton btnProximo;
	private JButton btnUltimo;
	private JButton btnClose;
	private JButton btnConfig;
	
	private TableTunningModel model;
	private Page<Tunning> page;
	private TunningService tunningService;
	
	private Machine machine;
	private int row = 0; 
	private int column = 0;
	private int tamanhoPagina = 10;
	private int paginaAtual = 0;
	
	
	private static TableTunning TABLE_TUNNING;
	private JButton btnReport;
	
	private TableTunning(Machine machine) {
		this.machine = machine;
		initComponents();
		eventHandlers();
		iniciarTable();
	}
	
	
	public static TableTunning getInstancia(Machine machine) {
		if (Objects.isNull(TABLE_TUNNING)) {
			TABLE_TUNNING = new TableTunning(machine);
		}
		TABLE_TUNNING.setMachine(machine);
		return TABLE_TUNNING;
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
				dispose();
			}
		});
		
		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableController view =  TableController.getInstancia(Constants.INCLUIR,machine);
				view.setLocationRelativeTo(null);
				view.setVisible(true);
			}
		});
		
		btnReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new TunningReportFill(machine);
			}
		});
	
	
	}


	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 820, 570);
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
		
		tabelaController = new JTable();
		scrollPaneTable.setViewportView(tabelaController);
		
		btnPrimeiro = new JButton("Primeiro");
	
		btnPrimeiro.setFont(new Font("Verdana", Font.BOLD, 14));
		btnPrimeiro.setBounds(10, 316, 105, 36);
		panelTable.add(btnPrimeiro);
		
		btnAnterior = new JButton("Anterior");
	
		btnAnterior.setFont(new Font("Verdana", Font.BOLD, 14));
		btnAnterior.setBounds(125, 316, 105, 36);
		panelTable.add(btnAnterior);
		
		btnProximo = new JButton("Próximo");
	
		btnProximo.setFont(new Font("Verdana", Font.BOLD, 14));
		btnProximo.setBounds(240, 316, 105, 36);
		panelTable.add(btnProximo);
		
		btnUltimo = new JButton("Último");
	
		btnUltimo.setFont(new Font("Verdana", Font.BOLD, 14));
		btnUltimo.setBounds(355, 316, 105, 36);
		panelTable.add(btnUltimo);
		
		JPanel panelPesquisa = new JPanel();
		panelPesquisa.setBounds(0, 0, 789, 75);
		contentPane.add(panelPesquisa);
		panelPesquisa.setLayout(null);
		
		btnClose = new JButton("Fechar");
		btnClose.setFont(new Font("Verdana", Font.BOLD, 14));
		btnClose.setBounds(145, 478, 89, 33);
		contentPane.add(btnClose);
		
		btnConfig = new JButton("Configurar");
		btnConfig.setFont(new Font("Verdana", Font.BOLD, 14));
		btnConfig.setBounds(10, 478, 125, 33);
		btnConfig.setEnabled(false);
		btnConfig.setVisible(false);
		contentPane.add(btnConfig);
		
		btnReport = new JButton("Relatório");
		btnReport.setFont(new Font("Verdana", Font.BOLD, 14));
		btnReport.setBounds(244, 478, 147, 33);
		contentPane.add(btnReport);
		
	}


	public void iniciarTable() {
    	
    	listarTunning();
   	
    	model = new TableTunningModel(page.getContent());
    	
    	model.fireTableDataChanged();
    	
    	tabelaController.setModel(model);
    	
    	RenderHeaderTable renderHeader = new RenderHeaderTable();
    	
    	tabelaController.getTableHeader().setDefaultRenderer(renderHeader);
    	
    	RenderTable render = new RenderTable();
    	
    	for (int column=0; column < model.getColumnCount(); column++) {
    		tabelaController.setDefaultRenderer(model.getColumnClass(column), render);
    	}
    	
    	
    	for (int col = 0; col < model.getColumnCount(); col++) {
    		TableColumn column = tabelaController.getColumnModel().getColumn(col);
    		column.setMinWidth(100);
    		column.setMaxWidth(100);
    		column.setPreferredWidth(100);
    	}
    	
    }
	
	
	private void listarTunning() {
		
		
		btnConfig.setEnabled(true);
		btnConfig.setVisible(true);
		
		
    	tunningService = getTunningService();
    	
    	
    	page = tunningService.findByMachine(paginaAtual, tamanhoPagina, machine);
    	
    	
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
    	tamanhoPagina = page.getPageSize();
    	
    	
    }


	public TunningService getTunningService() {
		return new TunningService();
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


	public Machine getMachine() {
		return machine;
	}


	public void setMachine(Machine machine) {
		this.machine = machine;
		listarTunning();
	}

}
