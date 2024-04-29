package com.poc.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Table(name="samplepoc")
@Entity
public class keys {
    @Id
    @Column(name = "CLIENTID")
    private String clientid;
    @Column(name = "NAME")
    private String name;
    @Column(name = "enddate")
    private String enddt;

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnddt() {
        return enddt;
    }

    public void setEnddt(String enddt) {
        this.enddt = enddt;
    }



}

