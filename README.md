# Logline

Logline is a simple yet powerful framework that solves a very common problem - transparently simulate corner cases that require fine control of an application execution flow. For example:
* Simulate latency e.g. when accessing DB, or other remote service 
* Sync between multiple threads to simulate deadlock or count-down latches
* Simulate rare exception thrown during the execution flow
* Simulate a crash in a **specific** point of the execution flow

In the general form, Logline enables the running of any piece of code, at a specific point in the flow.

Actual log lines represents the points in the execution flow, which makes it very easy to and intuitive to use.

For example, to simulate a delay when the program logs "Fetching contacts from facebook":

```java
on("Fetching contacts from facebook").delayMillis(5000);
```
	
You can also concatenate actions which is pretty cool, and lets you simulate even more interesting use cases:

```java
on("Fetching contacts from facebook").delayMillis(60000).throw(new ConnectionTimeoutException());
```

	
For more information please see the following:
* Why should you use logline 
* Getting started
* Limitations 
* Backlog
