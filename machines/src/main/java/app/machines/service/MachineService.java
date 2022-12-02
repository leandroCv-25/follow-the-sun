package app.machines.service;

import app.machines.dao.MachineDao;
import app.machines.message.Response;
import app.machines.model.Machine;


public class MachineService extends ServiceInterface<Machine, Long> {

	MachineDao dao;
	
	public MachineService(){
		this.dao = new MachineDao(openEntityManager());
	}
	
	@Override
	public Response save(Machine machine) {
		try {
			getTransaction();
			dao.save(machine);
			getCommit();
			response = getMessageResponse().message(machine, "Cadastrado com sucesso!", false);
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(machine, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response update(Machine machine) {
		try {
			getTransaction();
			machine = dao.update(machine);
			getCommit();
			response = getMessageResponse().message(machine, "Atualizado com sucesso!", false);
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(machine, e.getMessage(), true);
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response delete(Machine machine) {
		try {
			getTransaction();
			Machine machineCadastrado = dao.getById(machine.getId());
			dao.delete(machineCadastrado);
			getCommit();
			response = getMessageResponse().message(machine, "Excluído com sucesso!", false);	
		} catch(Exception e) {
			e.printStackTrace();
			if (getAtivo()) {
				getRollback();
			}
			response = getMessageResponse().message(machine, e.getMessage(), true);
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response getById(Long id) {
		Machine machine = null;
		try {
			machine = dao.getById(id);
			response = getMessageResponse().message(machine, "Registro localizado com sucesso!", false);	
		}catch(Exception e) {
			e.printStackTrace();
			response = getMessageResponse().message(machine, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}
	
	public Response getMachineByMac(String mac) {
		Machine machine = null;
		try {
			machine = dao.getMachineByMac(mac);
			response = getMessageResponse().message(machine, "Registro localizado com sucesso!", false);	
		}catch(Exception e) {
			e.printStackTrace();
			response = getMessageResponse().message(machine, e.getMessage(), true);	
		} finally {
			closeEntityManager();
		}
		return response;
	}

	@Override
	public Response validarDadosFromView(Machine objeto) {
		return null;
	}

}
