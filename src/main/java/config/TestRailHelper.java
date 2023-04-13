package config;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TestRailHelper {

  private static APIClient apiClient;
  private static Long suiteId;

  public static void createNewTestRailRun(TestRailCreds projectCreds, String projectId, String testRunName){
    if (suiteId==null) createRun(projectCreds, projectId, testRunName);
  }

  public static void setTestRailStatus(ExtensionContext extContext, TestResult result, String suiteId) {
    TestRailCreds creds = new TestRailCreds.Builder()
            .withProjectUrl("")
            .withUsername("")
            .withPassword("")
            .build();
    apiClient = new APIClient(creds.getProjectUrl());
    apiClient.setUser(creds.getUsername());
    apiClient.setPassword(creds.getPassword());

    String testId = getTestRailId(extContext);
    int testResult = getTestResult(result);
    String commentValue = "";
    if (testResult==1) {
      commentValue = "TEST SUCCESS";
    }
    if (testResult==2) {
      commentValue = "TEST RETRY";
    }
    if (testResult==5) {
      commentValue = "TEST HAS BEEN FAILED";
    }
    Map <String, Object> resultData = new HashMap<>();
    resultData.put("status_id", testResult);
    resultData.put("comment", commentValue);

    try {
      apiClient.sendPost("add_result_for_case/" + suiteId + "/" + testId, resultData);
    } catch (IOException | APIException e) {
      e.printStackTrace();
    }
  }

  private static void createRun(TestRailCreds projectCreds, String projectId, String testRunName) {
    String currentDateTime = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss"));

    apiClient = new APIClient(projectCreds.getProjectUrl());
    apiClient.setUser(projectCreds.getUsername());
    apiClient.setPassword(projectCreds.getPassword());

    Map <String, Object> requestData = new HashMap<>();
    requestData.put("include_all", true);
    requestData.put("name", testRunName + currentDateTime);
    requestData.put("description", "Automatically generated test suite");

    JSONObject obj = null;
    try {
      obj = (JSONObject) apiClient.sendPost("/add_run/" + projectId, requestData);
    } catch (IOException | APIException e) {
      e.printStackTrace();
    }
    suiteId = (Long) obj.get("id");
  }

  private static int getTestResult(TestResult result) {
    int statusId = 0;

    switch (result) {
      case PASSED:
        statusId = 1;
        break;
      case BLOCKED:
        statusId = 2;
        break;
      case FAILED:
        statusId = 5;
        break;
    }
    return statusId;
  }

  private static String getTestRailId(ExtensionContext extContext) {
    String testId = "";
    Method testMethod = extContext.getTestMethod().get();
    if (testMethod.isAnnotationPresent(TestRailCase.class))
      testId = testMethod.getAnnotation(TestRailCase.class).id();
    return testId;
  }
}
