package com.angrysamaritan.roomwithlights.repos;

import com.angrysamaritan.roomwithlights.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepo extends CrudRepository<Country, Integer> {
}
