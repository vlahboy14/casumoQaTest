import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.testng.Assert.assertEquals;
import static org.testng.Reporter.log;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class CasumoTest {

    private final static String URL = "http://localhost:8080/";

    public static String removeVowels(String input) {
        return input.replaceAll("[aeiouAEIOU]", "");
    }


    @DataProvider(name = "inputsProvider")
    public static Object[][] inputsProvider() {
        return new Object[][]{
                {"Verify random valid alphanumeric input, length between 5 and 10",
                        randomAlphanumeric(5, 10)},

                {"Verify random valid alphanumeric input with spaces",
                        randomAlphanumeric(3, 5) + " " + randomAlphanumeric(3, 5) + " " + randomAlphanumeric(3, 5)},

                {"Verify valid alphanumeric input with 100 characters length",
                        randomAlphanumeric(100)}
        };
    }

    @Test(dataProvider = "inputsProvider")
    public void test(String testCaseName, String input) {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        log("Starting testcase : " + testCaseName);

        String output = given()
                .pathParam("input", input)
                .when()
                .get(URL + "{input}")
                .then().log().all()
                .extract().body().asString();

        assertEquals(output, removeVowels(input));
    }
}