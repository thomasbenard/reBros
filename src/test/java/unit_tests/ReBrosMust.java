package unit_tests;

import com.thomasbenard.rebros.*;
import org.junit.Test;

import static com.thomasbenard.rebros.Matches.emptyResult;
import static com.thomasbenard.rebros.Node.objectNode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ReBrosMust {

    private final String complexInputData = "{family: {person: {id: 1, first_name: Jean, last_name: Bonneau}}}";
    private final String familyIsAnArray = "{family: ["
            + "{person: {id: 1, first_name: Jean, last_name: Bonneau}}, "
            + "{person: {id: 2, first_name: Charles, last_name: Cuttery}}]}";

    private ReBros reBros(String jsonData) {
        Content content = new JsonContent(jsonData);
        return new ReBros(content);
    }

    private Node jean() {
        return objectNode()
                .addField("id", "1")
                .addField("first_name", "Jean")
                .addField("last_name", "Bonneau");
    }

    private ObjectNode charles() {
        return objectNode()
                .addField("id", "2")
                .addField("first_name", "Charles")
                .addField("last_name", "Cuttery");
    }

    @Test
    public void return_empty_result_given_empty_request() {
        Request emptyRequest = new Request();

        Matches matches = reBros("{id: 1, first_name: Jean, last_name: Bonneau}").run(emptyRequest);

        assertThat(matches, equalTo(emptyResult()));
    }

    @Test
    public void return_empty_result_when_no_fields_match_selected_in_request() {
        Request request = new Request();
        request.select("non_existent_field");

        Matches matches = reBros(complexInputData).run(request);

        assertThat(matches, equalTo(emptyResult()));
    }

    @Test
    public void return_the_wanted_fields_when_selecting() {
        Request request = new Request();
        request.select("id", "last_name");

        Matches matches = reBros(complexInputData).run(request);

        Matches expectedMatches = emptyResult()
                .put("id", "1")
                .put("last_name", "Bonneau");
        assertThat(matches, equalTo(expectedMatches));
    }

    @Test
    public void return_all_simple_matches() {
        Request request = new Request();
        request.select("something");

        Matches matches = reBros("{something: [1, 2]}").run(request);

        Matches expectedMatches = emptyResult()
                .put("something", "1")
                .put("something", "2");
        assertThat(matches, equalTo(expectedMatches));
    }

    @Test
    public void return_complex_types() {
        Request request = new Request();
        request.select("person");

        Matches matches = reBros(complexInputData).run(request);

        Matches expectedMatches = emptyResult()
                .put("person", jean());
        assertThat(matches, equalTo(expectedMatches));
    }

    @Test
    public void return_all_complex_matches() {
        Request request = new Request();
        request.select("person");

        Matches matches = reBros(familyIsAnArray).run(request);

        Matches expectedMatches = emptyResult()
                .put("person", jean())
                .put("person", charles());
        assertThat(matches, equalTo(expectedMatches));
    }

    @Test
    public void return_complex_matches_containing_an_array() {
        Request request = new Request();
        request.select("family");

        Matches matches = reBros(familyIsAnArray).run(request);

        Matches expectedMatches = emptyResult()
                .put("family", objectNode().addField("person", jean()))
                .put("family", objectNode().addField("person", charles()));
        assertThat(matches, equalTo(expectedMatches));
    }

    @Test
    public void select_matches_fitting_conditions() {
        Request request = new Request();
        request.select("person");
        request.where("id", "1");

        Matches matches = reBros(familyIsAnArray).run(request);

        Matches expectedMatches = emptyResult()
                .put("person", jean());
        assertThat(matches, equalTo(expectedMatches));
    }
}
