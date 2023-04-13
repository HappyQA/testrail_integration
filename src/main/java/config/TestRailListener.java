package config;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

public class TestRailListener implements TestWatcher {

    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional<String> reason) {
    }

    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        sendTestResult(extensionContext, String.valueOf(TestResult.PASSED));
    }

    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable cause) {
        sendTestResult(extensionContext, String.valueOf(TestResult.BLOCKED));
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
        throw new Exception("Missing Run Id");
    }

    private void sendTestResult(ExtensionContext extensionContext, String result) {
        String suiteId;
        try {
            suiteId = getTestRunId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        TestRailHelper.setTestRailStatus(extensionContext, TestResult.valueOf(result), suiteId);
    }
}
