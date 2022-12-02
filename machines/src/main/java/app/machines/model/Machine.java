package app.machines.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Machine")
public class Machine implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -290648328528442084L;
	private long id;
	private String mac;
	private boolean auto;
	
	public Machine(){
		super();
	}
	
	public Machine(String mac, boolean auto, long id){
		super();
		this.id = id;
		this.auto = auto;
		this.mac = mac;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MACHINE_ID")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "mac", length = 64, nullable = false, unique = true)
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	@Column(name = "auto",  nullable = false)
	public boolean isAuto() {
		return auto;
	} 
	public void setAuto(boolean auto) {
		this.auto = auto;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Machine other = (Machine) obj;
		return Objects.equals(id, other.getId());
	}
}
