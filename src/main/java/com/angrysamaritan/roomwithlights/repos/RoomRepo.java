package com.angrysamaritan.roomwithlights.repos;

import com.angrysamaritan.roomwithlights.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepo extends CrudRepository<Room, Integer> {
}
