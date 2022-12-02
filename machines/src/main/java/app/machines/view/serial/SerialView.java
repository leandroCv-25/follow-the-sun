package app.machines.view.serial;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import com.fazecast.jSerialComm.SerialPort;

import app.machines.config.Constants;
import app.machines.view.machine.MachineView;

public class SerialView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1664936606297716154L;
	private JPanel contentPane;

	private JLabel lblTitle;
	private JLabel lblStatus;
	private JButton btnConnect;
	private JComboBox<String> serialComboBox;
	
	private MachineView machineView;
	
	private SerialPort[] ports;

	private int openComState = Constants.NOCONNECTION;




	/**
	 * Create the frame.
	 */
	public SerialView() {
		serialPorts();
		initComponents();

		eventHandler();

	}

	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 100);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);

		lblTitle = new JLabel("Conexão serial");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblTitle, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblTitle, 10, SpringLayout.WEST, contentPane);
		contentPane.add(lblTitle);
		
		lblStatus = new JLabel("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblStatus, 6, SpringLayout.SOUTH, lblTitle);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblStatus, 10, SpringLayout.WEST, lblTitle);
		contentPane.add(lblStatus);
		
		btnConnect = new JButton("Não disponível");
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnConnect, 0, SpringLayout.NORTH, lblTitle);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnConnect, -10, SpringLayout.EAST, contentPane);
		contentPane.add(btnConnect);

		serialComboBox = new JComboBox<String>();
		for(SerialPort port: ports) {
			serialComboBox.addItem(port.getDescriptivePortName());
		}
		sl_contentPane.putConstraint(SpringLayout.NORTH, serialComboBox, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, serialComboBox, -61, SpringLayout.WEST, btnConnect);
		contentPane.add(serialComboBox);
		
		didChangeTheState();
	}

	private void serialPorts(){
		ports = SerialPort.getCommPorts();

		if(ports.length==0) {
			openComState = Constants.NOCONNECTION;
		} else {
			openComState = Constants.CLOSED;
		}
	}
	
	private void didChangeTheState() {
		if(openComState == Constants.CLOSED) {
			serialComboBox.setEnabled(true);
			serialComboBox.setVisible(true);
			btnConnect.setText("Conectar");
			btnConnect.setEnabled(true);
			lblStatus.setText("Esperando Conexão");
		}
		if(openComState == Constants.OPEN) {
			serialComboBox.setEnabled(false);
			btnConnect.setText("Desconectar");
			serialComboBox.setVisible(false);
			lblStatus.setText("Conectado");
		}
		if(openComState == Constants.NOCONNECTION) {
			btnConnect.setText("Não disponível");
			btnConnect.setEnabled(false);
			lblStatus.setText("Sem conexões");
		}
	}


	private void eventHandler() {
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(openComState == Constants.CLOSED) {
					showMachineView();
				} else if(openComState == Constants.OPEN) {
					closeMachineView();
				}
			}
		});
	}
	
	void showMachineView() {
		openComState = Constants.OPEN;
		machineView =  new MachineView(serialComboBox.getSelectedIndex());
		machineView.setLocationRelativeTo(null);
		machineView.setVisible(true);
		didChangeTheState();
	}
	
	void closeMachineView() {
		openComState = Constants.CLOSED;
		if(machineView != null) {
			machineView.close();
			machineView.dispose();
		}
		didChangeTheState();
	}
}


