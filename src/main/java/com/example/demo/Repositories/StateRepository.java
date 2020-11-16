package com.example.demo.Repositories;

import com.example.demo.PersistenceClasses.State;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends CrudRepository<State, Integer>{}
