package com.bitfinish.eventlooptest;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Nathan Hammond
 */
public class EventLoopTest {

    private static final Logger log = LoggerFactory.getLogger(EventLoopTest.class);

    private static final int INSTANCES = 2;
    private static final String VERTICLE = "com.bitfinish.eventlooptest.TestVerticle";

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        DeploymentOptions options = new DeploymentOptions()
            .setInstances(INSTANCES);

        vertx.deployVerticle(VERTICLE, options, result -> {
            if (result.failed()) {
                log.error("Error deploying verticles.", result.cause());
                vertx.close();
                System.exit(1);
            }

            log.info("Verticles successfully deployed.");
        });
    }

}
