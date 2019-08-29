package com.felipesales.geolocation.controller;

import com.felipesales.geolocation.model.Client;
import com.felipesales.geolocation.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        clientService.delete(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Client> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public Client findById(@PathVariable("id") Integer id) {
        return clientService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Client save(@RequestBody @Valid Client client, HttpServletRequest request) {
        return clientService.save(client, request);
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable("id") Integer id, @RequestBody @Valid Client client) {
        return clientService.update(id, client);
    }
}