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
 */
public class RunScript implements Runnable {

	private final String aPathToScript; // immutable
	private String[] aArgs; // mutable
	private InputStream aOutput; // stores the output of the most recent run call
	private int aExitCode; // stores exit code of most recent run call

	/**
	 * @param pPathToScript path to executable script
	 * @param pArgs         arguments to the script
	 */
	public RunScript(String pPathToScript, String... pArgs) {
		assert pPathToScript != null && pArgs != null;

		this.aPathToScript = pPathToScript;
		this.aArgs = pArgs;
	}

	@Override
	/**
	 * Creates a new process and executes the script with given arguments
	 */
	public void run() {

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

			aOutput = process.getInputStream(); // assign global variable
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
	 * @assert aProcess != null
	 * @return process of most recently executed task
	 */
	public InputStream getOutput() {
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

}
