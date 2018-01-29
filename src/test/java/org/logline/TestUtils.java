package org.logline;

import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.junit.Assert;
import org.logline.logger.ILogger;

public class TestUtils {
	public static Exception assertThrownException(ILogger logger, String line,
			Class<? extends RuntimeException> exceptionClass) {
		try {
			logger.info(line);
		} catch (AppenderLoggingException ex) {
			Assert.assertEquals(ex.getCause().getClass(), exceptionClass);
			return (Exception) ex.getCause();
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), exceptionClass);
			return e;
		}

		Assert.fail("Expected exception to be thrown");
		return null; // never reaches here
	}
}
