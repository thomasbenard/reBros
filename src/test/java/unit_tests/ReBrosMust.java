package unit_tests;

import com.thomasbenard.rebros.*;
import org.junit.Test;

import static com.thomasbenard.rebros.Node.objectNode;
import static com.thomasbenard.rebros.Result.emptyResult;
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

    @Test
    public void return_empty_result_given_empty_request() {
        Request emptyRequest = new Request();

        Result result = reBros("{id: 1, first_name: Jean, last_name: Bonneau}").run(emptyRequest);

        assertThat(result, equalTo(emptyResult()));
    }

    @Test
    public void return_empty_result_when_no_fields_match_selected_in_request() {
        Request request = new Request();
        request.select("non_existent_field");

        Result result = reBros(complexInputData).run(request);

        assertThat(result, equalTo(emptyResult()));
    }

    @Test
    public void return_the_wanted_fields_when_selecting() {
        Request request = new Request();
        request.select("id", "last_name");

        Result result = reBros(complexInputData).run(request);

        Result expectedResult = emptyResult()
                .put("id", "1")
                .put("last_name", "Bonneau");
        assertThat(result, equalTo(expectedResult));
    }

    @Test
    public void return_all_simple_matches() {
        Request request = new Request();
        request.select("something");

        Result result = reBros("{something: [1, 2]}").run(request);

        Result expectedResult = emptyResult()
                .put("something", "1")
                .put("something", "2");
        assertThat(result, equalTo(expectedResult));
    }

    @Test
    public void return_complex_types() {
        Request request = new Request();
        request.select("person");

        Result result = reBros(complexInputData).run(request);

        Node complexNode = objectNode()
                .addField("id", "1")
                .addField("first_name", "Jean")
                .addField("last_name", "Bonneau");
        Result expectedResult = emptyResult()
                .put("person", complexNode);
        assertThat(result, equalTo(expectedResult));
    }

    @Test
    public void return_all_complex_matches() {
        Request request = new Request();
        request.select("person");

        Result result = reBros(familyIsAnArray).run(request);

        Node jean = objectNode()
                .addField("id", "1")
                .addField("first_name", "Jean")
                .addField("last_name", "Bonneau");
        Node charles = objectNode()
                .addField("id", "2")
                .addField("first_name", "Charles")
                .addField("last_name", "Cuttery");
        Result expectedResult = emptyResult()
                .put("person", jean)
                .put("person", charles);
        assertThat(result, equalTo(expectedResult));
    }

    @Test
    public void return_complex_matches_containing_an_array() {
        Request request = new Request();
        request.select("family");

        Result result = reBros(familyIsAnArray).run(request);

        Node jean = objectNode()
                .addField("person", objectNode()
                        .addField("id", "1")
                        .addField("first_name", "Jean")
                        .addField("last_name", "Bonneau"));
        Node charles = objectNode()
                .addField("person", objectNode()
                        .addField("id", "2")
                        .addField("first_name", "Charles")
                        .addField("last_name", "Cuttery"));
        Result expectedResult = emptyResult()
                .put("family", jean)
                .put("family", charles);
        assertThat(result, equalTo(expectedResult));
    }

    @Test
    public void select_matches_fitting_conditions() {
        Request request = new Request();
        request.select("person");
        request.where("id", "1");

        Result result = reBros(familyIsAnArray).run(request);

        Node jean = objectNode()
                .addField("id", "1")
                .addField("first_name", "Jean")
                .addField("last_name", "Bonneau");
        Result expectedResult = emptyResult()
                .put("person", jean);
        assertThat(result, equalTo(expectedResult));
    }
}
