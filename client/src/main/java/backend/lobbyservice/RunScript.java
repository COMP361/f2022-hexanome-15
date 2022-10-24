/**
 * Oct 24, 2022
 * TODO
 */
package backend.lobbyservice;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author zacharyhayden
 * @see Cannot be sub-classed.
 */
public final class RunScript {
	/**
	 * 
	 * @param pPathToScript path to executable script
	 * @param pArgs         arguments for the script, can be null if no arguments
	 * @return process from executing the runtime
	 */
	public static Process exec(String pPathToScript, String... pArgs) {
		String[] command;
		if (pArgs == null) {
			command = new String[] { pPathToScript };
		} else {
			command = new String[1 + pArgs.length];
			command[0] = pPathToScript;
			// fill array with script arguments
			for (int i = 0; i < pArgs.length && (i + 1) < command.length; i++) {
				command[i + 1] = pArgs[i];
			}
		}

		try {
			// execute the command
			Process process = Runtime.getRuntime().exec(command);
			// handle exit code
			int exitCode = process.waitFor();
			if (exitCode != 0) {
				System.out.println("[WARNING] Process: " + Arrays.toString(command) + " resulted in exit code: " + exitCode);
			}
			return process;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			System.exit(1);
			e.printStackTrace();
		}

		return null;
	}

}
