package com.bitfinish.eventlooptest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Nathan Hammond
 */
public class TestVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(TestVerticle.class);

    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1/management";
    private static final String JDBC_DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static final String JDBC_USER_NAME = "nhammond";
    private static final String JDBC_PASSWORD = "Think1";
    private static final String MARKER = "marker";

    private static AtomicInteger id = new AtomicInteger(0);

    @Override
    public void start(Future<Void> future) {

        JDBCClient client;
        int verticleId = id.getAndIncrement();


        try {
            JsonObject jdbcConfig = new JsonObject()
                .put("url", JDBC_URL)
                .put("driver_class", JDBC_DRIVER_CLASS)
                .put("user", JDBC_USER_NAME)
                .put("password", JDBC_PASSWORD);

            client = JDBCClient.createShared(vertx, jdbcConfig);

        } catch (Throwable t) {
            future.fail(t);
            return;
        }

        future.succeeded();

        vertx.getOrCreateContext().put(MARKER, "found");

        log.info(String.format("[%s] [%d] pre-connection Context id=%08x, marker=%s",
            Thread.currentThread().getName(),
            verticleId,
            System.identityHashCode(vertx.getOrCreateContext()),
            vertx.getOrCreateContext().get(MARKER)));


        client.getConnection(connectionResult -> {
            if (connectionResult.failed()) {
                log.error("JDBC getConnection failed");
                return;
            }

            log.info(String.format("[%s] [%d] post-connection Context id=%08x, marker=%s",
                Thread.currentThread().getName(),
                verticleId,
                System.identityHashCode(vertx.getOrCreateContext()),
                vertx.getOrCreateContext().get(MARKER)));

            SQLConnection connection = connectionResult.result();
            connection.query("SELECT * FROM NOTABLE WHERE ID = NOTFOUND", queryResult -> {
                log.info(String.format("[%s] [%d] post-query Context id=%08x, marker=%s",
                    Thread.currentThread().getName(),
                    verticleId,
                    System.identityHashCode(vertx.getOrCreateContext()),
                    vertx.getOrCreateContext().get(MARKER)));
                client.close();
            });
        });

    }

}
