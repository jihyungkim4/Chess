package chess;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class VerboseRunListener extends RunListener {

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
        System.err.println(
                "FAILED: " + failure.getDescription().getClassName() + "." + failure.getDescription().getMethodName());
        System.err.println("Reason: " + failure.getMessage());
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        System.out.println("Ignored: " + description.getClassName() + "." + description.getMethodName());
    }
}
