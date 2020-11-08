package com.example.demo.Repositories;

import com.example.demo.PersistenceClasses.Job;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends CrudRepository<Job, Integer>{}
