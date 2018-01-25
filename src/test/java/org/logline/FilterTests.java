package org.logline;

import static org.logline.TestUtils.assertThrownException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
		LogLineConfigurations.clear();
	}

	@Test
	public void testPatternBasedFilter() {
		Map<String, List<String>> patternToMatchedMessages = new HashMap<String, List<String>>();
		Map<String, List<String>> patternToUnmatchedMessages = new HashMap<String, List<String>>();

		// Examples taken from : http://www.vogella.com/tutorials/JavaRegularExpressions/article.html

		patternToMatchedMessages.put("this is text", Arrays.asList("this is text"));
		patternToMatchedMessages.put(".*(jim|joe).*",
				Arrays.asList("humbapumpa jim", "humbapumpa joe", "humbapumpa joe jim", "humbapumpa joe aaaa"));
		patternToMatchedMessages.put("\\d\\d\\d([,\\s])?\\d\\d\\d\\d", Arrays.asList("1233323", "123 3323"));

		patternToUnmatchedMessages.put("this is text",
				Arrays.asList("thisistext", "foo this is text  foo", "foo this is text", "this is text  foo", ""));
		patternToUnmatchedMessages.put(".*(jim|joe).*", Arrays.asList("humbapumpa ji", "humbapumpa jom", "aaaaaaaaa"));
		patternToUnmatchedMessages.put("\\d\\d\\d([,\\s])?\\d\\d\\d\\d", Arrays.asList("1233323322"));
		for (String pattern : patternToUnmatchedMessages.keySet()) {
			Pattern p = Pattern.compile(pattern);
			LogLineConfigurations.on(new PatternBasedLoggingEventFilter(p)).throwException(new IllegalStateException());

			for (String match : patternToUnmatchedMessages.get(pattern)) {
				logger.info(match);
			}
		}

		for (String pattern : patternToMatchedMessages.keySet()) {
			Pattern p = Pattern.compile(pattern);
			LogLineConfigurations.on(new PatternBasedLoggingEventFilter(p)).throwException(new IllegalStateException());

			for (String match : patternToMatchedMessages.get(pattern)) {
				assertThrownException(logger, match, IllegalStateException.class);
			}
		}

	}

	@Test
	public void testExactMessageFilter() {
		LogLineConfigurations.on(new ExactMessageLoggingEventFilter("foo")).throwException(new IllegalStateException());

		logger.info("foooo");
		logger.info("oofoo");

		assertThrownException(logger, "foo", IllegalStateException.class);
	}

	@Test
	public void testStartWithMessageFilter() {
		LogLineConfigurations.on(new StartsWithMessageLoggingEventFilter("foo"))
				.throwException(new IllegalStateException());

		logger.info("oofoo");
		assertThrownException(logger, "fooooooo", IllegalStateException.class);
	}

	public void testMultipleConfigurationsInAdditionToDefault() {

	}

	public void testMutlipleConfigurationsOrder() {

	}

	public void testConflictingConfigurations() {
		LogLineConfiguration conf1 = new LogLineConfiguration("conf1");
		conf1.on(new ExactMessageLoggingEventFilter("foo")).throwException(new IllegalStateException());

		LogLineConfiguration conf2 = new LogLineConfiguration("conf2");
		conf2.on(new ExactMessageLoggingEventFilter("foo")).throwException(new NullPointerException());

	}

	@Test
	public void testNamedConfiguration() {
		LogLineConfiguration conf1 = new LogLineConfiguration("conf1");
		conf1.on(new ExactMessageLoggingEventFilter("foo")).throwException(new IllegalStateException());

		LogLineConfiguration conf2 = new LogLineConfiguration("conf2");
		conf2.on(new ExactMessageLoggingEventFilter("bar")).throwException(new IllegalStateException());

		LogLineConfigurations.addConfiguration(conf1);
		LogLineConfigurations.addConfiguration(conf2);
		assertThrownException(logger, "foo", IllegalStateException.class);
		assertThrownException(logger, "bar", IllegalStateException.class);
	}

	public void testDisableConfiguration() {

	}
}
