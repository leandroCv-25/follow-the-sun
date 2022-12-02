package app.machines.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import app.machines.service.errors.CampoRequerido;
import app.machines.service.errors.CampoPassword;
import app.machines.service.errors.CampoEmail;

@Entity
@Table(name="User")
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4175032182252549289L;
	private long id;
	private String name;
	@CampoRequerido(valor=1, mensagem = "O email deve ser informado")
	@CampoEmail(valor=1)
	private String email;
	@CampoRequerido(valor=2, mensagem = "A senha deve ser informada")
	@CampoPassword(valor=2)
	private String password;


	public User() {
		super();
	}

	public User(long id, String name, String email, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
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

	@Column(name = "EMAIL", length = 100, nullable = false, unique = true)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name = "PASSWORD", length = 100, nullable = false, unique = false)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}
}
