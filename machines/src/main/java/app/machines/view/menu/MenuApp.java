package app.machines.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import app.machines.config.Constants;
import app.machines.message.ModelResponse;
import app.machines.model.Machine;
import app.machines.model.User;
import app.machines.service.UserService;
import app.machines.view.controller.TableController;
import app.machines.view.decoration.Decoration;
import app.machines.view.login.Login;
import app.machines.view.serial.SerialView;
import app.machines.view.user.UserView;

import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JButton;

public class MenuApp extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7109004023774328869L;
	private JPanel contentPane;
	private JButton btnUpdateUser;
	private JButton btnControllers;
	private JButton btnExit;
	private JLabel lblName;
	private JButton btnMachine;
	
	long  userId;
	User user;

	/**
	 * Create the frame.
	 */
	public MenuApp(long  id) {
		this.userId = id;
		
		initComponents();
		
		eventHandler();
		
		setUser();
	}
	
	private void initComponents() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 226, 239);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JLabel lblGrettings = new JLabel("Olá,");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblGrettings, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblGrettings, 10, SpringLayout.WEST, contentPane);
		lblGrettings.setFont(Decoration.fontText);
		contentPane.add(lblGrettings);
		
		lblName = new JLabel("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblName, 0, SpringLayout.NORTH, lblGrettings);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblName, 6, SpringLayout.EAST, lblGrettings);
		lblName.setFont(Decoration.fontText);
		contentPane.add(lblName);
		
		btnUpdateUser = new JButton("Editar perfil");
		sl_contentPane.putConstraint(SpringLayout.WEST, btnUpdateUser, 0, SpringLayout.WEST, lblName);
		contentPane.add(btnUpdateUser);
		
		btnControllers = new JButton("Controllers");
		sl_contentPane.putConstraint(SpringLayout.EAST, btnUpdateUser, 0, SpringLayout.EAST, btnControllers);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnControllers, -66, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnControllers, 0, SpringLayout.WEST, lblName);
		contentPane.add(btnControllers);
		
		btnExit = new JButton("Sair");
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnExit, 157, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnUpdateUser, -6, SpringLayout.NORTH, btnExit);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnExit, 0, SpringLayout.WEST, lblName);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnExit, -40, SpringLayout.EAST, contentPane);
		contentPane.add(btnExit);
		
		btnMachine = new JButton("Conectar");
		sl_contentPane.putConstraint(SpringLayout.EAST, btnControllers, 0, SpringLayout.EAST, btnMachine);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnMachine, -95, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnMachine, 109, SpringLayout.WEST, lblName);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnMachine, 0, SpringLayout.WEST, lblName);
		contentPane.add(btnMachine);
	}
	
	private void eventHandler() {
		btnMachine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSerialFrame();
			}
		});
		
		btnControllers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showControllersTable();
			}
		});


		btnUpdateUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showUserFrame();
				setUser();
			}
		});

		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showLoginFrame();
				dispose();
			}
		});
	}
	
	private void showSerialFrame() {
		SerialView view = new SerialView();
		view.setLocationRelativeTo(null);
		view.setVisible(true);
		dispose();
	}
	
	private void showUserFrame() {
		UserView view = new UserView(user,Constants.ALTERAR);
		view.setLocationRelativeTo(null);
		view.setVisible(true);
	}
	
	private void showControllersTable() {
		Machine machine = null;
		TableController view =  TableController.getInstancia(Constants.CONSULTAR,machine);
		view.setLocationRelativeTo(null);
		view.setVisible(true);
	}
	
	private void showLoginFrame() {
		Login view = new Login();
		view.setLocationRelativeTo(null);
		view.setVisible(true);
	}
	
	@SuppressWarnings("unchecked")
	private void setUser(){
		ModelResponse<User> modelResponse = (ModelResponse<User>) getUserService().getById(userId);
		user = modelResponse.getObject();
		
		lblName.setText(user.getName());
		
	}
	
	public UserService getUserService() {
		return new UserService();
	}
}
