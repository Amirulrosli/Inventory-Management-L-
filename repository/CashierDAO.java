package com.nep.itn08.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.nep.itn08.model.CashierStorage;



@Transactional
public interface CashierDAO extends CrudRepository <CashierStorage,Integer> {

	Optional<CashierStorage> findByRid(String productID);

	boolean existsByRid(String productID);

	



}
