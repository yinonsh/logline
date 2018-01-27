package org.logline;

import org.junit.Assert;
import org.logline.logger.ILogger;

public class TestUtils {
	public static Exception assertThrownException(ILogger logger, String line,
			Class<? extends RuntimeException> exceptionClass) {
		try {
			logger.info(line);
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), exceptionClass);
			return e;
		}

		Assert.fail("Expected exception to be thrown");
		return null; // never reaches here
	}
}
