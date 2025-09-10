/**
 * ----------------------------------------------------------------------
 * File: CommandLineIntegrationTest.java
 * Author: Sandro Fouche
 * Email: sandro.fouche@montgomerycollege.edu
 * Course: CMSC204 - Computer Science II
 * Project: UserAccessManager
 * Institution: Montgomery College
 * Year: 2025
 * @version 1.0 (Fall 2025)
 *
 * Description:
 *     CLI-based tests reimplementing public UserAccessManager functionality
 *     using full command-line interaction.
 * ----------------------------------------------------------------------
 */

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.concurrent.*;

public class CommandLineIntegrationTests {

    /**
     * Helper method to simulate a CLI session with the given input.
     * Returns the captured output as a string.
     */
    private String runCliSession(String fullInput) throws Exception {
        String classpath = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpath, "Main");
        pb.redirectErrorStream(true);
        Process process = pb.start();

        // Send all input to the process
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        writer.write(fullInput);
        writer.flush();
        writer.close(); // We signal end of input

        // Read all output from the process
        StringBuilder output = new StringBuilder();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> readerTask = executor.submit(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            } catch (IOException e) {
                output.append("ERROR reading output: ").append(e.getMessage()).append("\n");
            }
        });

        boolean finished = process.waitFor(10, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
            readerTask.cancel(true);
            fail("CLI session timed out.");
        }

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
        return output.toString();
    }

    /**
     * Returns a Java-friendly multiline string with explicit \n escapes.
     */
    private static String formatCliInput(String rawInput) {
        String[] lines = rawInput.split("\\n");
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].replace("\"", "\\\"");
            sb.append(line).append("\\n");
            if (i < lines.length - 1) {
                sb.append("\" +\n\"");
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    @Test
    public void testAddAndVerifyUser() throws Exception {
        String input = String.join("\n",
                "add testuser",
                "mypassword",
                "verify testuser",
                "mypassword",
                "exit"
        ) + "\n";

        String output = runCliSession(input);
        assertTrue(output.contains("Access verified"), "User should be able to verify login.");
    }

    @Test
    public void testDuplicateUser() throws Exception {
        String input = String.join("\n",
                "add dupe",
                "pass1",
                "add dupe",
                "pass2",
                "exit"
        ) + "\n";

        String output = runCliSession(input);
        assertTrue(
                output.toLowerCase().contains("duplicate") ||
                        output.toLowerCase().contains("already exists"),
                "Expected duplicate user error (duplicate or already exists)."
        );
    }

    @Test
    public void testInvalidPasswordFailsAccess() throws Exception {
        String input = String.join("\n",
                "add wrongpw",
                "correctpass",
                "verify wrongpw",
                "badpass",
                "exit"
        ) + "\n";

        String output = runCliSession(input);
        assertTrue(output.toLowerCase().contains("incorrect") ||
                        output.toLowerCase().contains("failed") ||
                        output.toLowerCase().contains("error"),
                "Expected message indicating incorrect password.");
    }

    @Test
    public void testRemoveUserThenFailVerify() throws Exception {
        String input = String.join("\n",
                "add removeMe",
                "letmein",
                "remove removeMe",
                "verify removeMe",
                "letmein",
                "exit"
        ) + "\n";

        String output = runCliSession(input);
        assertTrue(output.toLowerCase().contains("not found") ||
                        output.toLowerCase().contains("no such user"),
                "Expected message for user not found after removal.");
    }

    @Test
    public void testAccountEventuallyLocks() throws Exception {
        StringBuilder inputBuilder = new StringBuilder();
        inputBuilder.append("add lockuser\n");
        inputBuilder.append("lockpass\n");
        for (int i = 0; i < 5; i++) {
            inputBuilder.append("verify lockuser\n");
            inputBuilder.append("wrongpass\n");
        }
        inputBuilder.append("verify lockuser\n");
        inputBuilder.append("lockpass\n                                                                                                          ");
        inputBuilder.append("exit\n");

        String output = runCliSession(inputBuilder.toString());
        assertTrue(output.toLowerCase().contains("locked"),
                "Expected account to be locked after repeated failed attempts.");
    }
}