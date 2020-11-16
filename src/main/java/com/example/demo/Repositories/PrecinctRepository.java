package com.example.demo.Repositories;

import com.example.demo.PersistenceClasses.Precinct;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrecinctRepository extends CrudRepository<Precinct, String>{}
