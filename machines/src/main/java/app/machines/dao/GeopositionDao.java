package app.machines.dao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import app.machines.model.Geoposition;
import app.machines.model.Machine;

public class GeopositionDao extends GenericDAO<Geoposition, Long> {

	public GeopositionDao(EntityManager entityManager) {
		super(entityManager);
	}

	public Geoposition getByMachine(Machine machine) {
		TypedQuery<Geoposition> query = getEntityManager().createQuery("SELECT g FROM Geoposition g where g.machine =:machine", Geoposition.class);
		query.setParameter("machine", machine);

		Geoposition geoposition = query.getSingleResult();

		return geoposition;
	}

}
