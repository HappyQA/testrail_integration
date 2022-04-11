package test.common;

import com.codeborne.selenide.WebDriverRunner;

import config.TestRailManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.platform.engine.TestExecutionResult;

import static com.codeborne.selenide.Selenide.clearBrowserCookies;

public class BaseTest {

    protected String TestCaseId;

    public static Logger logger = LogManager.getLogger(someClass.class);

    /**
     * Here example how to use @After annotation and send test results
     * to TestRail via REST API after every single test run
     */

    @After
    public void sendResultsToTestRail(TestExecutionResult result) throws Throwable {
        if (result.getStatus() == TestExecutionResult.Status.SUCCESSFUL) {
            TestRailManager.addResultForTestCase(TestCaseId, TestRailManager.TEST_CASE_PASSED_STATUS, "");
        } else {
            TestRailManager.addResultForTestCase(TestCaseId, TestRailManager.TEST_CASE_FAILED_STATUS, String.valueOf(result.getThrowable()));
        }
        logger.info("Sending test's result's to TestRail server");
    }

    @AfterEach
    public void shutDown() {
        clearBrowserCookies();
        WebDriverRunner.getWebDriver().quit();
    }
}
