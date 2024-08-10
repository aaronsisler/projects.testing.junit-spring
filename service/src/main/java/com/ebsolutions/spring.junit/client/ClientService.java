package com.ebsolutions.spring.junit.client;

import com.ebsolutions.spring.junit.model.Client;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientService {
    private final ClientDao clientDao;

    public ClientService(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public List<Client> create(List<Client> clients) {
        return clientDao.create(clients);
    }

    public List<Client> readAll() {
        return clientDao.readAll();
    }
}
