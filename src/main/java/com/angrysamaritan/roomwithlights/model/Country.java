package com.angrysamaritan.roomwithlights.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String name;

    @OneToMany(mappedBy = "country")
    private List<Room> rooms;
}
