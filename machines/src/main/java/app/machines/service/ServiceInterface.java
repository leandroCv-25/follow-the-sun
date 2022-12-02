package app.machines.service;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import app.machines.message.MessageResponse;
import app.machines.message.ModelResponse;
import app.machines.message.Response;
import app.machines.persistence.ConexaoBancoDados;
import app.machines.service.errors.ErrorsData;

public abstract class ServiceInterface<T, ID>{

	@PersistenceContext(unitName="machines")
	private EntityManager entityManager;
	protected List<ErrorsData> errorsData;
	protected Response response;
	protected MessageResponse<ErrorsData> errorData;
	protected MessageResponse<T> messageResponse;
	protected ModelResponse<T> modelResponse;


	public EntityManager openEntityManager() {

		if (Objects.isNull(entityManager)) {
			entityManager = ConexaoBancoDados.getConexao().getEntityManager(); 
		}
		return entityManager;
	}

	public void getTransaction() {
		entityManager.getTransaction().begin();
	}

	public void getCommit() {
		entityManager.getTransaction().commit();
	}

	public boolean getAtivo() {
		return entityManager.getTransaction().isActive();
	}

	public void getRollback() {
		entityManager.getTransaction().rollback();
	}


	public void closeEntityManager() {
		entityManager.close();
	}


	public abstract Response save(T entity);

	public abstract Response update(T entity);

	public abstract Response delete(T entity);

	public abstract Response getById(ID id);

	public abstract Response validarDadosFromView(T objeto);


	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public MessageResponse<T> getMessageResponse() {
		return new MessageResponse<T>();
	}

	public void setMessageResponse(MessageResponse<T> messageResponse) {
		this.messageResponse = messageResponse;
	}

	public ModelResponse<T> getModelResponse() {
		return new ModelResponse<T>();
	}

	public void setModelResponse(ModelResponse<T> modelResponse) {
		this.modelResponse = modelResponse;
	}

	public Response returnErrorOrNot() {
		if (errorsData.size() > 0) {
			response = getErrorData().message(errorsData, "", true);
		} else {
			response = getErrorData().message(errorsData, "", false);
		}
		return response;
	}

	public MessageResponse<ErrorsData> getErrorData() {
		return new MessageResponse<ErrorsData>();
	}

	public void setErrorData(MessageResponse<ErrorsData> errorData) {
		this.errorData = errorData;
	}


}
