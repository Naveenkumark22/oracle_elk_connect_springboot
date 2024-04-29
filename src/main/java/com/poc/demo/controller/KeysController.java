package com.poc.demo.controller;

import com.poc.demo.model.contequiv;
import com.poc.demo.model.keys;
import com.poc.demo.service.contequivService;
import com.poc.demo.service.keysservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/poc")
public class KeysController {
    @Autowired
    private keysservice service;

@Autowired
    private contequivService contequivservice;

    @GetMapping("/getkeys")
public  List<keys> getallkeys()
{
    return  service.getallkeys();
}
    @GetMapping("/getkeysbyid")
    public Optional<keys> getallkeysbyid()
    {
        return  service.getallkeysid();
    }
    @GetMapping("/getcontequivkeys")
    public  List<contequiv> getallcontequivkeys()
    {
        return  contequivservice.getallcontequiv();

    }
    @GetMapping("/fetchdata")
    public List<String> fetchData() throws ExecutionException, InterruptedException, IOException {
        return contequivservice.fetchDataConcurrently();
    }
    @GetMapping("/fetchdata1")
    public List<String> fetchData1() throws ExecutionException, InterruptedException, IOException {
        return contequivservice.fetchDataConcurrently1();
    }
    @GetMapping("/fetchelkdata")
    public Object fetchElkData() throws IOException {
        return contequivservice.fetchelkdata() ;
    }
}
