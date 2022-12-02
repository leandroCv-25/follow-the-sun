package app.machines.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import app.machines.service.errors.CampoRequerido;
import app.machines.service.errors.CampoName;

@Entity
@Table(name="Controller")
public class Controller implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -891247556628211541L;

	
	private long id;
	@CampoRequerido(valor=1, mensagem = "O nome deve ser informado")
	@CampoName()
	private String name;
	private Double p;
	private Double d;
	private Double i;
	
	
	
	public Controller() {
		super();
	}
	
	
	public Controller(long id, String name, double p, double d, double i) {
		super();
		this.id = id;
		this.name = name;
		this.p = p;
		this.d = d;
		this.i = i;
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
	
	@Column(name = "NAME", length = 100, nullable = false, unique = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "Controller_P", nullable = false)
	public Double getP() {
		return p;
	}
	public void setP(double p) {
		this.p = p;
	}
	@Column(name = "Controller_D", nullable = false)
	public Double getD() {
		return d;
	}
	public void setD(double d) {
		this.d = d;
	}
	@Column(name = "Controller_I", nullable = false)
	public Double getI() {
		return i;
	}
	public void setI(double i) {
		this.i = i;
	}
	
}
