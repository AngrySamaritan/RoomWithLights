package com.angrysamaritan.roomwithlights;

import com.angrysamaritan.roomwithlights.service.CountryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CountryServiceTest {


    @Autowired
    private CountryService countryService;

    @Test
    public void checkCountryAccessTest() {
        Assert.assertTrue(countryService.checkCountryAccess(countryService.getCountry(36), "93.84.218.218"));
        Assert.assertFalse(countryService.checkCountryAccess(countryService.getCountry(1), "93.84.218.218"));
    }
}
