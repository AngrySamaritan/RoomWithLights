package com.angrysamaritan.roomwithlights.control;

import com.angrysamaritan.roomwithlights.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final RoomService roomService;

    public MainController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "index";
    }
}
