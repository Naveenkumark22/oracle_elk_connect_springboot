package com.poc.demo.service;

import com.poc.demo.model.keys;
import com.poc.demo.repository.keysRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class keysservice {

    @Autowired
    private keysRepo repo;

    public List<keys> getallkeys() {
       // checkResultsFromQuery();
        return repo.getallkeysdata();
    }
    public Optional<keys> getallkeysid() {
        // checkResultsFromQuery();
        String id="1";
//        return  repo.findByClientIda(id);
      return  repo.findById(id);
    }
    public void checkResultsFromQuery() {
        List<keys> users =  repo.getallkeysdata();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("Found " + users.size() + " users.");
            for (keys user : users) {
                System.out.println("User: " + user.getName() + ", Email: " + user.getClientid());
            }
        }
    }

}
