package integration_tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = com.thomasbenard.rebros.infra.Application.class,
        properties = "file.json=my_file.json")
public class ReBrosWebIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    private String sendRequest(String parameters) throws MalformedURLException {
        URL base = new URL("http://localhost:" + port + "?" + parameters);
        return template.getForEntity(base.toString(), String.class).getBody();
    }

    @Test
    public void select_first_name_and_last_name() throws MalformedURLException {
        String response = sendRequest("select=first_name,last_name");
        assertThat(response, equalTo("{last_name=[Bonneau, Cuttery], first_name=[Jean, Charles]}"));
    }

    @Test
    public void select_person_where_id_is_1() throws MalformedURLException {
        String response = sendRequest("select=person&where=id:1");
        assertThat(response, equalTo("{person=[{last_name=Bonneau, id=1, first_name=Jean}]}"));
    }
}
