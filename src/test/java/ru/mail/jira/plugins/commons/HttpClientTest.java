package ru.mail.jira.plugins.commons;

import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpClientTest {
    @BeforeEach
    void setUp() {
    }

    @Test
    void get() {
        GetRequest request = HttpClient.get("https://example.com").headers(new HashMap<String, String>() {{
            put("accept", "application/json");
        }});
        assertEquals("application/json", request.getHeaders().get("accept").get(0));
        HttpResponse<String> response = request.asString();
        assertEquals(200, response.getStatus());
        assertNotNull(response.getBody());
    }

}
