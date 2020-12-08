package com.example.demo.Repositories;

import com.example.demo.PersistenceClasses.Districting;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictingRepository extends CrudRepository<Districting, Integer>{}
