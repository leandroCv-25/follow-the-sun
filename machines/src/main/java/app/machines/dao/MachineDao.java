package app.machines.dao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import app.machines.model.Machine;


public class MachineDao extends GenericDAO<Machine, Long> {

	public MachineDao(EntityManager entityManager) {
		super(entityManager);
	}
	
	public Machine getMachineByMac(String mac){

		TypedQuery<Machine> query = getEntityManager().createQuery("SELECT m FROM Machine m where m.mac =:mac", Machine.class);
		query.setParameter("mac", mac);

		Machine machine = query.getSingleResult();

		return machine;
	}

}
