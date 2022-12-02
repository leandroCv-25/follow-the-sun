package app.machines.service;

import app.machines.dao.GeopositionDao;
import app.machines.message.Response;
import app.machines.model.Geoposition;
import app.machines.model.Machine;

public class GeopositionService extends ServiceInterface<Geoposition, Long>{
	GeopositionDao dao;
	
	public GeopositionService(){
		this.dao = new GeopositionDao(openEntityManager());
	}
	
	@Override
	public Response save(Geoposition geoposition) {
		try {
			getTransaction();
			dao.save(geoposition);
			getCommit();
			response = getMessageResponse().message(geoposition, "Cadastrado com sucesso!", false);
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(geoposition, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response update(Geoposition geoposition) {
		try {
			getTransaction();
			geoposition = dao.update(geoposition);
			getCommit();
			response = getMessageResponse().message(geoposition, "Atualizado com sucesso!", false);
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(geoposition, e.getMessage(), true);
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response delete(Geoposition geoposition) {
		try {
			getTransaction();
			Geoposition geopositionCadastrado = dao.getById(geoposition.getId());
			dao.delete(geopositionCadastrado);
			getCommit();
			response = getMessageResponse().message(geoposition, "Excluído com sucesso!", false);	
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(geoposition, e.getMessage(), true);
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response getById(Long id) {
		Geoposition geoposition = null;
		try {
			geoposition = dao.getById(id);
			response = getMessageResponse().message(geoposition, "Registro localizado com sucesso!", false);	
		}catch(Exception e) {
			e.printStackTrace();
			response = getMessageResponse().message(geoposition, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}
	
	public Response getByMachine(Machine machine) {
		Geoposition geoposition = null;	
		try {
			geoposition = dao.getByMachine(machine);
			response = getMessageResponse().message(geoposition, "Registro localizado com sucesso!", false);	
		}catch(Exception e) {
			e.printStackTrace();
			response = getMessageResponse().message(geoposition, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response validarDadosFromView(Geoposition objeto) {
		return null;
	}
}