package unit_tests;

import com.thomasbenard.rebros.*;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ReBrosMust {

    private final String inputData = "{id: 1, first_name: Jean, last_name: Bonneau}";
    private final Content content = new JsonContent(inputData);
    private final ReBros reBros = new ReBros(content);

    @Test
    public void return_empty_result_given_empty_request() {
        Request emptyRequest = new Request();

        Result result = reBros.run(emptyRequest);

        Result emptyResult = new Result();
        assertThat(result, equalTo(emptyResult));
    }

    @Test
    public void return_the_wanted_fields_when_selecting() {
        Request request = new Request();
        request.select("id", "first_name");

        Result result = reBros.run(request);

        Result expectedResult = new Result();
        expectedResult.put("id", "1");
        expectedResult.put("first_name", "Jean");
        assertThat(result, equalTo(expectedResult));
    }
}
