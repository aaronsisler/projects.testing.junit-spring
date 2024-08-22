package com.ebsolutions.spring.junit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class SpringBootAutowiredMockMvcTest {
    @Autowired
    private MockMvc mockMvc;

    private final String HEALTH_CHECK_URL = "/actuator/health";
    private final String INFO_CHECK_URL = "/actuator/info";

    @Test
    void whenHealthCheckIsCalledShouldReturnCorrectMessage() throws Exception {
        this.mockMvc.perform(get(HEALTH_CHECK_URL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("UP")));
    }

    @Test
    void whenInfoCheckIsCalledShouldReturnCorrectMessage() throws Exception {
        this.mockMvc.perform(get(INFO_CHECK_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.build.group", is("com.ebsolutions.spring.junit")))
                .andExpect(jsonPath("$.build.artifact", is("spring-junit-service")))
                .andExpect(jsonPath("$.build.name", is("Spring Junit Service")))
                .andExpect(jsonPath("$.build.version", notNullValue()))
                .andExpect(jsonPath("$.build.time", notNullValue()));
    }
}
