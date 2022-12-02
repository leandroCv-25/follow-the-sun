package app.machines.view.controller;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import app.machines.config.Constants;
import app.machines.config.Page;
import app.machines.message.ModelResponse;
import app.machines.model.Controller;
import app.machines.model.Machine;
import app.machines.model.Tunning;
import app.machines.service.ControllerService;
import app.machines.service.TunningService;
import app.machines.view.RenderHeaderTable;
import app.machines.view.RenderTable;

public class TableController extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8195950197183379799L;
	private JPanel  contentPane;
	private JTable  tabelaController;
	private JButton btnPesquisar;
	private JButton btnPrimeiro;
	private JButton btnAnterior;
	private JButton btnProximo;
	private JButton btnUltimo;
	private JButton btnIncluir;
	private JButton btnAlterar;
	private JButton btnExcluir;
	private JButton btnClose;
	private JButton btnConfig;

	private TableControllerModel model;
	private Page<Controller> page;
	private ControllerService controllerService;
	private Controller controller;

	private Integer option;
	private Machine machine;
	private int row = 0; 
	private int column = 0;
	private int tamanhoPagina = 10;
	private int paginaAtual = 0;
	private JTextField nomePesquisa;


	private static TableController TABLE_CONTROLLER;

	private TableController(Integer option, Machine machine) {
		this.option = option;
		this.machine = machine;
		initComponents();
		eventHandlers();
		iniciarTable();
	}


	public static TableController getInstancia(Integer option, Machine machine) {
		if (Objects.isNull(TABLE_CONTROLLER)) {
			TABLE_CONTROLLER = new TableController(option, machine);
		}
		TABLE_CONTROLLER.setOption(option);
		TABLE_CONTROLLER.setMachine(machine);
		return TABLE_CONTROLLER;
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

		btnIncluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showControllerFrame(Constants.INCLUIR);
				iniciarTable();
			}
		});
		btnAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getRowTable();
				showControllerFrame(Constants.ALTERAR);
				iniciarTable();
			}
		});
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getRowTable();
				controllerService.delete(controller);
				iniciarTable();

			}
		});
		nomePesquisa.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
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
				configController();
			}
		});
	}


	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 570);
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

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setFont(new Font("Verdana", Font.BOLD, 14));
		lblNome.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNome.setBounds(10, 22, 73, 30);
		panelPesquisa.add(lblNome);

		nomePesquisa = new JTextField();
		nomePesquisa.setBounds(106, 25, 250, 28);
		panelPesquisa.add(nomePesquisa);
		nomePesquisa.setColumns(10);

		btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.setFont(new Font("Verdana", Font.BOLD, 14));
		btnPesquisar.setBounds(366, 22, 119, 30);
		panelPesquisa.add(btnPesquisar);

		btnIncluir = new JButton("Incluir");

		btnIncluir.setFont(new Font("Verdana", Font.BOLD, 14));
		btnIncluir.setBounds(10, 478, 89, 33);
		contentPane.add(btnIncluir);

		btnAlterar = new JButton("Alterar");
		btnAlterar.setFont(new Font("Verdana", Font.BOLD, 14));
		btnAlterar.setBounds(109, 478, 89, 33);
		contentPane.add(btnAlterar);

		btnExcluir = new JButton("Excluir");
		btnExcluir.setFont(new Font("Verdana", Font.BOLD, 14));
		btnExcluir.setBounds(208, 478, 89, 33);
		contentPane.add(btnExcluir);

		btnClose = new JButton("Fechar");
		btnClose.setFont(new Font("Verdana", Font.BOLD, 14));
		btnClose.setBounds(307, 478, 89, 33);
		contentPane.add(btnClose);

		btnConfig = new JButton("Configurar");
		btnConfig.setFont(new Font("Verdana", Font.BOLD, 14));
		btnConfig.setBounds(406, 478, 125, 33);
		btnConfig.setEnabled(false);
		btnConfig.setVisible(false);
		contentPane.add(btnConfig);

	}

	@SuppressWarnings("unchecked")
	private void configController(){
		getRowTable();
		if(controller != null) {

			ModelResponse<Tunning> response= (ModelResponse<Tunning>) getTunningService().getLastConfig(machine); 
			Tunning tunningLast = response.getObject();
			if(tunningLast != null) {
				tunningLast.setDataEnd(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
				
				getTunningService().update(tunningLast);
			}
						
			Tunning tunning = new Tunning(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),controller,machine);

			getTunningService().save(tunning);
		}
	}

	public void iniciarTable() {

		listarController();

		model = new TableControllerModel(page.getContent());

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


	private void getRowTable() {
		controller = getController();
		if ( tabelaController.getSelectedRow()!= -1 ) {
			row = tabelaController.getSelectedRow();
			column = tabelaController.getSelectedColumn();
			controller = model.getController(row);
		} else  {
			JOptionPane.showMessageDialog(null, "Selecione uma row na Table","Erro", JOptionPane.ERROR_MESSAGE);
		}



	}

	private void showControllerFrame(Integer opcaoCadastro) {
		ControllerGUI view = new ControllerGUI(controller, opcaoCadastro);
		view.setLocationRelativeTo(null);
		view.setVisible(true);
	}


	private void listarController() {

		if(option == Constants.INCLUIR) {
			btnConfig.setEnabled(true);
			btnConfig.setVisible(true);
		}

		controllerService = getControllerService();

		if (nomePesquisa.getText().equals("")) {
			page = controllerService.findAll(paginaAtual, tamanhoPagina);
		} else {
			page = controllerService.findAll(paginaAtual, tamanhoPagina, nomePesquisa.getText());	
		}

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


	public ControllerService getControllerService() {
		return new ControllerService();
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


	public Controller getController() {
		return new Controller();
	}


	public Integer getOption() {
		return option;
	}


	public void setOption(Integer option) {
		this.option = option;
	}


	public Machine getMachine() {
		return machine;
	}


	public void setMachine(Machine machine) {
		this.machine = machine;
		listarController();
	}

}
