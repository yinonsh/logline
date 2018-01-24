package org.logline;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.logline.filters.ExactMessageLoggingEventFilter;
import org.logline.filters.PatternBasedLoggingEventFilter;
import org.logline.filters.StartsWithMessageLoggingEventFilter;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author Yinon Sharifi
 */

@RunWith(Parameterized.class)
public class FilterTests {

	private ILogger logger;

	@Parameters
	public static Collection<Object[]> data() {
		final Logger logback = (Logger) LoggerFactory.getLogger(ActionTests.class);
		final org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(ActionTests.class);

		return Arrays.asList(new Object[][] { { new LogbackLogger(logback) }, { new Log4jLogger(log4j) } });
	}

	public FilterTests(ILogger logger) {
		this.logger = logger;
	}

	@Before
	public void clearConfiguration() {
		logger.info("Clearing logline configuration");
		LogLineConfiguration.clear();
	}

	@Test
	public void testPatternBasedFilter() {
		Map<String, List<String>> patternToMatchedMessages = new HashMap<>();
		Map<String, List<String>> patternToUnmatchedMessages = new HashMap<>();

		patternToMatchedMessages.put("this is text", Arrays.asList("this is text"));
		patternToMatchedMessages.put(".*(jim|joe).*",
				Arrays.asList("humbapumpa jim", "humbapumpa joe", "humbapumpa joe jim", "humbapumpa joe aaaa"));

		patternToUnmatchedMessages.put("this is text",
				Arrays.asList("thisistext", "foo this is text  foo", "foo this is text", "this is text  foo"));
		patternToUnmatchedMessages.put(".*(jim|joe).*", Arrays.asList("humbapumpa ji", "humbapumpa jom", "aaaaaaaaa"));

		for (String pattern : patternToUnmatchedMessages.keySet()) {
			Pattern p = Pattern.compile(pattern);
			LogLineConfiguration.on(new PatternBasedLoggingEventFilter(p)).throwException(new IllegalStateException());

			for (String match : patternToUnmatchedMessages.get(pattern)) {
				logger.info(match);
			}
		}

		for (String pattern : patternToMatchedMessages.keySet()) {
			Pattern p = Pattern.compile(pattern);
			LogLineConfiguration.on(new PatternBasedLoggingEventFilter(p)).throwException(new IllegalStateException());

			for (String match : patternToMatchedMessages.get(pattern)) {
				try {
					logger.info(match);
				} catch (IllegalStateException e) {
					continue;
				}

				Assert.fail("Expected exception to be thrown");
			}
		}

	}

	@Test
	public void testExactMessageFilter() {
		LogLineConfiguration.on(new ExactMessageLoggingEventFilter("foo")).throwException(new IllegalStateException());

		logger.info("foooo");
		logger.info("oofoo");

		try {
			logger.info("foo");
		} catch (IllegalStateException e) {
			return;
		}

		Assert.fail("Expected exception to be thrown");
	}

	@Test
	public void testStartWithMessageFilter() {
		LogLineConfiguration.on(new StartsWithMessageLoggingEventFilter("foo"))
				.throwException(new IllegalStateException());

		logger.info("oofoo");

		try {
			logger.info("fooooooo");
		} catch (IllegalStateException e) {
			return;
		}

		Assert.fail("Expected exception to be thrown");
	}
}
