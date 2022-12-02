package app.machines.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import app.machines.config.Page;
import app.machines.model.Machine;
import app.machines.model.Tunning;

public class TunningDao extends GenericDAO<Tunning,Long> {

	public TunningDao(EntityManager entityManager) {
		super(entityManager);
	}
	
	public Page<Tunning> findAllByMachine(Integer page, Integer pageSize, Machine machine){

		List<Tunning> datas = new ArrayList<Tunning>();

		Long total = count();

		Integer paginaAtual = (( page - 1 ) * pageSize );

		if (paginaAtual < 0 ) {
			paginaAtual = 0;
		}

		Double totalPaginas = Math.ceil( total.doubleValue() / pageSize.doubleValue());


		TypedQuery<Tunning> query = getEntityManager().createQuery("SELECT t FROM Tunning t where t.machine =:machine ORDER BY t.id DESC", Tunning.class);
		query.setParameter("machine", machine);



		datas = query.setParameter("machine", machine)
				.setFirstResult(paginaAtual)
				.setMaxResults(pageSize)
				.getResultList();

		return getPages(datas, page, pageSize, totalPaginas.intValue(), total.intValue());
	}
	
	public Tunning lastConfig(Machine machine) {
		TypedQuery<Tunning> query = getEntityManager().createQuery("SELECT t FROM Tunning t where t.machine =:machine ORDER BY t.id DESC", Tunning.class);
		query.setParameter("machine", machine);
		
		List<Tunning> tunnings = query.setParameter("machine", machine).setFirstResult(0)
				.setMaxResults(1).getResultList();
		if(tunnings.isEmpty()) {
			return null;
		}
		return tunnings.get(0);
	}

}
