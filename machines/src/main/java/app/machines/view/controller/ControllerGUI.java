package app.machines.view.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import app.machines.config.Constants;
import app.machines.message.ModelResponse;
import app.machines.model.Controller;
import app.machines.service.ControllerService;
import app.machines.service.errors.ErrorsData;
import app.machines.view.decoration.Decoration;

import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ControllerGUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6101269844751575393L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JTextField textFieldKp;
	private JTextField textFieldKi;
	private JTextField textFieldKd;
	private JLabel msgName;
	
	Controller controller;
	Integer opcaoCadastro;
	
	private ModelResponse<Controller> modelResponse;

	private ModelResponse<ErrorsData> errors;


	/**
	 * Create the dialog.
	 */
	public ControllerGUI(Controller controller,Integer opcaoCadastro) {
		this.controller=controller;
		this.opcaoCadastro = opcaoCadastro;
		
		if(opcaoCadastro==Constants.ALTERAR) {
			setData();
		}
		
		component_init();
		
		
	}
	
	private void setData() {
		textFieldName.setText(controller.getName());
		textFieldKp.setText(String.valueOf(controller.getP()));
		textFieldKi.setText(String.valueOf(controller.getI()));
		textFieldKd.setText(String.valueOf(controller.getD()));
	}

	private void component_init() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);

		JLabel lblTitle = new JLabel("Controle");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblTitle, 10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblTitle, 145, SpringLayout.WEST, contentPanel);
		lblTitle.setFont(Decoration.fontTittleBold);
		contentPanel.add(lblTitle);

		textFieldName = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, textFieldName, 71, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, textFieldName, 10, SpringLayout.WEST, contentPanel);
		textFieldName.setFont(Decoration.fontText);
		contentPanel.add(textFieldName);
		textFieldName.setColumns(10);

		JLabel lblName = new JLabel("Nome");
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblName, 0, SpringLayout.WEST, textFieldName);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblName, -6, SpringLayout.NORTH, textFieldName);
		lblName.setFont(Decoration.fontText);
		contentPanel.add(lblName);

		textFieldKp = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, textFieldKp, 0, SpringLayout.NORTH, textFieldName);
		textFieldKp.setFont(Decoration.fontText);
		contentPanel.add(textFieldKp);
		textFieldKp.setColumns(10);

		JLabel lblKp = new JLabel("Kp:");
		sl_contentPanel.putConstraint(SpringLayout.WEST, textFieldKp, 0, SpringLayout.WEST, lblKp);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblKp, 6, SpringLayout.SOUTH, lblTitle);
		sl_contentPanel.putConstraint(SpringLayout.EAST, lblKp, 0, SpringLayout.EAST, lblTitle);
		lblKp.setFont(Decoration.fontText);
		contentPanel.add(lblKp);

		textFieldKi = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.WEST, textFieldKi, 0, SpringLayout.WEST, textFieldKp);
		textFieldKi.setFont(Decoration.fontText);
		contentPanel.add(textFieldKi);
		textFieldKi.setColumns(10);

		JLabel lblKi = new JLabel("Ki:");
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblKi, 0, SpringLayout.WEST, textFieldKp);
		lblKi.setFont(Decoration.fontText);
		contentPanel.add(lblKi);

		textFieldKd = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, textFieldKi, 0, SpringLayout.NORTH, textFieldKd);
		sl_contentPanel.putConstraint(SpringLayout.EAST, textFieldKd, 0, SpringLayout.EAST, textFieldName);
		textFieldKd.setFont(Decoration.fontText);
		contentPanel.add(textFieldKd);
		textFieldKd.setColumns(10);

		JLabel lblKd = new JLabel("Kd:");
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblKd, -71, SpringLayout.SOUTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblKi, 0, SpringLayout.NORTH, lblKd);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, textFieldKd, 6, SpringLayout.SOUTH, lblKd);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblKd, 10, SpringLayout.WEST, contentPanel);
		lblKd.setFont(Decoration.fontText);
		contentPanel.add(lblKd);
		
		msgName = new JLabel("");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, msgName, 6, SpringLayout.SOUTH, textFieldName);
		sl_contentPanel.putConstraint(SpringLayout.WEST, msgName, 0, SpringLayout.WEST, textFieldName);
		msgName.setFont(Decoration.fontCaption);
		contentPanel.add(msgName);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(opcaoCadastro==Constants.ALTERAR) {
							updateUser();
						} else {
							saveController();
						}
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}

	}
	
	@SuppressWarnings("unchecked")
	private void saveController(){

		ControllerService service = getControllerService();
		this.controller = new Controller();
		getDataFromView();
		errors = (ModelResponse<ErrorsData>) service.validarDadosFromView(controller);
		if (errors.isError()) {
			showErrorFromServidor();
			JOptionPane.showMessageDialog(null, errors.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE );			
		} else {
			modelResponse = (ModelResponse<Controller>) service.save(controller);
			controller = modelResponse.getObject();
			JOptionPane.showMessageDialog(null, modelResponse.getMessage(), "Cadastro", JOptionPane.INFORMATION_MESSAGE );	
			dispose();
		}
		//cleanText();
	}
	
	@SuppressWarnings("unchecked")
	private void updateUser(){

		ControllerService service = getControllerService();
		getDataFromView();
		errors = (ModelResponse<ErrorsData>) service.validarDadosFromView(controller);
		if (errors.isError()) {
			showErrorFromServidor();
			JOptionPane.showMessageDialog(null, errors.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE );			
		} else {
			modelResponse = (ModelResponse<Controller>) service.update(controller);
			controller = modelResponse.getObject();
			JOptionPane.showMessageDialog(null, modelResponse.getMessage(), "Cadastro", JOptionPane.INFORMATION_MESSAGE );	
			dispose();
		}
		//cleanText();
	}
	
	private void getDataFromView() {
		
		controller.setName(textFieldName.getText());
		controller.setP(Double.parseDouble(textFieldKp.getText()));
		controller.setI(Double.parseDouble(textFieldKi.getText()));
		controller.setD(Double.parseDouble(textFieldKd.getText()));
	}
	
	private void initStateMsg(){
		msgName.setVisible(false);
		textFieldName.setBorder(BorderFactory.createLineBorder(Color.gray,1));
	}
	
	private void showErrorFromServidor() {
		
		initStateMsg();

		for (ErrorsData erro : errors.getListObject()) {

			if (erro.getNumeroCampo() == 1) {
				msgName.setVisible(true);
				msgName.setForeground(Color.red);
				msgName.setText(erro.getShowMensagemErro());
				textFieldName.setBorder(BorderFactory.createLineBorder(Color.red,2));

			}
		}
	}
	
	public ControllerService getControllerService() {
		return new ControllerService();
	}
	
	public ModelResponse<Controller> getModelResponse() {
		return modelResponse;
	}



	public void setModelResponse(ModelResponse<Controller> modelResponse) {
		this.modelResponse = modelResponse;
	}


}
