package org.logline.logger;

import org.logline.BaseLogger;

/**
 * @author Yinon Sharifi
 */

public class ConsoleLogger extends BaseLogger {
	@Override
	public void log(String message, Level level) {
		System.out.println(message);
	}
}