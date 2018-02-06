# Logline

Logline is a simple yet powerful framework that solves a very common problem - reproducing use cases that require fine control of an application execution flow. Such use cases include:

* A hanging thread that should be identified by a watchdog
* Sync between multiple threads to simulate a deadlock or count-down latches
* A high latency when accessing DB or other remote service 
* A crash in a specific point of the execution flow, to test a recovery or an upgrade from that specific point
* Throwing of a rare exception

In the general form, Logline enables the running of any piece of code (action), at a specific point in the flow (filter), without modifying the actual execution flow.

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

For readability assume 
```java
import static org.logline.LogLineConfigurationRegistry.*;
```

* Crashing the process

```java
on("Done phase A of processing the trade data").exit(0);
```
or even crash it in the middle of the process, to check the recovery.

* Taking thread dump when failed to take a connection from pool 

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

Logline supports multiple sets of configuration of filters and actions. A configuration is enabled and "registered" for use on creation, but can be managed separately from other configurations.

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
* Support Java 6 and up
