package unit_tests;

import com.thomasbenard.rebros.*;
import org.junit.Ignore;
import org.junit.Test;

import static com.thomasbenard.rebros.Result.emptyResult;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

//TODO
//Clean JsonContent
//Mutability Result
//Key in both Result and Match

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
    public void return_all_wanted_matches() {
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

        Match complexMatch = new Match()
                .addField("id", "1")
                .addField("first_name", "Jean")
                .addField("last_name", "Bonneau");
        Result expectedResult = emptyResult()
                .put("person", complexMatch);
        assertThat(result, equalTo(expectedResult));
    }

    @Test
    public void return_all_matching_results() {
        Request request = new Request();
        request.select("person");

        Result result = reBros(familyIsAnArray).run(request);

        Match jean = new Match()
                .addField("id", "1")
                .addField("first_name", "Jean")
                .addField("last_name", "Bonneau");
        Match charles = new Match()
                .addField("id", "2")
                .addField("first_name", "Charles")
                .addField("last_name", "Cuttery");
        Result expectedResult = emptyResult()
                .put("person", jean)
                .put("person", charles);
        assertThat(result, equalTo(expectedResult));
    }

    @Ignore
    @Test
    public void return_multiple_complex_objects() {
        Request request = new Request();
        request.select("family");

        Result result = reBros(familyIsAnArray).run(request);

        Result expectedResult = emptyResult();
        Match jean = new Match()
                .addField("id", "1")
                .addField("first_name", "Jean")
                .addField("last_name", "Bonneau");
        Match charles = new Match()
                .addField("id", "2")
                .addField("first_name", "Charles")
                .addField("last_name", "Cuttery");
        Match family = new Match()
                .addField("person", jean)
                .addField("person", charles);
        expectedResult.put("family", family);
        assertThat(result, equalTo(expectedResult));
    }
}
