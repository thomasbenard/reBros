package unit_tests;

import com.thomasbenard.rebros.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.thomasbenard.rebros.Matches.emptyResult;
import static com.thomasbenard.rebros.Node.objectNode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class ReBrosMust {

    private final String familyIsOnePerson = "{family: {person: {id: 1, first_name: Jean, last_name: Bonneau}}}";
    private final String familyIsAnArrayOfPersons = "{family: ["
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
        Request request = new Request()
                .select("non_existent_field");

        Matches matches = reBros(familyIsOnePerson).run(request);

        assertThat(matches, equalTo(emptyResult()));
    }

    @Test
    public void return_the_wanted_fields_when_selecting() {
        Request request = new Request()
                .select("id", "last_name");

        Matches matches = reBros(familyIsOnePerson).run(request);

        Matches expectedMatches = emptyResult()
                .put("id", "1")
                .put("last_name", "Bonneau");
        assertThat(matches, equalTo(expectedMatches));
    }

    @Test
    public void return_all_simple_matches() {
        Request request = new Request()
                .select("something");

        Matches matches = reBros("{something: [1, 2]}").run(request);

        Matches expectedMatches = emptyResult()
                .put("something", "1")
                .put("something", "2");
        assertThat(matches, equalTo(expectedMatches));
    }

    @Test
    public void return_complex_types() {
        Request request = new Request()
                .select("person");

        Matches matches = reBros(familyIsOnePerson).run(request);

        Matches expectedMatches = emptyResult()
                .put("person", jean());
        assertThat(matches, equalTo(expectedMatches));
    }

    @Test
    @Parameters(method = "browseArraysValues")
    public void browse_arrays_to_find_all_matches(String fieldName, Node match1, Node match2) {
        Request request = new Request()
                .select(fieldName);

        Matches matches = reBros(familyIsAnArrayOfPersons).run(request);

        Matches expectedMatches = emptyResult()
                .put(fieldName, match1)
                .put(fieldName, match2);
        assertThat(matches, equalTo(expectedMatches));
    }

    private Object[] browseArraysValues() {
        return new Object[]{
                new Object[]{"person", jean(), charles()},
                new Object[]{"family", objectNode().addField("person", jean()), objectNode().addField("person", charles())}
        };
    }

    @Test
    @Parameters(method = "whereClausesValues")
    public void filter_matches_not_fitting_where_clauses(String fieldName, String fieldValue, Node expectedNode) {
        Request request = new Request()
                .select("person")
                .where(fieldName, fieldValue);

        Matches matches = reBros(familyIsAnArrayOfPersons).run(request);

        Matches expectedMatches = emptyResult()
                .put("person", expectedNode);
        assertThat(matches, equalTo(expectedMatches));
    }

    private Object[] whereClausesValues() {
        return new Object[]{
                new Object[]{"id", "1", jean()},
                new Object[]{"id", "2", charles()},
                new Object[]{"first_name", "Charles", charles()}
        };
    }

    @Test
    public void filter_matches_not_fitting_all_where_clauses() {
        Request request = new Request()
                .select("person")
                .where("first_name", "2");

        Matches matches = reBros(familyIsAnArrayOfPersons).run(request);

        assertThat(matches, equalTo(emptyResult()));
    }
}
