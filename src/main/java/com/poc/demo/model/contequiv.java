package com.poc.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Table(name="contequiv")
@Entity
public class contequiv {

    @Id
    @Column(name="ADMIN_CLIENT_ID")
    private  String adminclientid;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="END_DT")
    private Timestamp end_dt;

    public Timestamp getEnd_dt() {
        return end_dt;
    }

    public void setEnd_dt(Timestamp end_dt) {
        this.end_dt = end_dt;
    }

    public String getAdminclientid() {
        return adminclientid;
    }

    public void setAdminclientid(String adminclientid) {
        this.adminclientid = adminclientid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
