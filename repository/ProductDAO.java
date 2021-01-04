package com.nep.itn08.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.nep.itn08.model.Product;

@Transactional
public interface ProductDAO extends CrudRepository<Product,Integer>{


	boolean existsById(String id);

	boolean existsByBrand(String brand);

	List<Product> findByBrand(String brand);



	Optional<Product> findByRid(String search);

	boolean existsByRid(String id);

	Product findAllByRid(String id);

	List<Product> findAll(Sort by);

	Product deleteByRid(String search);

	
	
}

