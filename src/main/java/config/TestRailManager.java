package config;

import org.aeonbits.owner.ConfigFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestRailManager {

    /**
     * Provide cfg file with Username / Password / Host to TestRail API
     */

    public static ConfigFile cfg = ConfigFactory.create(ConfigFile.class);

    public static String TEST_RUN_ID = "";
    public static String TESTRAIL_USERNAME = cfg.testRailLogin();
    public static String TESTRAIL_PASSWORD = cfg.testRailPassword();
    public static String RAILS_ENGINE_URL = "https://test.rail.host/";
    public static final int TEST_CASE_PASSED_STATUS = 1;
    public static final int TEST_CASE_FAILED_STATUS = 5;

    public static void addResultForTestCase(String testCaseId, int status, String error) throws APIException, IOException {
        String testRunId = TEST_RUN_ID;
        APIClient client = new APIClient(RAILS_ENGINE_URL);
        client.setUser(TESTRAIL_USERNAME);
        client.setPassword(TESTRAIL_PASSWORD);
        Map data = new HashMap();
        data.put("status_id", status);
        data.put("comment", "Test Executed - Status updated automatically from Selenium test automation.");
        client.sendPost("add_result_for_case/"+testRunId+"/"+testCaseId+"", data);
    }
}
