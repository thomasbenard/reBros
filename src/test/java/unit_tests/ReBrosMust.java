package unit_tests;

import com.thomasbenard.rebros.*;
import org.junit.Ignore;
import org.junit.Test;

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
//    private final String moreComplexInputData = "{family: " +
//            "{" +
//            "dog: {name: Cooper, age: 6, race: dawg, good_boy_status: true}, " +
//            "people: {id: 1, first_name: Jean, last_name: Bonneau}, " +
//            "cat: {name: Felix}" +
//            "}" +
//            "}";

    private ReBros reBros(String jsonData) {
        Content content = new JsonContent(jsonData);
        return new ReBros(content);
    }

    @Test
    public void return_empty_result_given_empty_request() {
        Request emptyRequest = new Request();

        Result result = reBros("{id: 1, first_name: Jean, last_name: Bonneau}").run(emptyRequest);

        Result emptyResult = new Result();
        assertThat(result, equalTo(emptyResult));
    }

    @Test
    public void return_empty_result_when_no_fields_match_selected_in_request() {
        Request request = new Request();
        request.select("non_existent_field");

        Result result = reBros(complexInputData).run(request);

        Result emptyResult = new Result();
        assertThat(result, equalTo(emptyResult));
    }

    @Test
    public void return_the_wanted_fields_when_selecting() {
        Request request = new Request();
        request.select("id", "last_name");

        Result result = reBros(complexInputData).run(request);

        Result expectedResult = new Result();
        expectedResult.put("id", "1");
        expectedResult.put("last_name", "Bonneau");
        assertThat(result, equalTo(expectedResult));
    }

    @Test
    public void return_all_wanted_matches() {
        Request request = new Request();
        request.select("something");

        Result result = reBros("{something: [1, 2]}").run(request);

        Result expectedResult = new Result();
        expectedResult.put("something", "1");
        expectedResult.put("something", "2");
        assertThat(result, equalTo(expectedResult));
    }

    @Test
    public void return_complex_types() {
        Request request = new Request();
        request.select("person");

        Result result = reBros(complexInputData).run(request);

        Result expectedResult = new Result();
        Match complexMatch = new Match("person")
                .addField("id", "1")
                .addField("first_name", "Jean")
                .addField("last_name", "Bonneau");
        expectedResult.put("person", complexMatch);
        assertThat(result, equalTo(expectedResult));
    }

    @Test
    public void return_all_matching_results() {
        Request request = new Request();
        request.select("person");

        Result result = reBros(familyIsAnArray).run(request);

        Result expectedResult = new Result();
        Match jean = new Match("person")
                .addField("id", "1")
                .addField("first_name", "Jean")
                .addField("last_name", "Bonneau");
        expectedResult.put("person", jean);
        Match charles = new Match("person")
                .addField("id", "2")
                .addField("first_name", "Charles")
                .addField("last_name", "Cuttery");
        expectedResult.put("person", charles);
        assertThat(result, equalTo(expectedResult));
    }

    @Ignore
    @Test
    public void return_multiple_complex_objects() {
        Request request = new Request();
        request.select("family");

        Result result = reBros(familyIsAnArray).run(request);

        Result expectedResult = new Result();
        Match jean = new Match("person")
                .addField("id", "1")
                .addField("first_name", "Jean")
                .addField("last_name", "Bonneau");
        Match charles = new Match("person")
                .addField("id", "2")
                .addField("first_name", "Charles")
                .addField("last_name", "Cuttery");
        Match family = new Match("family")
                .addField("person", jean)
                .addField("person", charles);
        expectedResult.put("family", family);
        assertThat(result, equalTo(expectedResult));
    }
}
