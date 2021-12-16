package com.behind.the.project.service;

import com.behind.the.project.domain.City;
import com.behind.the.project.domain.Country;
import com.behind.the.project.domain.Province;
import com.behind.the.project.domain.Register;
import com.behind.the.project.repository.CityRepository;
import com.behind.the.project.repository.CountryRepository;
import com.behind.the.project.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RegisterService {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private CityRepository cityRepository;

    public void process(List<Register> registers){
        Iterator<Register> it = registers.iterator();
        //String province = null;
        //List<String> provinces = new ArrayList<>();
        //boolean exist = false;
        Register reg;
        Country country;
        Province province;
        City city;
        while (it.hasNext()){
            reg = it.next();

            country = countryRepository.findByCountry(reg.getCountry());
            if(country == null){
                country = new Country();
                country.setCountry(reg.getCountry());
                country = countryRepository.save(country);
            }

            province = provinceRepository.findByProvince(reg.getAdmin_name());
            if(province == null){
                province = new Province();
                province.setProvince(reg.getAdmin_name());
                province.setCountry(country);
                province = provinceRepository.save(province);
                country.getProvinces().add(province);
                countryRepository.save(country);
            }

            city = cityRepository.findByCity(reg.getCity());
            if(city == null){
                city = new City();
                city.setCity(reg.getCity());
                city = cityRepository.save(city);

                province = provinceRepository.findByProvince(reg.getAdmin_name());
                if(province == null){
                    province = new Province();
                    province.setProvince(reg.getAdmin_name());
                    province = provinceRepository.save(province);
                    province.getCities().add(city);
                    province = provinceRepository.save(province);
                    country.getProvinces().add(province);
                    countryRepository.save(country);
                }else{
                    province.getCities().add(city);
                    province = provinceRepository.save(province);
                }
                city.setProvince(province);
                cityRepository.save(city);
            }
        }
    }
}
