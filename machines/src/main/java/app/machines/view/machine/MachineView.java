package app.machines.view.machine;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

import app.machines.message.ModelResponse;
import app.machines.model.Geoposition;
import app.machines.model.Machine;
import app.machines.model.Tunning;
import app.machines.model.Data;
import app.machines.service.DataService;
import app.machines.service.GeopositionService;
import app.machines.service.MachineService;
import app.machines.service.TunningService;
import app.machines.view.data.TableData;
import app.machines.view.decoration.Decoration;
import app.machines.view.tunning.TableTunning;

import javax.swing.JLabel;
import javax.swing.SpringLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JTextField;

public class MachineView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2035971592522478866L;
	private JPanel contentPane;
	private JTextField textFieldLatitude;
	private JTextField textFieldLogitude;
	private JButton btnTunning;
	private JButton btnSaveGeoPosition;
	private JLabel lblMacData;
	private JButton btnAuto;
	private JButton btnSearch;
	private JLabel lblKdData;
	private JLabel lblKiData;
	private JLabel lblKpData;
	private JButton btnData;

	private SerialPort comPort;

	private int portSelected;

	private Machine machine;
	private Geoposition position;
	private Tunning tunning;


	/**
	 * Create the frame.
	 */
	public MachineView(int selectedPort) {
		this.portSelected = selectedPort;

		openConnection();

		initComponent();
		eventHandler();

	}

	private void initComponent() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);

		JLabel lblTitle = new JLabel("Machine");
		lblTitle.setFont(Decoration.fontTittleBold);
		contentPane.add(lblTitle);

		btnSaveGeoPosition = new JButton("Salvar");
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnSaveGeoPosition, -12, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnSaveGeoPosition, -10, SpringLayout.EAST, contentPane);
		btnSaveGeoPosition.setFont(Decoration.fontCaption);
		contentPane.add(btnSaveGeoPosition);

		JLabel lblTitleGeoPosition = new JLabel("Posição:");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblTitleGeoPosition, -6, SpringLayout.NORTH, btnSaveGeoPosition);
		lblTitleGeoPosition.setFont(Decoration.fontTittleBold);
		contentPane.add(lblTitleGeoPosition);

		textFieldLatitude = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.WEST, textFieldLatitude, 438, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblTitleGeoPosition, -6, SpringLayout.WEST, textFieldLatitude);
		sl_contentPane.putConstraint(SpringLayout.NORTH, textFieldLatitude, -3, SpringLayout.NORTH, btnSaveGeoPosition);
		textFieldLatitude.setFont(Decoration.fontText);
		contentPane.add(textFieldLatitude);
		textFieldLatitude.setColumns(10);

		textFieldLogitude = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.EAST, textFieldLatitude, -6, SpringLayout.WEST, textFieldLogitude);
		sl_contentPane.putConstraint(SpringLayout.NORTH, textFieldLogitude, -3, SpringLayout.NORTH, btnSaveGeoPosition);
		sl_contentPane.putConstraint(SpringLayout.EAST, textFieldLogitude, -28, SpringLayout.WEST, btnSaveGeoPosition);
		contentPane.add(textFieldLogitude);
		textFieldLogitude.setFont(Decoration.fontText);
		textFieldLogitude.setColumns(10);

		btnTunning = new JButton("Configurar Controle");
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnTunning, -6, SpringLayout.NORTH, btnSaveGeoPosition);
		btnTunning.setFont(Decoration.fontButton);
		contentPane.add(btnTunning);

		btnSearch = new JButton("Encontrar melhor lugar");
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnSearch, -29, SpringLayout.NORTH, btnSaveGeoPosition);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnSearch, 0, SpringLayout.EAST, btnSaveGeoPosition);
		btnSearch.setFont(Decoration.fontCaption);
		contentPane.add(btnSearch);

		JLabel lblMac = new JLabel("Mac:");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblTitle, -6, SpringLayout.NORTH, lblMac);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblTitle, -119, SpringLayout.WEST, lblMac);
		lblMac.setFont(Decoration.fontTextBold);
		contentPane.add(lblMac);

		lblMacData = new JLabel("00:00:00:00:00:00");
		sl_contentPane.putConstraint(SpringLayout.WEST, textFieldLogitude, 0, SpringLayout.WEST, lblMacData);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblMacData, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblMac, 0, SpringLayout.NORTH, lblMacData);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblMac, -6, SpringLayout.WEST, lblMacData);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblMacData, 0, SpringLayout.EAST, btnSaveGeoPosition);
		lblMacData.setFont(Decoration.fontText);
		contentPane.add(lblMacData);

		btnAuto = new JButton("Auto");
		sl_contentPane.putConstraint(SpringLayout.WEST, btnAuto, 0, SpringLayout.WEST, btnSearch);
		btnAuto.setFont(Decoration.fontCaption);
		contentPane.add(btnAuto);

		JLabel lblKd = new JLabel("Kd:");
		sl_contentPane.putConstraint(SpringLayout.WEST, btnTunning, 0, SpringLayout.WEST, lblKd);
		lblKd.setFont(Decoration.fontTextBold);
		contentPane.add(lblKd);

		lblKdData = new JLabel("0.0");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblKd, 0, SpringLayout.NORTH, lblKdData);
		lblKdData.setFont(Decoration.fontText);
		contentPane.add(lblKdData);

		JLabel lblKi = new JLabel("Ki:");
		sl_contentPane.putConstraint(SpringLayout.WEST, lblKi, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblKd, 0, SpringLayout.WEST, lblKi);
		lblKi.setFont(Decoration.fontTextBold);
		contentPane.add(lblKi);

		lblKiData = new JLabel("0.0");
		sl_contentPane.putConstraint(SpringLayout.WEST, lblKiData, 16, SpringLayout.EAST, lblKi);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblKdData, 6, SpringLayout.SOUTH, lblKiData);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblKdData, 0, SpringLayout.WEST, lblKiData);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblKiData, 0, SpringLayout.NORTH, lblKi);
		lblKiData.setFont(Decoration.fontText);
		contentPane.add(lblKiData);

		JLabel lblKp = new JLabel("Kp:");
		sl_contentPane.putConstraint(SpringLayout.WEST, lblKp, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblKi, 6, SpringLayout.SOUTH, lblKp);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblKp, 44, SpringLayout.NORTH, contentPane);
		lblKp.setFont(Decoration.fontTextBold);
		contentPane.add(lblKp);

		lblKpData = new JLabel("0.0");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblKpData, 0, SpringLayout.NORTH, lblKp);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblKpData, 9, SpringLayout.EAST, lblKp);
		lblKpData.setFont(Decoration.fontText);
		contentPane.add(lblKpData);

		btnData = new JButton("Dados");
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnAuto, 0, SpringLayout.NORTH, btnData);
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnData, 30, SpringLayout.SOUTH, lblMacData);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnData, 0, SpringLayout.EAST, btnSaveGeoPosition);
		btnData.setFont(Decoration.fontCaption);
		contentPane.add(btnData);
	}

	private void eventHandler() {
		btnSaveGeoPosition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveGeoPosition();
			}
		});

		btnData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showDataTable();
			}
		});
		
		btnAuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toogleAuto();
			}
		});
		
		btnTunning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTunningTable();
			}
		});
		
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getBestSpot();
			}
		});
	}
	
	void showDataTable(){
		TableData view =  TableData.getInstancia(machine);
		view.setLocationRelativeTo(null);
		view.setVisible(true);
	}
	
	void showTunningTable(){
		TableTunning view =  TableTunning.getInstancia(machine);
		view.setLocationRelativeTo(null);
		view.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	private void saveGeoPosition(){
		getPosition().setLatitude(textFieldLatitude.getText());
		getPosition().setLongitude(textFieldLogitude.getText());

		ModelResponse<Geoposition> response  = (ModelResponse<Geoposition>)getGeopositionService().update(getPosition());

		setPosition(response.getObject());
	}

	public void close() {
		if(comPort != null) {
		comPort.removeDataListener();
		comPort.closePort();
		}
	}

	void openConnection(){
		comPort = SerialPort.getCommPorts()[portSelected];
		comPort.setBaudRate(115200);
		comPort.setNumDataBits(8);
		comPort.setParity(SerialPort.NO_PARITY);
		comPort.setNumStopBits(1);
		comPort.openPort();


		MessageListener listener = new MessageListener();
		comPort.addDataListener(listener);
	}


	public MachineService getMachineService() {
		return new MachineService();
	}

	public GeopositionService getGeopositionService() {
		return new GeopositionService();
	}

	public DataService getDataService() {
		return new DataService();
	}
	
	public TunningService getTunningService() {
		return new TunningService();
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
		setData();
	}

	private void setData() {
		textFieldLatitude.setText(getPosition().getLatitude());
		textFieldLogitude.setText(getPosition().getLongitude());
		lblMacData.setText(getMachine().getMac());
		if(tunning != null) {
			lblKpData.setText(tunning.getController().getP().toString());
			lblKiData.setText(tunning.getController().getI().toString());
			lblKdData.setText(tunning.getController().getD().toString());
		}
		
		if(getMachine().isAuto()) {
			btnAuto.setText("Auto on");
		}else {
			btnAuto.setText("Auto off");
		}

	}
	
	private void getBestSpot() {
		String d = "{\"type\":2,\"ki\":0,\"kd\":0,\"kp\":"+0+",\"auto\":0}"; 
		System.out.println(d);
		comPort.writeBytes(d.getBytes(), d.length());
	}
	
	@SuppressWarnings("unchecked")
	private void toogleAuto() {
		if(getMachine().isAuto()) {
			machine.setAuto(false);
			ModelResponse<Machine> modelResponse = (ModelResponse<Machine>) getMachineService().update(machine);
			
			if(!modelResponse.isError()) {
				String d = "{\"type\":0,\"ki\":"+tunning.getController().getI().toString()+",\"kd\":"+tunning.getController().getD().toString()+",\"kp\":"+tunning.getController().getP().toString()+",\"auto\":"+0+"}"; 
				System.out.println(d);
				comPort.writeBytes(d.getBytes(), d.length());
			}
			
		}else {
			machine.setAuto(true);
			ModelResponse<Machine> modelResponse = (ModelResponse<Machine>) getMachineService().update(machine);
			
			if(!modelResponse.isError()) {
				String d = "{\"type\":"+0+",\"ki\":"+tunning.getController().getI().toString()+",\"kd\":"+tunning.getController().getD().toString()+",\"kp\":"+tunning.getController().getP().toString()+",\"auto\":"+1+"}"; 
				System.out.println(d);
				comPort.writeBytes(d.getBytes(), d.length());
			}
		}
		setData();
	}


	public Geoposition getPosition() {
		return position;
	}

	public void setPosition(Geoposition position) {
		this.position = position;
		setData();
	}
	
	public void setTunning(Tunning tunning) {
		this.tunning = tunning;
	}


	private final class MessageListener implements SerialPortMessageListener
	{
		@Override
		public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }

		@Override
		public byte[] getMessageDelimiter() { return new byte[] {(byte)'\n'}; }

		@Override
		public boolean delimiterIndicatesEndOfMessage() { return true; }

		@SuppressWarnings("unchecked")
		@Override
		public void serialEvent(SerialPortEvent event)
		{
			byte[] newData = event.getReceivedData();
			String s = "";
			for (int i = 0; i < newData.length; ++i) {
				s = s.concat(String.valueOf((char)newData[i]));
			}

			System.out.println(s.trim());

			Object obj=JSONValue.parse(s);  
			JSONObject jsonObject = (JSONObject) obj;  

			String mac = (String) jsonObject.get("mac"); 

			ModelResponse<Machine> modelResponse  = (ModelResponse<Machine>) getMachineService().getMachineByMac(mac);
			if(modelResponse.getObject() == null){
				machine = new Machine();
				machine.setAuto(false);

				machine.setMac(mac);


				modelResponse = (ModelResponse<Machine>) getMachineService().save(machine);
				machine = modelResponse.getObject();


				Geoposition position = new Geoposition();
				position.setMachine(machine);

				System.out.println(machine.getId());

				ModelResponse<Geoposition> response  = (ModelResponse<Geoposition>)getGeopositionService().save(position);
				setPosition(response.getObject());
			} else {
				
				machine = modelResponse.getObject();
				System.out.println("Teste");
				
				ModelResponse<Tunning> responseTunning  = (ModelResponse<Tunning>)getTunningService().getLastConfig(machine);
				if(responseTunning.getObject()!=null&&!responseTunning.getObject().equals(tunning)) {
					setTunning(responseTunning.getObject());
					System.out.println(responseTunning.getObject().getController().getName());
					

					String d = "{\"type\":"+1+",\"ki\":"+tunning.getController().getI().toString()+",\"kd\":"+tunning.getController().getD().toString()+",\"kp\":"+tunning.getController().getP().toString()+",\"auto\":"+0+"}"; 
					System.out.println(d);
					comPort.writeBytes(d.getBytes(), d.length());
				}

				
				ModelResponse<Geoposition> response  = (ModelResponse<Geoposition>)getGeopositionService().getByMachine(getMachine());
				setPosition(response.getObject());
			}

			Data data = new Data();
			data.setAngle((Double) jsonObject.get("angle"));
			data.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
			data.setError((Double) jsonObject.get("error"));
			data.setVoltageGenerated((Double) jsonObject.get("voltage"));
			data.setMachine(machine);

			getDataService().save(data);

		}
	}
}
