package com.ebsolutions.spring.junit.client;

import com.ebsolutions.spring.junit.BaseTestContext;
import com.ebsolutions.spring.junit.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


public class ClientTest extends BaseTestContext {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        System.out.println("setup");
    }

    //    @Test
    void givenClientsDoNotExistWhenGetClientsIsCalledShouldReturnCorrectResponse() throws Exception {
        System.out.println("Before: this.mockMvc.perform");
        this.mockMvc.perform(get(Constants.CLIENTS_URL));
//                .andExpect(status().isOk())
//                .andDo(print());
//                .andExpect(content().string(containsString("UP")));
        System.out.println("After: this.mockMvc.perform");

    }
}
