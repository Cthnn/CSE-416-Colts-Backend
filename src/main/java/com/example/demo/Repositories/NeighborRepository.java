package com.example.demo.Repositories;

import com.example.demo.PersistenceClasses.Neighbor;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeighborRepository extends CrudRepository<Neighbor, String>{}
