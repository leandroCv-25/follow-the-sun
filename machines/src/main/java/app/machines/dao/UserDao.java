package app.machines.dao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import app.machines.model.User;

public class UserDao extends GenericDAO<User, Long>{

	public UserDao(EntityManager entityManager) {
		super(entityManager);
	}
	
	public User getUserByEmail(String email){

		TypedQuery<User> query = getEntityManager().createQuery("SELECT u FROM User u where u.email =:email", User.class);
		query.setParameter("email", email);

		User user = query.getSingleResult();

		return user;
	}

}
