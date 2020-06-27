package com.angrysamaritan.roomwithlights.service;

import com.angrysamaritan.roomwithlights.model.HookInfo;
import com.angrysamaritan.roomwithlights.model.Room;
import com.angrysamaritan.roomwithlights.repos.HookRepo;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class HookService {

    private final HookRepo hookRepo;

    public HookService(HookRepo hookRepo) {
        this.hookRepo = hookRepo;
    }

    public HookInfo getHook(int id) throws NotFoundException {
        return hookRepo.findById(id).orElseThrow(() -> new NotFoundException("Not Found!"));
    }

    private int makeHookInfo(Room room, boolean prevState) {
        var hookInfo = hookRepo.getHook(room.getId());
        if (hookInfo == null) {
            hookInfo = new HookInfo();
            hookInfo.setPrevState(prevState);
            hookInfo.setRoom(room);
            hookInfo.setRequestsAmount(1);
        } else {
            hookInfo.setRequestsAmount(hookInfo.getRequestsAmount() + 1);
        }
        return hookRepo.save(hookInfo).getId();
    }

    private void closeHook(int hookId) throws NotFoundException {
        var hook = getHook(hookId);
        if (hook.getRequestsAmount() == 1) {
            hookRepo.delete(hook);
        } else {
            hook.setRequestsAmount(hook.getRequestsAmount() - 1);
        }
    }

    public HookInfo getTriggeredHook(Room room) {
        return hookRepo.getTriggeredHook(room.getId());
    }

    public boolean wait(Room room, int time, boolean prevState, int requestDelay) throws InterruptedException, NotFoundException {
        var hookId = makeHookInfo(room, prevState);
        var timeGone = 0;
        HookInfo hook = getTriggeredHook(room);
        while (hook == null && timeGone < time) {
            Thread.sleep(requestDelay);
            timeGone += requestDelay;
            hook = getTriggeredHook(room);
        }
        closeHook(hookId);
        return hook != null;
    }
}
