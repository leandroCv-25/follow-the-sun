package app.machines.model;

import java.io.Serializable;
import java.util.Objects;

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
@Table(name="Tunning")
public class Tunning implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6732458322906715468L;
	
	private Long Id;
	private String dataStart;
	private String dataEnd;
	
	private Controller controller;
	private Machine machine;
	
	Tunning(){
		super();
	}
	
	public Tunning(String dataStart, Controller controller, Machine machine){
		super();
		this.dataStart = dataStart;
		this.controller = controller;
		this.machine = machine;
	}
	
	@Column(name = "start", length = 100, nullable = false, unique = false)
	public String getDataStart() {
		return dataStart;
	}

	public void setDataStart(String dataStart) {
		this.dataStart = dataStart;
	}
	
	@Column(name = "end", length = 100, nullable = true, unique = false)
	public String getDataEnd() {
		return dataEnd;
	}

	public void setDataEnd(String dataEnd) {
		this.dataEnd = dataEnd;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CONTROLLER_ID", referencedColumnName = "ID")
	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MACHINE_ID", referencedColumnName = "MACHINE_ID")
	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tunning other = (Tunning) obj;
		return Objects.equals(Id, other.getId());
	}

}
