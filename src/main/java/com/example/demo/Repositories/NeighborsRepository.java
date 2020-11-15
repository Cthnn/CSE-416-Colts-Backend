package com.example.demo.Repositories;

import com.example.demo.PersistenceClasses.Neighbors;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeighborsRepository extends CrudRepository<Neighbors, Integer>{}
