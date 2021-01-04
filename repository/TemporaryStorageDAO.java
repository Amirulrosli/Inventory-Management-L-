package com.nep.itn08.repository;


import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import com.nep.itn08.model.Product;
import com.nep.itn08.model.TemporaryStorage;


@Transactional
public interface TemporaryStorageDAO extends CrudRepository<TemporaryStorage,Integer>{

	List<TemporaryStorage> findAllByRid(String productID);

	

	boolean existsByRid(String productID);



	Optional<TemporaryStorage> findByRid(String productID);




}
