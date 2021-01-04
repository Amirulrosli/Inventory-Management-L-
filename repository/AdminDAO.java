package com.nep.itn08.repository;



import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.nep.itn08.model.Admin;


@Transactional
public interface AdminDAO extends CrudRepository<Admin,Integer> {

	boolean existsByUserName(String userName);

	boolean existsByUserPassword(String userPassword);

	boolean existsByRid(String id);

	

	Optional<Admin> findByRid(String search);
	Optional<Admin> findByUserName(String userName);






	
}
