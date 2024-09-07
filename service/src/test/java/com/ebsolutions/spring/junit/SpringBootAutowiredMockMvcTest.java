package com.ebsolutions.spring.junit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class SpringBootAutowiredMockMvcTest extends BaseTestContext {
    @Autowired
    private MockMvc mockMvc;

    //    @Test
    void whenHealthCheckIsCalledShouldReturnCorrectMessage() throws Exception {
        this.mockMvc.perform(get(Constants.HEALTH_CHECK_URL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("UP")));
    }

    //    @Test
    void whenInfoCheckIsCalledShouldReturnCorrectMessage() throws Exception {
        this.mockMvc.perform(get(Constants.INFO_CHECK_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.build.group", is("com.ebsolutions.spring.junit")))
                .andExpect(jsonPath("$.build.artifact", is("spring-junit-service")))
                .andExpect(jsonPath("$.build.name", is("Spring Junit Service")))
                .andExpect(jsonPath("$.build.version", notNullValue()))
                .andExpect(jsonPath("$.build.time", notNullValue()));
    }
}
