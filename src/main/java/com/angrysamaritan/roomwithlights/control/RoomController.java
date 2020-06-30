package com.angrysamaritan.roomwithlights.control;

import com.angrysamaritan.roomwithlights.model.Room;
import com.angrysamaritan.roomwithlights.service.CountryService;
import com.angrysamaritan.roomwithlights.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Controller
public class RoomController {

    public static int DB_REQUEST_INTERVAL = 500;
    private final RoomService roomService;
    private final CountryService countryService;


    public RoomController(RoomService roomService, CountryService countryService) {
        this.roomService = roomService;
        this.countryService = countryService;
    }

    @GetMapping("/room/id{id}")
    public String getRoom(@PathVariable int id, Model model, HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {
        Room room = roomService.getRoom(id);
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        if (countryService.checkCountryAccess(room.getCountry(), ip)) {
            model.addAttribute("room", room);
            return "room";
        } else {
            redirectAttributes.addAttribute("access_error", true);
            return "redirect:/room_list";
        }
    }

    @GetMapping("/")
    public String getIndex() {
        return "redirect:/room_list";
    }

    @GetMapping("/room_list")
    public String getMain(@RequestParam(value = "access_error", required = false) boolean error, Model model) {
        model.addAttribute("access_error", error);
        model.addAttribute("rooms", roomService.getAllRooms());
        return "index";
    }

    @GetMapping("/room/create")
    public String getRoomCreatePage(Model model) {
        model.addAttribute("countries", countryService.getAllCountries());
        return "create_room";
    }

    @PostMapping("/room/create")
    public String createRoom(@RequestParam("name") String name, @RequestParam("country_id") int countryId,
                             @RequestParam(value = "is_light_on", required = false) boolean isLightOn) {
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
                        LocalDateTime endTime = LocalDateTime.now().plusNanos(time * 1000);
                        while (room.isLightOn() == lastState &&  LocalDateTime.now().isBefore(endTime)) {
                            TimeUnit.MILLISECONDS.sleep(DB_REQUEST_INTERVAL);
                            room = roomService.getRoom(id);
                        }
                        deferredResult.setResult(String.valueOf(room.isLightOn()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        return deferredResult;
    }
}
