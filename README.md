# Logline

Logline is a simple yet powerful framework that solves a very common problem - reproducing use cases that require fine control of an application execution flow. Such use cases include:

* A hanging thread that should be identified by a watchdog
* Sync between multiple threads to simulate a deadlock or count-down latches
* A high latency when accessing DB or other remote service 
* A crash in a specific point of the execution flow, to test a recovery or an upgrade from that specific point
* Throwing of a rare exception

In the general form, Logline enables the running of any piece of code (action), at a specific point in the flow (filter), without modifying the actual execution flow. The concept is somewhat similar to AOP, except that actual log lines represents these points in the execution flow, which makes it simpler to use and to configure.

For example, to simulate a latency when the program fetches contacts from facebook i.e. when logging logging "Fetching FB contacts":

```java
on("Fetching FB contacts").delayMillis(5000);
```
	
You can also concatenate actions which is pretty cool, and lets you simulate even more interesting use cases:

```java
on("Fetching FB contacts").delayMillis(60000).throw(new ConnectionTimeoutException());
```
For more information, please visit:
* Why should you use logline 
* Getting started
* Limitations 
* Backlog
