package com.ebsolutions.spring.junit.client;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClientTestAbc extends ClientContext {
    private final String CLIENTS_URL = "/clients";

    //    @Test
    void whenGetClientsIsCalledShouldReturnCorrectClientsMessage() throws Exception {
        this.mockMvc.perform(get(CLIENTS_URL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("UP")));
    }
}
