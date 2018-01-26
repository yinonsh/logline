package org.logline;

import static org.logline.LogLineConfigurationRegistry.on;
import static org.logline.TestUtils.assertThrownException;

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

/**
 * @author Yinon Sharifi
 */

@RunWith(Parameterized.class)
public class ActionTests {
	@Parameters
	public static Collection<Object[]> data() {
		final Logger logback = (Logger) LoggerFactory.getLogger(ActionTests.class);
		final org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(ActionTests.class);

		return Arrays.asList(new Object[][] { { new LogbackLogger(logback) }, { new Log4jLogger(log4j) } });
		// return Arrays.asList(new Object[][] { { new Log4jLogger(log4j) } });
		// return Arrays.asList(new Object[][] { { new LogbackLogger(logback) }});
	}

	private ILogger logger;

	public ActionTests(ILogger logger) {
		this.logger = logger;
	}

	@Before
	public void clearConfiguration() {
		logger.info("Clearing logline configuration");
		LogLineConfigurationRegistry.clear();
	}

	@Test
	public void testDelayAction() {
		long delayMs = 350;

		onLogLine("foo").delayMillis(delayMs);
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

		onLogLine("foo").delayMillis(delayMs);
		final CountDownLatch latch = new CountDownLatch(1);

		long start = System.currentTimeMillis();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				logger.info("foo");
				latch.countDown();
			}
		};

		new Thread(runnable).start();

		latch.await();

		long duration = System.currentTimeMillis() - start;
		Assert.assertTrue("Expected at least a delay of " + delayMs + " ms",
				duration >= delayMs && duration < delayMs + 20);
	}

	@Test
	public void testExceptionAction() {
		String exceptionMessage = "Dummy exception";
		onLogLine("foo").throwException(new IllegalStateException(exceptionMessage));

		assertThrownException(logger, "foo", IllegalStateException.class);
	}

	@Test
	public void testConcatenateActions() {
		String exceptionMessage = "Dummy exception";
		long delayMs = 275;
		onLogLine("foo-exception").delayMillis(delayMs).throwException(new IllegalStateException(exceptionMessage));

		long start = System.currentTimeMillis();

		Exception e = assertThrownException(logger, "foo-exception", IllegalStateException.class);
		Assert.assertEquals(e.getMessage(), exceptionMessage);
		long duration = System.currentTimeMillis() - start;
		Assert.assertTrue("Expected at least a delay of " + delayMs + " ms",
				duration >= delayMs && duration < delayMs + 20);
	}

	@Test
	public void tesActionsOnDifferentMessageLevels() {
		onLogLine("foo").throwException(new IllegalStateException());
		logger.debug("foo");
		assertThrownException(logger, "foo", IllegalStateException.class);
	}

	private ILoggingEventFilterWrapper onLogLine(final String line) {
		return on(new ILoggingEventFilter() {

			@Override
			public boolean accept(ILoggingEvent event) {
				return event.getFormattedMessage().contains(line);
			}
		});
	}

}
