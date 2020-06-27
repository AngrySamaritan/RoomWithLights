package com.angrysamaritan.roomwithlights.service;

import com.angrysamaritan.roomwithlights.exceptions.CountryNotFoundException;
import com.angrysamaritan.roomwithlights.model.Country;
import com.angrysamaritan.roomwithlights.repos.CountryRepo;
import org.springframework.stereotype.Service;

@Service
public class CountryService {

    private final CountryRepo countryRepo;

    public CountryService(CountryRepo countryRepo) {
        this.countryRepo = countryRepo;
    }

    public Country getCountry(int id) {
        return countryRepo.findById(id).orElseThrow(() -> new CountryNotFoundException(id));
    }
}
