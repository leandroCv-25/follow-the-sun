package app.machines.view.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SpringLayout;

import app.machines.config.Constants;
import app.machines.message.ModelResponse;
import app.machines.model.User;
import app.machines.service.UserService;
import app.machines.view.decoration.Decoration;
import app.machines.view.menu.MenuApp;
import app.machines.view.user.UserView;

import javax.swing.JButton;

public class Login extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1571161498109445209L;

	JButton btnLogin;
	JButton btnSignin;
	JPasswordField passwordField;
	private JTextField emailField;

	String password;
	String email;

	private ModelResponse<User> modelResponse;


	/**
	 * Create the frame.
	 */
	public Login() {
		initComponents();

		eventHandler();
	}


	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);

		JLabel lblNewLabel = new JLabel("Entrar");
		lblNewLabel.setFont(Decoration.fontTittleBold);
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lblNewLabel, -193, SpringLayout.EAST, getContentPane());
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("E-mail:");
		lblNewLabel_1.setFont(Decoration.fontText);
		getContentPane().add(lblNewLabel_1);

		emailField = new JTextField();
		springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel_1, -6, SpringLayout.NORTH, emailField);
		springLayout.putConstraint(SpringLayout.WEST, emailField, 10, SpringLayout.WEST, lblNewLabel_1);
		springLayout.putConstraint(SpringLayout.NORTH, emailField, 78, SpringLayout.NORTH, getContentPane());
		emailField.setFont(Decoration.fontText);
		getContentPane().add(emailField);
		emailField.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Senha:");
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel_2, 43, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lblNewLabel_1, 0, SpringLayout.EAST, lblNewLabel_2);
		lblNewLabel_2.setFont(Decoration.fontText);
		getContentPane().add(lblNewLabel_2);

		passwordField = new JPasswordField();
		springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel_2, -6, SpringLayout.NORTH, passwordField);
		springLayout.putConstraint(SpringLayout.WEST, passwordField, 53, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, passwordField, 0, SpringLayout.EAST, emailField);
		passwordField.setFont(Decoration.fontText);
		getContentPane().add(passwordField);

		btnLogin = new JButton("Entrar");
		springLayout.putConstraint(SpringLayout.SOUTH, btnLogin, -27, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnLogin, -10, SpringLayout.EAST, getContentPane());
		btnLogin.setFont(Decoration.fontButton);
		getContentPane().add(btnLogin);

		btnSignin = new JButton("Cadastrar");
		springLayout.putConstraint(SpringLayout.EAST, emailField, 101, SpringLayout.EAST, btnSignin);
		springLayout.putConstraint(SpringLayout.SOUTH, passwordField, -42, SpringLayout.NORTH, btnSignin);
		springLayout.putConstraint(SpringLayout.SOUTH, btnSignin, 0, SpringLayout.SOUTH, btnLogin);
		springLayout.putConstraint(SpringLayout.EAST, btnSignin, -6, SpringLayout.WEST, btnLogin);
		btnSignin.setFont(Decoration.fontButton);
		getContentPane().add(btnSignin);
	}


	private void eventHandler() {

		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

		btnSignin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showUserFrame();
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void login() {
		UserService userService = getUserService();
		getDataFromView();

		modelResponse = (ModelResponse<User>) userService.login(email,password);
		User user = modelResponse.getObject();
		JOptionPane.showMessageDialog(null, modelResponse.getMessage(), "Login", JOptionPane.INFORMATION_MESSAGE );	
		
		if(!modelResponse.isError()) {
			MenuApp view = new MenuApp(user.getId());
			view.setLocationRelativeTo(null);
			view.setVisible(true);
			dispose();
		}
		
		cleanText();

	}

	private void showUserFrame() {
		UserView view = new UserView(new User(), Constants.INCLUIR);
		view.setLocationRelativeTo(null);
		view.setVisible(true);
	}


	private void getDataFromView(){
		@SuppressWarnings("deprecation")
		String pass = passwordField.getText();

		password = pass;
		email = emailField.getText();
	}

	public UserService getUserService() {
		return new UserService();
	}

	private void cleanText() {
		passwordField.setText("");
		emailField.setText("");
	}	
}
