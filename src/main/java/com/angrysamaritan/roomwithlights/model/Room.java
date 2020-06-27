package com.angrysamaritan.roomwithlights.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @ManyToOne
    @JoinColumn(name = "country_id")
    @Getter
    @Setter
    private Country country;

    @Getter
    @Setter
    private boolean lightOn;

    @Getter
    @Setter
    private String name;
}
