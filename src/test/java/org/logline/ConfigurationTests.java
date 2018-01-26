package org.logline;

import static org.logline.TestUtils.assertThrownException;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.logline.filters.ExactMessageLoggingEventFilter;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author Yinon Sharifi
 */

@RunWith(Parameterized.class)
public class ConfigurationTests {

	private ILogger logger;

	@Parameters
	public static Collection<Object[]> data() {
		final Logger logback = (Logger) LoggerFactory.getLogger(ActionTests.class);
		final org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(ActionTests.class);

		return Arrays.asList(new Object[][] { { new LogbackLogger(logback) }, { new Log4jLogger(log4j) } });
	}

	public ConfigurationTests(ILogger logger) {
		this.logger = logger;
	}

	@Before
	public void clearConfiguration() {
		logger.info("Clearing logline configuration");
		LogLineConfigurationRegistry.clear();
	}

	@Test
	public void testOrderOfConfigurations() {
		LogLineConfiguration conf1 = new LogLineConfiguration("conf1");
		conf1.on(new ExactMessageLoggingEventFilter("foo")).throwException(new IllegalStateException());

		assertThrownException(logger, "foo", IllegalStateException.class);

		LogLineConfiguration conf2 = new LogLineConfiguration("conf2");
		conf2.on(new ExactMessageLoggingEventFilter("foo")).throwException(new NullPointerException());

		assertThrownException(logger, "foo", NullPointerException.class);
	}

	@Test
	public void testNamedConfiguration() {
		LogLineConfiguration conf1 = new LogLineConfiguration("conf1");
		conf1.on(new ExactMessageLoggingEventFilter("foo")).throwException(new IllegalStateException());

		LogLineConfiguration conf2 = new LogLineConfiguration("conf2");
		conf2.on(new ExactMessageLoggingEventFilter("bar")).throwException(new IllegalStateException());

		LogLineConfigurationRegistry.on(new ExactMessageLoggingEventFilter("baz"))
				.throwException(new IllegalArgumentException());

		assertThrownException(logger, "foo", IllegalStateException.class);
		assertThrownException(logger, "bar", IllegalStateException.class);
		assertThrownException(logger, "baz", IllegalArgumentException.class);
	}

	@Test
	public void testDisableConfiguration() {
		LogLineConfiguration conf1 = new LogLineConfiguration("conf1");
		conf1.on(new ExactMessageLoggingEventFilter("foo")).throwException(new IllegalStateException());

		LogLineConfiguration conf2 = new LogLineConfiguration("conf2");
		conf2.on(new ExactMessageLoggingEventFilter("foo")).throwException(new NullPointerException());

		assertThrownException(logger, "foo", NullPointerException.class);
		conf2.setEnabled(false);
		assertThrownException(logger, "foo", IllegalStateException.class);
		conf1.setEnabled(false);
		logger.info("foo");

		conf1.setEnabled(true);
		assertThrownException(logger, "foo", IllegalStateException.class);
		conf2.setEnabled(true);
		assertThrownException(logger, "foo", NullPointerException.class);
	}

	@Test
	public void testRegisterOperations() {
		LogLineConfiguration conf1 = new LogLineConfiguration("conf1");
		conf1.on(new ExactMessageLoggingEventFilter("foo")).throwException(new IllegalStateException());
		assertThrownException(logger, "foo", IllegalStateException.class);
		LogLineConfigurationRegistry.unregister("conf1");
		logger.info("foo");

		LogLineConfigurationRegistry.register(conf1);

		assertThrownException(logger, "foo", IllegalStateException.class);
	}
}
