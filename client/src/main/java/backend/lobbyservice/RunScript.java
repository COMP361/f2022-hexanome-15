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

	private final String aPathToScript;
	private final String[] aArgs;
	private Process aProcess; // stores the process of the most recent run call

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
			if (exitCode != 0) {
				System.out.println(
						"[WARNING] Process: " + Arrays.toString(command) + " resulted in exit code: " + exitCode);
				// get error message
				InputStream errorStream = process.getErrorStream();
				for (int i = 0; i < errorStream.available(); i++) {
					System.out.println("" + errorStream.read());
				}
			}

			aProcess = process; // assign global variable
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
	public Process getProcess() {
		assert aProcess != null;
		return aProcess;
	}

	/**
	 * Destroy the stored process
	 * 
	 * @assert process != null
	 */
	public void destroy() {
		assert aProcess != null;
		aProcess.destroy();
	}

}
