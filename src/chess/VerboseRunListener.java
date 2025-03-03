package chess;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.ArrayList;
import java.util.List;

public class VerboseRunListener extends RunListener {
    private final List<Failure> failures = new ArrayList<>();

    @Override
    public void testStarted(Description description) throws Exception {
        System.out.println("Running: " + description.getClassName() + "." + description.getMethodName());
    }

    @Override
    public void testFinished(Description description) throws Exception {
        System.out.println("Finished: " + description.getClassName() + "." + description.getMethodName());
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        failures.add(failure);
        System.err.println(
                "FAILED: " + failure.getDescription().getClassName() + "." + failure.getDescription().getMethodName());
        System.err.println("Reason: " + failure.getMessage());
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        System.out.println("Ignored: " + description.getClassName() + "." + description.getMethodName());
    }

    @Override
    public void testRunFinished(Result result) {
        if (failures.isEmpty()) {
            System.out.println("\n✅ All tests passed.");
        } else {
            System.out.println("\n❌ TEST FAILURES SUMMARY:");
            for (int i = 0; i < failures.size(); i++) {
                Failure failure = failures.get(i);
                Description description = failure.getDescription();
                System.out.printf("%d) %s - %s\n", i + 1, description.getClassName(), description.getMethodName());
                System.out.println("   Reason: " + failure.getMessage());
            }
        }
    }
}
