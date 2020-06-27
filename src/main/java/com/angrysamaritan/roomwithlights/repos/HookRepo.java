package com.angrysamaritan.roomwithlights.repos;

import com.angrysamaritan.roomwithlights.model.HookInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface HookRepo extends CrudRepository<HookInfo, Integer> {

    @Query("SELECT h FROM HookInfo h JOIN h.room r WHERE r.id = ?1")
    HookInfo getHook(int roomId);

    @Query("SELECT h FROM HookInfo h JOIN h.room r WHERE r.id = ?1 and not r.lightOn = h.prevState")
    HookInfo getTriggeredHook(int roomId);
}
