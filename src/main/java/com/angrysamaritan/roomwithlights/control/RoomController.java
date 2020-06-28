package com.angrysamaritan.roomwithlights.control;

import com.angrysamaritan.roomwithlights.model.Room;
import com.angrysamaritan.roomwithlights.service.CountryService;
import com.angrysamaritan.roomwithlights.service.HookService;
import com.angrysamaritan.roomwithlights.service.RoomService;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RoomController {

    private static final int CHECK_DELAY = 100;
    private final RoomService roomService;
    private final CountryService countryService;
    private final HookService hookService;

    public RoomController(RoomService roomService, CountryService countryService, HookService hookService) {
        this.roomService = roomService;
        this.countryService = countryService;
        this.hookService = hookService;
    }

    @GetMapping("/room/id{id}")
    public String getRoom(@PathVariable int id, Model model) {
        Room room = roomService.getRoom(id);
        model.addAttribute("room", room);
        return "room";
    }

    @GetMapping("/room/create")
    public String getRoomCreatePage() {
        return null;
    }

    @PostMapping("/room/create")
    public String createRoom(@RequestParam("name") String name, @RequestParam("country_id") int countryId) {
        roomService.addRoom(name, countryService.getCountry(countryId));
        return null;
    }

    @PostMapping("room/{id}/switch")
    @ResponseBody
    public String switchLight(@PathVariable int id) {
        roomService.switchLight(id);
        return "All right";
    }

    @ResponseBody
    @GetMapping("/room/hook")
    public String lightWebHook(@RequestParam("id") int id, @RequestParam("last_state") boolean lastState,
                               @RequestParam("time") int time) throws InterruptedException, NotFoundException {
        boolean currentState;
        Room room = roomService.getRoom(id);
        boolean hasChanged;
        if (lastState == room.isLightOn()) {
            hasChanged = hookService.wait(room, time, lastState, CHECK_DELAY);
            if (hasChanged) {
                currentState = !lastState;
            } else {
                currentState = lastState;
            }
        } else {
            currentState = !room.isLightOn();
        }
        return String.valueOf(currentState);
    }
}
