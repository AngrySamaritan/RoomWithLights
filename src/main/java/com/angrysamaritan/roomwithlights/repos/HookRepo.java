package com.angrysamaritan.roomwithlights.repos;

import com.angrysamaritan.roomwithlights.model.LongPollInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface LongPollRepo extends CrudRepository<LongPollInfo, Integer> {

    @Query("SELECT l FROM LongPollInfo l JOIN l.room r WHERE r.id = ?1")
    LongPollInfo getLongPollInfo(int roomId);

    @Query("SELECT l FROM LongPollInfo l JOIN l.room r WHERE r.id = ?1 and not r.lightOn = l.prevState")
    LongPollInfo getTriggeredLongPoll(int roomId);
}
