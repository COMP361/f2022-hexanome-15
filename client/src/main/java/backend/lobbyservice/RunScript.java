/**
 * Oct 24, 2022
 * TODO
 */
package backend.lobbyservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author zacharyhayden
 * @implSpec Must define a private run method that'll be called from Executor
 */
public class RunScript {

	private final String aPathToScript; // immutable
	private String[] aArgs; // mutable
	private Object aOutput; // stores the output of the most recent run call
	private int aExitCode; // stores exit code of most recent run call
	private final OutputParser aParser;

	/**
	 * @param pPathToScript path to executable script
	 * @param pArgs         arguments to the script
	 */
	public RunScript(String pPathToScript, OutputParser pParser, String... pArgs) {
		assert pPathToScript != null && pArgs != null && pParser != null;

		this.aPathToScript = pPathToScript;
		this.aArgs = pArgs;
		this.aParser = pParser;
	}

	/**
	 * Creates a new process and executes the script with given arguments.
	 * 
	 * @implNote Can only be executed in LobbyServiceExecutor via execute method.
	 */
	@SuppressWarnings("unused")
	private void run() {

		String[] command;
		if (aArgs == null) {
			command = new String[] { aPathToScript };
		} else {
			command = new String[1 + aArgs.length];
			command[0] = aPathToScript;
			// fill array with script arguments
			for (int i = 0; i < aArgs.length && (i + 1) < command.length; i++) {
				command[i + 1] = aArgs[i];
			}
		}

		try {
			// execute the command
			Process process = Runtime.getRuntime().exec(command);

			// handle exit code
			int exitCode = process.waitFor();
			aExitCode = exitCode; // assign global variable

			if (exitCode != 0) {
				System.out.println(
						"[WARNING] Process: " + Arrays.toString(command) + " resulted in exit code: " + exitCode);
				// get error message
				InputStream errorStream = process.getErrorStream();
				for (int i = 0; i < errorStream.available(); i++) {
					System.out.println("" + errorStream.read());
				}
			}

			// get parsed output if parser is not null parser; else nothing happens
			if (aParser != NullParser.NULLPARSER) {
				aOutput = aParser.parse(process.getInputStream()); // assign global variable
			}

			// kill process
			process.destroy();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * @assert aOutput != null
	 * @return process of most recently executed task
	 */
	public Object getOutput() {
		assert aOutput != null;
		return aOutput;
	}

	/**
	 * Updates the arguments to be called with the script given at initialization.
	 * 
	 * @param pArgs arguments
	 * @assert pArgs != null
	 */
	public void updateArgs(String[] pArgs) {
		assert pArgs != null;
		aArgs = pArgs;
	}

	/**
	 * @return exit code of most recently executed process
	 */
	public int exitCode() {
		return aExitCode;
	}

	@Override
	public String toString() {
		String command = aPathToScript;
		for (int i = 0; i < aArgs.length; i++) {
			command += (" " + aArgs[i]);
		}
		return command;
	}

}
