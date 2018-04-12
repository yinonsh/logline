# Logline

Logline is a simple yet powerful framework that integrates with existing logging framework (logback or log4j), to solves a very common problem - reproduction of use cases that require fine control of an application execution flow. For example:

* A deadlock between multiple threads
* A hanging thread
* Increased latency when accessing DB or other remote service 
* A crash in a specific point of the execution flow
* Throwing of a rare exception

Logline is mostly used to develop automation tests, or as a tool for operations to analyze or reproduce incidents.

In the general form, Logline enables the running of any piece of code (action), at a specific point in the flow (filter), by leveraging existing log messages, without modifying the actual execution flow

For example, to simulate a latency when fetching contacts from facebook:

```java
on("Fetching FB contacts").delayMillis(5000);
```

You can also concatenate actions which is pretty cool, and lets you simulate even more interesting use cases:

```java
on("Fetching FB contacts").delayMillis(60000).throwException(new ConnectionTimeoutException());
```

## Getting Started

Only 2 steps and you're ready to go !
1. Get the Code
2. Configure your existing logging framework

See [Getting Started](https://github.com/yinonsh/logline/wiki/Getting-Started)

## Usage

For readability assume the following import
```java
import static org.logline.LogLineConfigurationRegistry.*;
```

* Crashing the entire Java process

```java
on("Done phase A of processing the trade data").exit(0);
```
It's useful for example to check for proper recovery.

* Taking a thread dump when failed to take a connection from pool 

```java
on("Failed to acquire DB connection").threadDump(logger) 
```
The stack trace is logged using the provided logger.

* Syncing between running threads, and have thread A wait for thread B:

```java
Object event = new Object()
on("foo").waitFor(event); // "foo" is logged on thread A flow
on("bar").notifyOf(event); // "bar" is logged on thread B flow
```

Thread A will wait when logging "foo", until thread B will log "bar".

* Running a given runnable when logging a message that matches a given pattern:

```java
onMatch(pattern).run(runnable);
```

* Taking snapshot and emailing QA/Support when a log starts with "Unexpected":

```java
onStartWith("Unexpected").run(() -> {
	takeSnapshot();
        notifySupportOrQa();
});
```

## Configuration

Logline supports multiple sets of configuration for filters and actions. A configuration is enabled and "registered" for use on creation, but can be managed separately from other configurations.

```java
LogLineConfiguration conf1 = new LogLineConfiguration("conf1");
conf1.on("foo").throwException(new IllegalStateException());

LogLineConfiguration conf2 = new LogLineConfiguration("conf2");
conf2.onStartWith("bar").delayMillis(1000);
conf2.disable(); // The configuration was enabled by default
```

A default configuration exist and can be managed similarly. To get it explicitly one can use

```java
LogLineConfigurationRegistry.getDefault();
```

## Notes

* The order of actions is important - an action that throws an exception will lead to skipping further actions.
* Actions are called only for messages with level higher than threshold
* Messages can be logged inside actions, but the filters will not be applied recursively
* Log4j2 doesnt support the throw-exception action correctly. The thrown exception is wrapped with AppenderLoggingException
* Time penalty - highly depends on the configured filters and actions.
* Java 6 and up is supported
