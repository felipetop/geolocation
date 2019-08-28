package com.felipesales.geolocation.controller;

import com.felipesales.geolocation.model.Client;
import com.felipesales.geolocation.service.ClientService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) throws NotFoundException {
        clientService.delete(id);
    }

    @GetMapping
    public List<Client> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public Client findById(@PathVariable("id") Integer id) throws NotFoundException {
        return clientService.findById(id);
    }

    @PostMapping
    public Client save(@RequestBody @Valid Client client, BindingResult bindingResult, HttpServletRequest request) throws NotFoundException, ValidationException, IOException {
        return clientService.save(client, bindingResult, request);
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable("id") Integer id, @RequestBody @Valid Client client, BindingResult bindingResult) throws NotFoundException, ValidationException {
        return clientService.update(id, client, bindingResult);
    }
}