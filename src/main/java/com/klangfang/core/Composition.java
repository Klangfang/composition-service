package com.klangfang.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Composition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sounds;

    public String getSounds() {
        return sounds;
    }

    public Long getId() {
        return id;
    }
}
