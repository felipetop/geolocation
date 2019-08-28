package com.felipesales.geolocation.service;

import com.felipesales.geolocation.datatransferobject.ipVigilante.Coordinates;
import com.felipesales.geolocation.datatransferobject.metaWeather.ConsolidateWeather;
import com.felipesales.geolocation.datatransferobject.metaWeather.Weather;
import com.felipesales.geolocation.datatransferobject.metaWeather.Woe;
import com.felipesales.geolocation.model.Client;
import com.felipesales.geolocation.model.Geolocation;
import com.felipesales.geolocation.repository.ClientRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    RestTemplate restTemplate;

    public void delete(Integer id) throws NotFoundException {
        Optional<Client> client = clientRepository.findById(id);

        if (!client.isPresent()) {
            throw new NotFoundException(messageSource.getMessage("clientNotFound", null, null));
        }

        clientRepository.delete(client.get());
    }

    public List<Client> findAll() {
        return clientRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public Client findById(Integer id) throws NotFoundException {

        Optional<Client> client = clientRepository.findById(id);

        if (!client.isPresent()) {
            throw new NotFoundException(messageSource.getMessage("clientNotFound", null, null));
        }

        return client.get();
    }

    public Client save(Client client, BindingResult bindingResult, HttpServletRequest request) throws NotFoundException, ValidationException, IOException {

        if (client.getGeolocation() == null) {
            client.setGeolocation(new Geolocation());
        }
        Coordinates coordinates = getCoordinatesByIp(request);
        ConsolidateWeather consolidateWeather = getTodayWeather(coordinates);
        client.getGeolocation().setMaxTemperature(consolidateWeather.getMax_temp());
        client.getGeolocation().setMinTemperature(consolidateWeather.getMin_temp());
        return clientRepository.save(client);
    }

    public Client update(Integer id, Client client, BindingResult bindingResult) throws NotFoundException, ValidationException {

        Client clientToUpdate = findById(id);

        if (clientToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("clientNotFound", null, null));
        }

        clientToUpdate.setName(client.getName());
        clientToUpdate.setAge(client.getAge());

        return clientRepository.save(clientToUpdate);
    }

    private Coordinates getCoordinatesByIp(HttpServletRequest request) throws IOException {
        //String url = "https://ipvigilante.com/json/"+request.getRemoteAddr()+"";
        String url = "https://ipvigilante.com/json/189.111.88.108";
        Coordinates coordinates = restTemplate.getForObject(url, Coordinates.class);
        return coordinates;
    }

    private Woe getWoeIdByCoordinates (Coordinates coordinates) {
        String url = "https://www.metaweather.com/api/location/search/?lattlong="+coordinates.getData().getLatitude()+","+coordinates.getData().getLongitude()+"";

        ResponseEntity<Woe[]> responseEntity = restTemplate.getForEntity(url, Woe[].class);
        Woe[] woes = responseEntity.getBody();
        Woe woe = new Woe();
        for (int i = 0; i < woes.length; i++) {
           woe = woes[i];
        }

        return woe;
    }

    private Weather getWeatherByWoe (Woe woe) {
        String url = "https://www.metaweather.com/api/location/"+woe.getWoeid()+"/";
        Weather weather = restTemplate.getForObject(url, Weather.class);
        return weather;
    }

    private ConsolidateWeather getTodayWeather(Coordinates coordinates){
        Woe woe = getWoeIdByCoordinates (coordinates);
        Weather weather = getWeatherByWoe(woe);
        return weather.getConsolidated_weather().get(0);
    }

}