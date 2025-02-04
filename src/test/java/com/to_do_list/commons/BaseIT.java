package com.to_do_list.commons;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
//@Sql(value = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BaseIT {
    @LocalServerPort
    private Integer port;
    protected final Gson gson = new Gson();

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.defaultParser = Parser.JSON;
    }
}
