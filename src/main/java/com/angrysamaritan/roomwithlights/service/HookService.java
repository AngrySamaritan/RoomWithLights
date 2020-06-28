package com.angrysamaritan.roomwithlights.service;

import com.angrysamaritan.roomwithlights.model.LongPollInfo;
import com.angrysamaritan.roomwithlights.model.Room;
import com.angrysamaritan.roomwithlights.repos.LongPollRepo;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LongPollService {

    private final LongPollRepo longPollRepo;

    public LongPollService(LongPollRepo longPollRepo) {
        this.longPollRepo = longPollRepo;
    }

    public LongPollInfo getLongPollInfo(int id) throws NotFoundException {
        return longPollRepo.findById(id).orElseThrow(() -> new NotFoundException("Not Found!"));
    }

    private int makeLongPollInfo(Room room, boolean prevState) {
        var longPollInfo = longPollRepo.getLongPollInfo(room.getId());
        if (longPollInfo == null) {
            longPollInfo = new LongPollInfo();
            longPollInfo.setPrevState(prevState);
            longPollInfo.setRoom(room);
            longPollInfo.setRequestsAmount(1);
        } else {
            longPollInfo.setRequestsAmount(longPollInfo.getRequestsAmount() + 1);
        }
        return longPollRepo.save(longPollInfo).getId();
    }

    private void closeLongPoll(int LongPollInfoId) throws NotFoundException {
        var longPollInfo = getLongPollInfo(LongPollInfoId);
        if (longPollInfo.getRequestsAmount() == 1) {
            longPollRepo.delete(longPollInfo);
        } else {
            longPollInfo.setRequestsAmount(longPollInfo.getRequestsAmount() - 1);
        }
    }

    public LongPollInfo getTriggeredLongPoll(Room room) {
        return longPollRepo.getTriggeredLongPoll(room.getId());
    }

    public boolean wait(Room room, int time, boolean prevState, int requestDelay) throws InterruptedException, NotFoundException {
        var longPollInfoId = makeLongPollInfo(room, prevState);
        var timeGone = 0;
        LongPollInfo longPollInfo = getTriggeredLongPoll(room);
        while (longPollInfo == null && timeGone < time) {
            Thread.sleep(requestDelay);
            timeGone += requestDelay;
            longPollInfo = getTriggeredLongPoll(room);
        }
        closeLongPoll(longPollInfoId);
        return longPollInfo != null;
    }
}
