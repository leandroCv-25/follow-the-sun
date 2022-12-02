package app.machines.service.errors;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface CampoRequerido {
	
	public int valor() default 0;
	
	public String mensagem() default "Campo obrigatório";

}
