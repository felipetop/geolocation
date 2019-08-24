package com.felipesales.geolocation.service;

import com.felipesales.geolocation.model.Client;
import com.felipesales.geolocation.repository.ClientRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@Component
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MessageSource messageSource;

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

    public Client save(Client client, BindingResult bindingResult) throws NotFoundException, ValidationException {

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
}