package com.angrysamaritan.roomwithlights.service;

import com.angrysamaritan.roomwithlights.exceptions.CountryNotFoundException;
import com.angrysamaritan.roomwithlights.model.Country;
import com.angrysamaritan.roomwithlights.repos.CountryRepo;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CountryService {

    private final OkHttpClient httpClient = new OkHttpClient();

    @Value("${side-api.check-ip-url}")
    private String ipCheckUrl;

    private final CountryRepo countryRepo;

    public CountryService(CountryRepo countryRepo) {
        this.countryRepo = countryRepo;
    }

    public Iterable<Country> getAllCountries() {
        return countryRepo.findAll();
    }

    public Country getCountry(int id) {
        return countryRepo.findById(id).orElseThrow(() -> new CountryNotFoundException(id));
    }

    public boolean checkCountryAccess(Country country, String ipAddress) {
        boolean result = false;
        Request request = new Request.Builder()
                .url(String.format(ipCheckUrl, ipAddress)).build();
        try {
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                JSONObject responseJson = new JSONObject(response.body().string());
                result = responseJson.get("geoplugin_countryName").equals(country.getName());
            }
        } catch (IOException ignored) {
        }
        return result;
    }
}
