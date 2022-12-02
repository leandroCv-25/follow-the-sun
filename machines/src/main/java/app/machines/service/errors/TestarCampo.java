package app.machines.service.errors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestarCampo {

	public static List<ErrorsData> validarCampoRequerido(final Object objeto) {

		List<ErrorsData> errorsData = new ArrayList<>();

		final Class<?> classe = objeto.getClass(); 
		final Field[] field =  classe.getDeclaredFields();

		for (int i = 0; i < field.length; i++) {
			
			if (field[i].isAnnotationPresent(CampoEmail.class)) {

				CampoEmail campoEmail = field[i].getDeclaredAnnotation(CampoEmail.class);

				int valor = campoEmail.valor();
				String mensagemErro = campoEmail.mensagem();

				field[i].setAccessible(true);

				try {
					Object campo = field[i].get(objeto);
					if (conteudoInformadoEmail(campo) == true) {
						ErrorsData erro = new ErrorsData();
						erro.setShowMensagemErro(mensagemErro);
						erro.setNumeroCampo(valor);
						errorsData.add(erro);
					}

				} catch(IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			if (field[i].isAnnotationPresent(CampoName.class)) {

				CampoName campoName = field[i].getDeclaredAnnotation(CampoName.class);

				int valor = campoName.valor();
				String mensagemErro = campoName.mensagem();

				field[i].setAccessible(true);

				try {
					Object campo = field[i].get(objeto);
					if (conteudoInformadoName(campo) == true) {
						ErrorsData erro = new ErrorsData();
						erro.setShowMensagemErro(mensagemErro);
						erro.setNumeroCampo(valor);
						errorsData.add(erro);
					}

				} catch(IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			if (field[i].isAnnotationPresent(CampoPassword.class)) {

				CampoPassword campoPassword = field[i].getDeclaredAnnotation(CampoPassword.class);

				int valor = campoPassword.valor();
				String mensagemErro = campoPassword.mensagem();

				field[i].setAccessible(true);

				try {
					Object campo = field[i].get(objeto);
					if (conteudoInformadoPassword(campo) == true) {
						ErrorsData erro = new ErrorsData();
						erro.setShowMensagemErro(mensagemErro);
						erro.setNumeroCampo(valor);
						errorsData.add(erro);
					}

				} catch(IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			if (field[i].isAnnotationPresent(CampoRequerido.class)) {

				CampoRequerido campoRequerido = field[i].getDeclaredAnnotation(CampoRequerido.class);

				int valor = campoRequerido.valor();
				String mensagemErro = campoRequerido.mensagem();

				field[i].setAccessible(true);

				try {
					Object campo = field[i].get(objeto);
					if (conteudoInformadoNoCampo(campo) == true) {
						ErrorsData erro = new ErrorsData();
						erro.setShowMensagemErro(mensagemErro);
						erro.setNumeroCampo(valor);
						errorsData.add(erro);
					}

				} catch(IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return errorsData;



	}

	private static boolean conteudoInformadoNoCampo(Object objeto){

		if (Objects.isNull(objeto)) {
			return true;
		}

		if (objeto instanceof String ) {
			String campo = (String) objeto;
			return "".equals(campo.trim()) ? true : false;
		}

		if (objeto instanceof Integer) {
			Integer campo = (Integer) objeto;
			return campo == 0 ? true : false;
		}

		if (objeto instanceof Long) {
			Long campo = (Long) objeto;
			return campo == 0L ? true : false;
		}

		return false;

	}

	
	private static boolean conteudoInformadoEmail(Object objeto){

		if (Objects.isNull(objeto)) {
			return true;
		}

		if (objeto instanceof String ) {
			String campo = (String) objeto;
			//return !campo.contains("@");
			String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
			        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
			 return !campo.matches(regexPattern);
		}

		return false;

	}
	
	private static boolean conteudoInformadoName(Object objeto){

		if (Objects.isNull(objeto)) {
			return true;
		}

		if (objeto instanceof String ) {
			String campo = (String) objeto;
			return campo.length()<6;
		}

		return false;

	}
	
	private static boolean conteudoInformadoPassword(Object objeto){

		if (Objects.isNull(objeto)) {
			return true;
		}

		if (objeto instanceof String ) {
			String campo = (String) objeto;
			return campo.length()<8;
		}

		return false;

	}



}
