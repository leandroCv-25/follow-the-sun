package app.machines.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Geoposition")
public class Geoposition implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8336006110048901876L;
	private long id;
	private String latitude;
	private String longitude;
	private Machine machine;
	
	public Geoposition(){
		this.latitude = "0°N";
		this.longitude = "0°W";
	}
	
	Geoposition(String latitude, String longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GEOPOSITION_ID")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "latitude", nullable = false)
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	@Column(name = "longitude", nullable = false)
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MACHINE_ID", referencedColumnName = "MACHINE_ID")
	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}
}
