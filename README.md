# vertx-eventloop-test

Vert.x eventloop handler test

The eventloop test is a Gradle project that contains a main class and one verticle (TestVerticle).  The main class starts two instances
of the verticle.  The verticle obtains a JDBC connection and performs a query.  A number of info messages
are printed showing the Context and thread on which the Verticle and the handlers execute.

Sample output:

<pre>
$ java -jar build/libs/vertx-eventloop-test-1.0-SNAPSHOT-fat.jar 
Aug 17, 2016 5:03:33 PM com.mchange.v2.log.MLog 
INFO: MLog clients using java 1.4+ standard logging.
Aug 17, 2016 5:03:33 PM com.mchange.v2.c3p0.C3P0Registry 
INFO: Initializing c3p0-0.9.5.2 [built 08-December-2015 22:06:04 -0800; debug? true; trace: 10]
Aug 17, 2016 5:03:33 PM com.bitfinish.eventlooptest.TestVerticle
INFO: [vert.x-eventloop-thread-0] [0] pre-connection Context id=5f7dfcc8, marker=found
Aug 17, 2016 5:03:33 PM com.bitfinish.eventlooptest.TestVerticle
INFO: [vert.x-eventloop-thread-1] [1] pre-connection Context id=3bbdb42f, marker=found
Aug 17, 2016 5:03:33 PM com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource 
[snip]
Aug 17, 2016 5:03:33 PM com.bitfinish.eventlooptest.TestVerticle
INFO: [vert.x-eventloop-thread-1] [1] post-connection Context id=3bbdb42f, marker=found
Aug 17, 2016 5:03:33 PM com.bitfinish.eventlooptest.TestVerticle
INFO: [vert.x-eventloop-thread-0] [0] post-connection Context id=5f7dfcc8, marker=found
Aug 17, 2016 5:03:33 PM com.bitfinish.eventlooptest.TestVerticle
INFO: [vert.x-eventloop-thread-2] [0] post-query Context id=383c5de9, marker=null
Aug 17, 2016 5:03:33 PM com.bitfinish.eventlooptest.TestVerticle
INFO: [vert.x-eventloop-thread-3] [1] post-query Context id=035509c7, marker=null
</pre>

Note that that Verticle [0] executes on thread-0 except for the query handler, which changes to 
thread-2.  The Context changes as well as noted by the object id and the marker value stored in the Context.

To run the 3.3.2 test:

1. Set the JDBC variables in TestVerticle.java for your location environment.  No updates are made to the database.
2. gradle shadowjar
3. java -jar build/libs/vertx-eventloop-test-1.0-SNAPSHOT-fat.jar
4. Control-C to terminate

To run the 3.3.1 test:

1. Change the vertxVersion variable in build.grade to "3.3.1".
2. Follow the 3.3.2 steps above.



