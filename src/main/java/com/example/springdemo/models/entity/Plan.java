package com.example.springdemo.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "plan",
        indexes = {@Index(name = "idx_hashcode", columnList = "hashcode",unique = true)})
@lombok.Data
public class Plan implements Serializable {
    private static final long serialVersionUID = 8436097833452420298L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String hashcode;

    @ManyToOne
    private City city;

    @Override
    public boolean equals(Object o){
        if (o instanceof Plan){
            Plan plan = (Plan) o;
            return plan.getId() == this.id;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return (int)this.id;
    }
}
