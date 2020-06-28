package com.angrysamaritan.roomwithlights.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;

@Entity
@Synchronize({"id", "request_amount", "prev_state", "room_id"})
public class LongPollInfo {

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
