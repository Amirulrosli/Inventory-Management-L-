package com.nep.itn08.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.nep.itn08.model.Total;

@Transactional
public interface TotalDAO extends CrudRepository<Total,Integer> {



}
