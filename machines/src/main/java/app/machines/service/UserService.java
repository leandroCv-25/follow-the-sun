package app.machines.service;

import java.util.ArrayList;

import app.machines.dao.UserDao;
import app.machines.message.Response;
import app.machines.model.User;
import app.machines.service.errors.ErrorsData;
import app.machines.service.errors.TestarCampo;

public class UserService extends ServiceInterface<User, Long> {

	private UserDao dao;

	public UserService() {
		dao = new UserDao(openEntityManager());
	}

	public Response save(User user) {
		try {
			getTransaction();
			dao.save(user);
			getCommit();
			response = getMessageResponse().message(user, "Cadastrado com sucesso!", false);
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(user, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}

	public Response update(User user) {
		try {
			getTransaction();
			user = dao.update(user);
			getCommit();
			response = getMessageResponse().message(user, "Atualizado com sucesso!", false);
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(user, e.getMessage(), true);
		} finally {
			closeEntityManager();
		}
		return response;
	}

	public Response delete(User user) {
		try {
			getTransaction();
			User userCadastrado = dao.getById(user.getId());
			dao.delete(userCadastrado);
			getCommit();
			response = getMessageResponse().message(user, "Excluído com sucesso!", false);	
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(user, e.getMessage(), true);
		} finally {
			closeEntityManager();
		}
		return response;
	}

	public Response getById(Long id) {
		User user = null;
		try {
			user = dao.getById(id);
			response = getMessageResponse().message(user, "Registro localizado com sucesso!", false);	
		}catch(Exception e) {
			e.printStackTrace();
			response = getMessageResponse().message(user, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}


	public Response login(String email, String password) {
		User conference = null;
		User user = null;

		try {
			conference = dao.getUserByEmail(email);

			if(conference != null && conference.getPassword().compareTo(password)== 0) {
				response = getMessageResponse().message(conference, "Logado com sucesso!", false);
			} else {
				response = getMessageResponse().message(user, "Email ou senha parecem incorretos", true);
			}
		}catch(Exception e) {
			e.printStackTrace();
			if(conference == null) {
				response = getMessageResponse().message(user, "Email ou senha parecem incorretos", true);
			}else {
				response = getMessageResponse().message(conference, e.getMessage(), true);					
			}
		} finally {
			closeEntityManager();
		}
		return response;

	}

	@Override
	public Response validarDadosFromView(User user) {
		errorsData = new ArrayList<ErrorsData>();
		errorsData = TestarCampo.validarCampoRequerido(user);
		return returnErrorOrNot();
	}


}
