package app.machines.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import app.machines.config.Page;
import app.machines.model.Data;
import app.machines.model.Machine;

public class DataDao extends GenericDAO<Data,Long> {

	public DataDao(EntityManager entityManager) {
		super(entityManager);
	}
	
	public Page<Data> findAllByMachine(Integer page, Integer pageSize, Machine machine){

		List<Data> datas = new ArrayList<Data>();

		Long total = count();

		Integer paginaAtual = (( page - 1 ) * pageSize );

		if (paginaAtual < 0 ) {
			paginaAtual = 0;
		}

		Double totalPaginas = Math.ceil( total.doubleValue() / pageSize.doubleValue());


		TypedQuery<Data> query = getEntityManager().createQuery("SELECT d FROM Data d where d.machine =:machine ORDER BY d.id DESC", Data.class);
		query.setParameter("machine", machine);



		datas = query.setParameter("machine", machine)
				.setFirstResult(paginaAtual)
				.setMaxResults(pageSize)
				.getResultList();

		return getPages(datas, page, pageSize, totalPaginas.intValue(), total.intValue());
	}

}
