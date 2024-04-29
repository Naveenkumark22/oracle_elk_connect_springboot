package com.poc.demo.repository;

import com.poc.demo.model.keys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface keysRepo extends JpaRepository<keys,String> {

    @Query("select e from keys e" )
    List<keys> getallkeysdata();

//    @Query("select e from keys e where e.clientid :clientId" )
//    keys findByClientIda(String clientId);


}
