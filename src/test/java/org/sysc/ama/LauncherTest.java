package org.sysc.ama;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LauncherTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public LauncherTest (String testName) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(LauncherTest.class);
    }

    public void testAlwaysPass() {
        assertTrue(true);
    }
}
