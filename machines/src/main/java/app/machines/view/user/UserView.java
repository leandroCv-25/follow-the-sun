package app.machines.view.user;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import app.machines.config.Constants;
import app.machines.message.ModelResponse;
import app.machines.model.User;
import app.machines.service.UserService;
import app.machines.service.errors.ErrorsData;
import app.machines.view.decoration.Decoration;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -772034344768054409L;
	private JButton btnSave;
	private JButton btnCancel;
	private JPasswordField passwordField;
	private JTextField emailField;
	private JTextField nameField;
	private JLabel msgEmail;
	private JLabel msgPassword;

	User user;
	private JLabel lblNewLabel_4;
	Integer opcaoCadastro;

	private ModelResponse<User> modelResponse;

	private ModelResponse<ErrorsData> errors;


	/**
	 * Create the frame.
	 */
	public UserView(User user,Integer opcaoCadastro) {

		this.opcaoCadastro = opcaoCadastro;
		this.user =  user;

		initComponents();

		eventHandler();

	}

	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 350);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);

		nameField = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, nameField, 37, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, nameField, -27, SpringLayout.EAST, getContentPane());
		nameField.setFont(Decoration.fontText);
		getContentPane().add(nameField);
		nameField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("E-mail:");
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel_1, 11, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel_1, -206, SpringLayout.SOUTH, getContentPane());
		lblNewLabel_1.setFont(Decoration.fontText);
		getContentPane().add(lblNewLabel_1);

		emailField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, emailField, 6, SpringLayout.SOUTH, lblNewLabel_1);
		springLayout.putConstraint(SpringLayout.WEST, emailField, 0, SpringLayout.WEST, nameField);
		springLayout.putConstraint(SpringLayout.EAST, emailField, -27, SpringLayout.EAST, getContentPane());
		emailField.setFont(Decoration.fontText);
		getContentPane().add(emailField);
		emailField.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Senha:");
		springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel_2, -135, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lblNewLabel_2, 0, SpringLayout.EAST, lblNewLabel_1);
		lblNewLabel_2.setFont(Decoration.fontText);
		getContentPane().add(lblNewLabel_2);

		passwordField = new JPasswordField();
		springLayout.putConstraint(SpringLayout.NORTH, passwordField, 6, SpringLayout.SOUTH, lblNewLabel_2);
		springLayout.putConstraint(SpringLayout.WEST, passwordField, -1, SpringLayout.WEST, nameField);
		springLayout.putConstraint(SpringLayout.EAST, passwordField, 0, SpringLayout.EAST, nameField);
		passwordField.setFont(Decoration.fontText);
		getContentPane().add(passwordField);

		if(opcaoCadastro == Constants.INCLUIR) {
			btnSave = new JButton("Cadastrar");
		}else {
			btnSave = new JButton("Alterar");
		}
		springLayout.putConstraint(SpringLayout.SOUTH, btnSave, -27, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnSave, -10, SpringLayout.EAST, getContentPane());
		btnSave.setFont(Decoration.fontButton);
		getContentPane().add(btnSave);

		btnCancel = new JButton("Cancelar");
		springLayout.putConstraint(SpringLayout.SOUTH, btnCancel, 0, SpringLayout.SOUTH, btnSave);
		springLayout.putConstraint(SpringLayout.EAST, btnCancel, -6, SpringLayout.WEST, btnSave);
		btnCancel.setFont(Decoration.fontButton);
		getContentPane().add(btnCancel);

		lblNewLabel_4 = new JLabel("Nome:");
		springLayout.putConstraint(SpringLayout.NORTH, nameField, 0, SpringLayout.SOUTH, lblNewLabel_4);
		springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel_4, -270, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel_4, 10, SpringLayout.WEST, getContentPane());
		lblNewLabel_4.setFont(new Font("Verdana", Font.BOLD, 16));
		getContentPane().add(lblNewLabel_4);


		msgEmail = new JLabel("");
		springLayout.putConstraint(SpringLayout.SOUTH, msgEmail, 20, SpringLayout.SOUTH, emailField);
		springLayout.putConstraint(SpringLayout.WEST, msgEmail, 48, SpringLayout.WEST, getContentPane());
		getContentPane().add(msgEmail);

		msgPassword = new JLabel("");
		springLayout.putConstraint(SpringLayout.WEST, msgPassword, 48, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, msgPassword, 20, SpringLayout.SOUTH, passwordField);
		getContentPane().add(msgPassword);

		if(opcaoCadastro != Constants.INCLUIR) {
			setUserData();
		}
		initStateMsg();
	}
	
	private void initStateMsg(){
		msgPassword.setVisible(false);
		msgEmail.setVisible(true);
		passwordField.setBorder(BorderFactory.createLineBorder(Color.gray,1));
		emailField.setBorder(BorderFactory.createLineBorder(Color.gray,1));
		nameField.setBorder(BorderFactory.createLineBorder(Color.gray,1));
	}

	private void eventHandler() {

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(opcaoCadastro == Constants.INCLUIR) {
					signinUser();
				}else {
					updateUser();
				}
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void signinUser(){

		UserService userService = getUserService();
		getDataFromView();
		errors = (ModelResponse<ErrorsData>) userService.validarDadosFromView(user);
		if (errors.isError()) {
			showErrorUserFromServidor();
			JOptionPane.showMessageDialog(null, errors.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE );			
		} else {
			modelResponse = (ModelResponse<User>) userService.save(user);
			user = modelResponse.getObject();
			JOptionPane.showMessageDialog(null, modelResponse.getMessage(), "Cadastro", JOptionPane.INFORMATION_MESSAGE );	
			dispose();
		}
		//cleanText();
	}
	
	@SuppressWarnings("unchecked")
	private void updateUser(){

		UserService userService = getUserService();
		getDataFromView();
		errors = (ModelResponse<ErrorsData>) userService.validarDadosFromView(user);
		if (errors.isError()) {
			showErrorUserFromServidor();
			JOptionPane.showMessageDialog(null, errors.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE );			
		} else {
			modelResponse = (ModelResponse<User>) userService.update(user);
			user = modelResponse.getObject();
			JOptionPane.showMessageDialog(null, modelResponse.getMessage(), "Atualização", JOptionPane.INFORMATION_MESSAGE );	
			//dispose();
		}
		//cleanText();
	}


	private void getDataFromView(){
		@SuppressWarnings("deprecation")
		String pass = passwordField.getText();

		user.setPassword(pass);
		user.setEmail(emailField.getText());
		user.setName(nameField.getText());
	}

	public UserService getUserService() {
		return new UserService();
	}

	private void showErrorUserFromServidor() {
		
		initStateMsg();

		for (ErrorsData erro : errors.getListObject()) {

			if (erro.getNumeroCampo() == 1) {
				msgEmail.setVisible(true);
				msgEmail.setForeground(Color.red);
				msgEmail.setText(erro.getShowMensagemErro());
				emailField.setBorder(BorderFactory.createLineBorder(Color.red,2));

			}
			if (erro.getNumeroCampo() == 2 ) {
				msgPassword.setVisible(true);
				msgPassword.setForeground(Color.red);
				msgPassword.setText(erro.getShowMensagemErro());
				passwordField.setBorder(BorderFactory.createLineBorder(Color.red,2));
			}
		}
	}


	private void setUserData() {
		passwordField.setText(user.getPassword());
		emailField.setText(user.getEmail());
		nameField.setText(user.getName());
	}

	public ModelResponse<User> getModelResponse() {
		return modelResponse;
	}



	public void setModelResponse(ModelResponse<User> modelResponse) {
		this.modelResponse = modelResponse;
	}

}
