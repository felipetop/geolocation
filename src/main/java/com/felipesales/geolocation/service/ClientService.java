package com.felipesales.geolocation.service;

import com.felipesales.geolocation.dataTransferObject.ipVigilante.Coordinates;
import com.felipesales.geolocation.dataTransferObject.metaWeather.ConsolidateWeather;
import com.felipesales.geolocation.dataTransferObject.metaWeather.Weather;
import com.felipesales.geolocation.dataTransferObject.metaWeather.Woe;
import com.felipesales.geolocation.model.Client;
import com.felipesales.geolocation.model.Geolocation;
import com.felipesales.geolocation.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Component
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    RestTemplate restTemplate;

    public void delete(Integer id) {
        Optional<Client> client = clientRepository.findById(id);
        clientRepository.delete(client.get());
    }

    public List<Client> findAll() {
        return clientRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public Client findById(Integer id) {

        Optional<Client> client = clientRepository.findById(id);

        return client.get();
    }

    public Client save(Client client, HttpServletRequest request) {

        if (client.getGeolocation() == null) {
            client.setGeolocation(new Geolocation());
        }
        Coordinates coordinates = getCoordinatesByIp(request);
        ConsolidateWeather consolidateWeather = getTodayWeather(coordinates);
        client.getGeolocation().setMaxTemperature(consolidateWeather.getMax_temp());
        client.getGeolocation().setMinTemperature(consolidateWeather.getMin_temp());
        return clientRepository.save(client);
    }

    public Client update(Integer id, Client client){

        Client clientToUpdate = findById(id);

        clientToUpdate.setName(client.getName());
        clientToUpdate.setAge(client.getAge());

        return clientRepository.save(clientToUpdate);
    }

    private Coordinates getCoordinatesByIp(HttpServletRequest request) {
        String url = "https://ipvigilante.com/json/"+request.getRemoteAddr()+"";
        Coordinates coordinates = restTemplate.getForObject(url, Coordinates.class);
        return coordinates;
    }

    private Woe getWoeIdsByCoordinates (Coordinates coordinates) {
        String url = "https://www.metaweather.com/api/location/search/?lattlong="+coordinates.getData().getLatitude()+","+coordinates.getData().getLongitude()+"";
        ResponseEntity<Woe[]> responseEntity = restTemplate.getForEntity(url, Woe[].class);
        Woe[] woes = responseEntity.getBody();
        return getWoeNearestLocation(woes,coordinates);
    }

    private Weather getWeatherByWoe (Woe woe) {
        String url = "https://www.metaweather.com/api/location/"+woe.getWoeid()+"/";
        Weather weather = restTemplate.getForObject(url, Weather.class);
        return weather;
    }

    private ConsolidateWeather getTodayWeather(Coordinates coordinates){
        Woe woe = getWoeIdsByCoordinates (coordinates);
        Weather weather = getWeatherByWoe(woe);
        return weather.getConsolidated_weather().get(0);
    }

    private static double distance(Double lat1, Double lon1, Double lat2, Double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    /*This function converts decimal degrees to radians*/
    private static Double deg2rad(Double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*This function converts radians to decimal degrees*/
    private static Double rad2deg(Double rad) {
        return (rad * 180 / Math.PI);
    }

    private Woe setWoeDistance(Woe woe, Coordinates coordinates){
        String latLong = woe.getLatt_long();
        int pos = latLong.indexOf(",");
        woe.setLatitude(Double.parseDouble(latLong.substring(0, pos)));
        woe.setLongitude(Double.parseDouble(latLong.substring(0, pos)));
        Double distance = distance(
                coordinates.getData().getLatitude(),
                coordinates.getData().getLongitude(),
                woe.getLatitude(),
                woe.getLongitude(),
                "K"
        );
        return woe;
    }

    private Woe getWoeNearestLocation(Woe[] woes,Coordinates coordinates) {
        Woe woeNearest = setWoeDistance(woes[0],coordinates);
        if (woes.length > 1){
            for (int i = 0; i < woes.length; i++) {
               Woe woe  = setWoeDistance(woes[i],coordinates);
               if(woe.getDistance() < woeNearest.getDistance()){
                   woeNearest.setDistance(woe.getDistance());
               }
            }
        }
        return woeNearest;
    }
}