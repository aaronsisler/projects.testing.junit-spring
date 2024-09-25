package com.ebsolutions.spring.junit.client;

import com.ebsolutions.spring.junit.model.Client;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ClientService {
  private final ClientDao clientDao;

  public List<Client> create(List<Client> clients) {
    return clientDao.create(clients);
  }

  public List<Client> readAll() {
    return clientDao.readAll();
  }
}
