package app.machines.service;

import app.machines.config.Page;
import app.machines.dao.TunningDao;
import app.machines.message.Response;
import app.machines.model.Machine;
import app.machines.model.Tunning;

public class TunningService extends ServiceInterface<Tunning, Long>{
	TunningDao dao;
	
	public TunningService(){
		this.dao = new TunningDao(openEntityManager());
	}
	
	@Override
	public Response save(Tunning tunning) {
		try {
			getTransaction();
			dao.save(tunning);
			getCommit();
			response = getMessageResponse().message(tunning, "Cadastrado com sucesso!", false);
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(tunning, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response update(Tunning tunning) {
		try {
			getTransaction();
			tunning = dao.update(tunning);
			getCommit();
			response = getMessageResponse().message(tunning, "Atualizado com sucesso!", false);
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(tunning, e.getMessage(), true);
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response delete(Tunning tunning) {
		try {
			getTransaction();
			Tunning tunningCadastrado = dao.getById(tunning.getId());
			dao.delete(tunningCadastrado);
			getCommit();
			response = getMessageResponse().message(tunning, "Excluído com sucesso!", false);	
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(tunning, e.getMessage(), true);
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response getById(Long id) {
		Tunning tunning = null;
		try {
			tunning = dao.getById(id);
			response = getMessageResponse().message(tunning, "Registro localizado com sucesso!", false);	
		}catch(Exception e) {
			e.printStackTrace();
			response = getMessageResponse().message(tunning, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response validarDadosFromView(Tunning objeto) {
		return null;
	}
	
	public Page<Tunning> findByMachine(int paginaAtual, int tamanhoPagina, Machine machine) {
		return dao.findAllByMachine(paginaAtual, tamanhoPagina, machine);
	}
	
	public Response getLastConfig(Machine machine) {
		Tunning tunning = null;
		try {
			tunning = dao.lastConfig(machine);
			response = getMessageResponse().message(tunning, "Registro localizado com sucesso!", false);	
		}catch(Exception e) {
			e.printStackTrace();
			response = getMessageResponse().message(tunning, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}
}
