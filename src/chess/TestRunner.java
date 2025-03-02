package chess;

import org.junit.runner.JUnitCore;

public class TestRunner {
    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new VerboseRunListener()); // Attach verbose listener
        junit.run(ChessTest.class); // Replace with your test class
    }
}