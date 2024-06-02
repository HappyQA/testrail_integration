package testRail;

public class TestRailListener implements TestWatcher {

    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional<String> reason) {
    }

    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        sendTestResult(extensionContext, String.valueOf(TestResult.PASSED));
    }

    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable cause) {
        sendTestResult(extensionContext, String.valueOf(TestResult.FAILED));
    }

    private String getTestRunId() throws Exception {
        String getRunId = System.getProperty("runId");
        if (getRunId != null) {
            return getRunId;
        }
        throw new Exception("TestRail run ID are not found");
    }

    private void sendTestResult(ExtensionContext extensionContext, String result) {
        String suiteId;
        try {
            suiteId = getTestRunId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        TestRailHelper.sendTestRailResults(extensionContext, TestResult.valueOf(result), suiteId);
    }
}
