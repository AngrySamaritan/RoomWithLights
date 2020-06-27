package com.angrysamaritan.roomwithlights;

import com.angrysamaritan.roomwithlights.repos.HookRepo;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RoomServiceTests {

    @Autowired
    HookRepo hookRepo;
}
