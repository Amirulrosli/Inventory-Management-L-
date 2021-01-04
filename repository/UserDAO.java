package com.nep.itn08.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.nep.itn08.model.Admin;
import com.nep.itn08.model.User;

@Transactional
public interface UserDAO extends CrudRepository<User,Integer> {

	

	boolean existsByUserName(String username);

	Optional<User> findByRid(String search);
	Optional<User> findByUserName(String userName);

	boolean existsByUserPassword(String userPassword);

}
