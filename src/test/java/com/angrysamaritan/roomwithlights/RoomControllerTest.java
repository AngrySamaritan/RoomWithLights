package com.angrysamaritan.roomwithlights;

import com.angrysamaritan.roomwithlights.control.RoomController;
import com.angrysamaritan.roomwithlights.model.Room;
import com.angrysamaritan.roomwithlights.service.RoomService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class RoomControllerTest {

    @Autowired
    RoomController roomController;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RoomService roomService;

    @Test
    public void testLongPollingTriggered() throws Exception {
        Room room = roomService.getRoom(1);

        MvcResult result = mockMvc.perform(get("/room/long_poll").param("id", String.valueOf(room.getId()))
                .param("time", "5")
                .param("last_state", String.valueOf(room.isLightOn()))).andReturn();

        new Thread(() -> {
            try {
                Thread.sleep(new Random().nextInt(3000) + 500);
                mockMvc.perform(post("/room/id1/switch").with(csrf()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


        mockMvc
                .perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().string(String.valueOf(!room.isLightOn())));

    }


    @Test
    public void testLongPollingExpired() throws Exception {
        Room room = roomService.getRoom(1);

        MvcResult result = mockMvc.perform(get("/room/long_poll").param("id", String.valueOf(room.getId()))
                .param("time", "5")
                .param("last_state", String.valueOf(room.isLightOn()))).andReturn();

        mockMvc
                .perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().string(String.valueOf(room.isLightOn())));
    }

    @Test
    public void switchLightTestWithToken() throws Exception {
        mockMvc
                .perform(post("/room/id1/switch").with(csrf()))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void redirectIndexTest() throws Exception {
        mockMvc
                .perform(get("/"))
                .andDo(print())
                .andExpect(status().is(302));
    }

    @Test
    public void roomListTest() throws Exception {
        mockMvc
                .perform(get("/room_list"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void switchLightTestNoToken() throws Exception {
        mockMvc
                .perform(post("/room/id1/switch"))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void addRoomTestWithToken() throws Exception {
        mockMvc
                .perform(post("/room/create").param("name", "TestRooooma")
                        .param("country_id", "36").with(csrf()))
                .andExpect(status().is(302));
    }

    @Test
    public void addRoomTestWithoutToken() throws Exception {
        mockMvc
                .perform(post("/room/create").param("name", "TestRooooma")
                        .param("country_id", "36"))
                .andExpect(status().is(403));
    }
}
