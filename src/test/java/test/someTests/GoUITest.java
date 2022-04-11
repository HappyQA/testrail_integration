package test.someTests;

import test.common.BaseTest;
import config.APIException;
import org.junit.jupiter.api.*;
import config.TestRailManager;

import java.io.IOException;


public class GoUITest extends BaseTest {

    @Test
    @DisplayName("Test Case - 1")
    public void createSomething() throws APIException, IOException {
        TestCaseId = "";

        /**
         * Do some test steps, after that method 'addResultForTestCase' sending test result to TestRail
         * via REST API or use @After method in Base Class
         */

        TestRailManager.addResultForTestCase(TestCaseId, TestRailManager.TEST_CASE_PASSED_STATUS, "");
    }
}
