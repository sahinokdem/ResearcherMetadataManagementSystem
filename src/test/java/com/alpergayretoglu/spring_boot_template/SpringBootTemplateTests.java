package com.alpergayretoglu.spring_boot_template;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@RunWith(SpringRunner.class)
class SpringBootTemplateTests {

    public static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) (new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("db")
            .withUsername("sa")
            .withPassword("sa"))
            .withReuse(true);

    @Test
    void contextLoads() {
    }

}
