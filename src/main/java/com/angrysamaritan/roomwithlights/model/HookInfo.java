package com.angrysamaritan.roomwithlights.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class HookInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @Getter
    @Setter
    private int requestsAmount;

    @Getter
    @Setter
    private boolean prevState;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
