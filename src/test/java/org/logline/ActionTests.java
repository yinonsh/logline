package org.logline;

import static org.logline.LogLineConfiguration.on;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.logline.LogLineConfiguration.ILoggingEventFilterWrapper;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

@RunWith(Parameterized.class)
public class ActionTests {
	@Parameters
	public static Collection<Object[]> data() {
		final Logger logback = (Logger) LoggerFactory.getLogger(ActionTests.class);
		final org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(ActionTests.class);

		return Arrays.asList(new Object[][] { { new LogbackLogger(logback) } });// , { new Log4jLogger(log4j) } });
	}

	private ILogger logger;

	public ActionTests(ILogger logger) {
		this.logger = logger;
	}

	@Before
	public void clearConfiguration() {
		logger.info("Clearing logline configuration");
		LogLineConfiguration.clear();
	}

	@Test
	public void testDelayAction() {
		long delayMs = 350;

		onLogLine("foo").delay(delayMs);
		long start = System.currentTimeMillis();

		logger.info("foo");
		logger.info("bar");

		long duration = System.currentTimeMillis() - start;
		Assert.assertTrue("Expected at least a delay of " + delayMs + " ms",
				duration >= delayMs && duration < delayMs + 20);
	}

	@Test
	public void testNoDelayAction() {
		long start = System.currentTimeMillis();

		logger.info("foo");
		logger.info("bar");

		long duration = System.currentTimeMillis() - start;
		Assert.assertTrue("Expected no delay", duration >= 0 && duration < 10);
	}

	@Test
	public void testAsyncDelayAction() throws InterruptedException {
		long delayMs = 350;

		onLogLine("foo").delay(delayMs);
		CountDownLatch latch = new CountDownLatch(1);

		long start = System.currentTimeMillis();

		new Thread(() -> {
			logger.info("foo");
			latch.countDown();
		}).start();

		latch.await();

		long duration = System.currentTimeMillis() - start;
		Assert.assertTrue("Expected at least a delay of " + delayMs + " ms",
				duration >= delayMs && duration < delayMs + 20);
	}

	@Test
	public void testExceptionAction() {
		String exceptionMessage = "Dummy exception";
		onLogLine("foo").throwException(new IllegalStateException(exceptionMessage));
		try {
			logger.info("foo");
		} catch (IllegalStateException e) {
			Assert.assertEquals(e.getMessage(), exceptionMessage);
			return;
		}

		Assert.fail("Expected exception to be thrown");
	}

	private ILoggingEventFilterWrapper onLogLine(String string) {
		return on(x -> x.getFormattedMessage().contains(string));
	}

}
