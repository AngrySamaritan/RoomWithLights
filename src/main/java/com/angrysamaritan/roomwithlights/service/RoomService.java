package com.angrysamaritan.roomwithlights.service;

import com.angrysamaritan.roomwithlights.exceptions.RoomNotFoundException;
import com.angrysamaritan.roomwithlights.model.Country;
import com.angrysamaritan.roomwithlights.model.Room;
import com.angrysamaritan.roomwithlights.repos.RoomRepo;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepo roomRepo;

    public RoomService(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }

    public Room addRoom(String name, Country country) {
        var room = new Room();
        room.setCountry(country);
        room.setName(name);
        return roomRepo.save(room);
    }

    public Room getRoom(int id) {
        return roomRepo.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
    }

    public void switchLight(int roomId) {
        Room room = getRoom(roomId);
        room.setLightOn(!room.isLightOn());
        roomRepo.save(room);
    }
}
