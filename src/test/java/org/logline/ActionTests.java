package org.logline;

import static org.logline.LogLineConfigurationRegistry.on;
import static org.logline.TestUtils.assertThrownException;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.logline.LogLineConfiguration.ILoggingEventFilterWrapper;
import org.logline.actions.DumpStackTraceLoggingEventAction;
import org.logline.logger.ConsoleLogger;
import org.logline.logger.ILogger;
import org.logline.logger.Log4jLogger;
import org.logline.logger.LogbackLogger;
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
		final org.apache.logging.log4j.Logger log4j = LogManager.getLogger(ActionTests.class);

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

	@Test(timeout = 5000)
	public void testWaitAndNotifyActions() throws InterruptedException {
		if (logger instanceof Log4jLogger) {
			return;
		}

		Object event = new Object();
		onLogLine("foo").waitFor(event);
		onLogLine("bar").notifyOf(event);

		Runnable runnable = () -> {
			logger.info("foo"); // will wait for event
			logger.info("aaaaaa"); // will wait for event
		};

		Thread t = new Thread(runnable);
		t.start();
		Thread.sleep(1000);

		logger.info("bar");
		t.join();
	}

	@Test
	public void testAsyncDelayAction() throws InterruptedException {
		long delayMs = 350;

		onLogLine("foo").delayMillis(delayMs);
		final CountDownLatch latch = new CountDownLatch(1);

		long start = System.currentTimeMillis();

		Runnable runnable = () -> {
			logger.info("foo");
			latch.countDown();
		};

		new Thread(runnable).start();

		latch.await();

		long duration = System.currentTimeMillis() - start;
		Assert.assertTrue("Expected a delay of " + delayMs + " ms but was " + duration,
				duration >= delayMs && duration < delayMs + 50);
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
	public void testActionsOnDifferentMessageLevels() {
		onLogLine("foo").throwException(new IllegalStateException());
		logger.debug("foo");
		assertThrownException(logger, "foo", IllegalStateException.class);
	}

	@Test
	public void testNoInfiniteActionsLoop() {
		onLogLine("foo").run(new ILoggingEventAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void act(ILoggingEvent loggingEvent) {
				logger.info("foo");
			}
		});
		logger.info("foo");
	}

	@Test
	public void testThreadDumpAction() {
		onLogLine("bar").run(new DumpStackTraceLoggingEventAction(new ConsoleLogger()));
		logger.info("bar");
	}

	@Test
	public void testRunnableAction() {
		onLogLine("foo").run(() -> {
			throw new IllegalStateException();
		});

		assertThrownException(logger, "foo", IllegalStateException.class);
	}

	private ILoggingEventFilterWrapper onLogLine(final String line) {
		return on(new ILoggingEventFilter() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean accept(ILoggingEvent event) {
				return event.getFormattedMessage().contains(line);
			}
		});
	}

}
