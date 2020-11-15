package com.klangfang.core;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = "h2")
public class CompositionServiceApplicationTests {

    @Test
    public void contextLoads() {
    }

}
