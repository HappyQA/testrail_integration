package testRail;

public class TestRailHelper {

  public static ICredentialsFile cfg = ConfigFactory.create(ICredentialsFile.class);

  private static APIClient apiClient;

  public static void sendTestRailResults(ExtensionContext extContext, TestResult result, String suiteId) {
    TestRailCreds creds = new TestRailCreds.Builder()
            .withProjectUrl(cfg.testRailUrl())
            .withUsername(cfg.testRailLogin())
            .withPassword(cfg.testRailPassword())
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

  private static int getTestResult(TestResult result) {
    int statusId = 0;

    switch (result) {
      case PASSED:
        statusId = 1;
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
