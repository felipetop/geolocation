package com.felipesales.geolocation.controller;

import com.felipesales.geolocation.model.Client;
import com.felipesales.geolocation.service.ClientService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/clients")
@CrossOrigin(origins = "*")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @ApiOperation(value = "Deletes a client using client identifier")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        clientService.delete(id);
    }

    @ApiOperation(value = "Returns all Clients")
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Client> findAll() {
        return clientService.findAll();
    }

    @ApiOperation(value = "Returns Client from Client identifier")
    @GetMapping("/{id}")
    public Client findById(@PathVariable("id") Integer id) {
        return clientService.findById(id);
    }

    @ApiOperation(value = "Create a new Client")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Client save(@RequestBody @Valid Client client, HttpServletRequest request) {
        return clientService.save(client, request);
    }

    @ApiOperation(value = "Update a Client")
    @PutMapping("/{id}")
    public Client update(@PathVariable("id") Integer id, @RequestBody @Valid Client client) {
        return clientService.update(id, client);
    }
}