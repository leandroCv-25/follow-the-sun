package app.machines.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Data")
public class Data implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2969689766569562239L;
	private long id;
	private double voltageGenerated;
	private double angle;
	private double error;
	private String date;
	
	private Machine machine;

	public Data(){
		super();
	}

	public Data(double voltageGenerated,double angle,double error, String date, Machine machine){
		super();
		this.voltageGenerated = voltageGenerated;
		this.angle = angle;
		this.error = error;
		this.date = date;
		this.setMachine(machine);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "voltage", nullable = false)
	public double getVoltageGenerated() {
		return voltageGenerated;
	}
	public void setVoltageGenerated(double voltageGenerated) {
		this.voltageGenerated = voltageGenerated;
	}

	@Column(name = "angle", nullable = false)
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}

	@Column(name = "error", nullable = false)
	public double getError() {
		return error;
	}
	public void setError(double error) {
		this.error = error;
	}

	@Column(name = "date", length = 100, nullable = false, unique = false)
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MACHINE_ID", referencedColumnName = "MACHINE_ID")
	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}
}
