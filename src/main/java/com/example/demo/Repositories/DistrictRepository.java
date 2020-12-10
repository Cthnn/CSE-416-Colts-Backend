package com.example.demo.Repositories;

import com.example.demo.PersistenceClasses.District;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends CrudRepository<District, Integer>{}
