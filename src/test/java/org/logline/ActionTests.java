package org.logline;

import static org.logline.LogLineConfiguration.on;

import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.logline.LogLineConfiguration.ILoggingEventFilterWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionTests {
	public static Logger log = LoggerFactory.getLogger(ActionTests.class);

	@Before
	public void clearConfiguration() {
		log.info("Clearing logline configuration");
		LogLineConfiguration.clear();
	}

	@Test
	public void testDelayAction() {
		long delayMs = 350;

		onLogLine("foo").delay(delayMs);
		long start = System.currentTimeMillis();

		log.info("foo");
		log.info("bar");

		long duration = System.currentTimeMillis() - start;
		Assert.assertTrue("Expected at least a delay of " + delayMs + " ms",
				duration >= delayMs && duration < delayMs + 20);
	}

	@Test
	public void testNoDelayAction() {
		long start = System.currentTimeMillis();

		log.info("foo");
		log.info("bar");

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
			log.info("foo");
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
			log.info("foo");
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
