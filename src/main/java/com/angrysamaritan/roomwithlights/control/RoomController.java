package com.angrysamaritan.roomwithlights.control;

import com.angrysamaritan.roomwithlights.model.Room;
import com.angrysamaritan.roomwithlights.service.CountryService;
import com.angrysamaritan.roomwithlights.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Controller
public class RoomController {

    public static int DB_REQUEST_INTERVAL = 100;
    private final RoomService roomService;
    private final CountryService countryService;


    public RoomController(RoomService roomService, CountryService countryService) {
        this.roomService = roomService;
        this.countryService = countryService;
    }

    @GetMapping("/room/id{id}")
    public String getRoom(@PathVariable int id, Model model, HttpServletRequest request) {
        Room room = roomService.getRoom(id);
        String ip = request.getRemoteAddr();
        if (countryService.checkCountryAccess(room.getCountry(), ip)) {
            model.addAttribute("room", room);
            return "room";
        } else {
            return "redirect:/country_access_error";
        }
    }

    @GetMapping("/room/create")
    public String getRoomCreatePage(Model model) {
        model.addAttribute("countries", countryService.getAllCountries());
        return "create_room";
    }

    @PostMapping("/room/create")
    public String createRoom(@RequestParam("name") String name, @RequestParam("country_id") int countryId,
                             @RequestParam(value = "is_light_on", required = false) boolean isLightOn, Model model) {
        int id = roomService.addRoom(name, countryService.getCountry(countryId), isLightOn).getId();
        return "redirect:/room/id" + id;
    }

    @PostMapping("room/id{id}/switch")
    @ResponseBody
    public String switchLight(@PathVariable int id) {
        return String.valueOf(roomService.switchLight(id));
    }

    @ResponseBody
    @GetMapping("/room/long_poll")
    public DeferredResult<String> lightWebHook(@RequestParam("id") int id, @RequestParam("last_state") boolean lastState,
                                               @RequestParam("time") int time) {
        DeferredResult<String> deferredResult = new DeferredResult<>();
        CompletableFuture.runAsync(
                () -> {
                    try {
                        var room = roomService.getRoom(id);
                        int timeGone = 0;
                        while (room.isLightOn() == lastState && timeGone < time) {
                            TimeUnit.MILLISECONDS.sleep(DB_REQUEST_INTERVAL);
                            timeGone += DB_REQUEST_INTERVAL;
                            room = roomService.getRoom(id);
                        }
                        deferredResult.setResult(String.valueOf(room.isLightOn()));
                    } catch (InterruptedException ignored) {
                    }
                }
        );
        return deferredResult;
    }
}
