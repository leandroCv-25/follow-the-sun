package app.machines.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import app.machines.model.Controller;
import app.machines.config.Page;


public class ControllerDao extends GenericDAO<Controller, Long> {

	public ControllerDao(EntityManager entityManager) {
		super(entityManager);
	}


	public Page<Controller> findAllBySearch(Integer page, Integer pageSize, String name){

		List<Controller> controllers = new ArrayList<Controller>();

		Long total = count();

		Integer paginaAtual = (( page - 1 ) * pageSize );

		if (paginaAtual < 0 ) {
			paginaAtual = 0;
		}

		Double totalPaginas = Math.ceil( total.doubleValue() / pageSize.doubleValue());


		TypedQuery<Controller> query = getEntityManager().createQuery("SELECT c FROM Controller c where c.name =:name", Controller.class);
		query.setParameter("name", name);



		controllers = query.setParameter("name", name)
				.setFirstResult(paginaAtual)
				.setMaxResults(pageSize)
				.getResultList();

		return getPages(controllers, page, pageSize, totalPaginas.intValue(), total.intValue());
	}

}
