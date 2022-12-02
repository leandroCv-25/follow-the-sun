package app.machines.service;

import app.machines.config.Page;
import app.machines.dao.DataDao;
import app.machines.message.Response;
import app.machines.model.Data;
import app.machines.model.Machine;

public class DataService extends ServiceInterface<Data, Long>{

	DataDao dao;
	
	public DataService(){
		this.dao = new DataDao(openEntityManager());
	}
	
	@Override
	public Response save(Data data) {
		try {
			getTransaction();
			dao.save(data);
			getCommit();
			response = getMessageResponse().message(data, "Cadastrado com sucesso!", false);
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(data, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response update(Data data) {
		try {
			getTransaction();
			data = dao.update(data);
			getCommit();
			response = getMessageResponse().message(data, "Atualizado com sucesso!", false);
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(data, e.getMessage(), true);
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response delete(Data data) {
		try {
			getTransaction();
			Data dataCadastrado = dao.getById(data.getId());
			dao.delete(dataCadastrado);
			getCommit();
			response = getMessageResponse().message(data, "Excluído com sucesso!", false);	
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(data, e.getMessage(), true);
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response getById(Long id) {
		Data data = null;
		try {
			data = dao.getById(id);
			response = getMessageResponse().message(data, "Registro localizado com sucesso!", false);	
		}catch(Exception e) {
			e.printStackTrace();
			response = getMessageResponse().message(data, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response validarDadosFromView(Data objeto) {
		return null;
	}
	
	public Page<Data> findByMachine(int paginaAtual, int tamanhoPagina, Machine machine) {
		return dao.findAllByMachine(paginaAtual, tamanhoPagina, machine);
	}

}
