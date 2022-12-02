package app.machines.service;

import java.util.ArrayList;

import app.machines.config.Page;
import app.machines.dao.ControllerDao;
import app.machines.message.Response;
import app.machines.model.Controller;
import app.machines.service.errors.ErrorsData;
import app.machines.service.errors.TestarCampo;


public class ControllerService extends ServiceInterface<Controller, Long> {

	private ControllerDao dao;

	public ControllerService() {
		dao = new ControllerDao(openEntityManager());
	}

	public Response save(Controller controller) {
		try {
			getTransaction();
			dao.save(controller);
			getCommit();
			response = getMessageResponse().message(controller, "Cadastrado com sucesso!", false);
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(controller, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}

	public Response update(Controller controller) {
		try {
			getTransaction();
			controller = dao.update(controller);
			getCommit();
			response = getMessageResponse().message(controller, "Atualizado com sucesso!", false);
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(controller, e.getMessage(), true);
		} finally {
			closeEntityManager();
		}
		return response;
	}

	public Response delete(Controller controller) {
		try {
			getTransaction();
			dao.delete(controller);
			getCommit();
			response = getMessageResponse().message(controller, "Excluído com sucesso!", false);
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(controller, e.getMessage(), true);
		} finally {
			closeEntityManager();
		}
		return response;
	}

	public Response getById(Long id) {
		Controller controller = null;
		try {
			controller = dao.getById(id);
			response = getMessageResponse().message(controller, "Registro localizado com sucesso!", false);	
		}catch(Exception e) {
			e.printStackTrace();
			response = getMessageResponse().message(controller, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}
	

	public Page<Controller> findAll(int paginaAtual, int tamanhoPagina) {
		return dao.findAll(paginaAtual, tamanhoPagina);
	}

	public Page<Controller> findAll(int paginaAtual, int tamanhoPagina, String text) {
		return dao.findAllBySearch(paginaAtual, tamanhoPagina, text);
	}

	@Override
	public Response validarDadosFromView(Controller objeto) {
		errorsData = new ArrayList<ErrorsData>();
		errorsData = TestarCampo.validarCampoRequerido(objeto);
		return returnErrorOrNot();
	}
}
