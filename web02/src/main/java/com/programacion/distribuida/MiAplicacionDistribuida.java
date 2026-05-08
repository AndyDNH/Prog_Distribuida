package com.programacion.distribuida;

import io.helidon.config.Config;
import io.helidon.dbclient.DbClient;
import io.helidon.http.media.jsonb.JsonbSupport;
import io.helidon.http.media.jsonp.JsonpSupport;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class MiAplicacionDistribuida {
    static JsonBuilderFactory factory = Json.createBuilderFactory(Map.of());
    static DbClient dbClient;

    public static void handleHola1(ServerRequest req, ServerResponse res) {
        var name = req.path().pathParameters().get("name");

        JsonObject obj = factory.createObjectBuilder().
                add("name", "Hola " + name)
                .add("hora", LocalDateTime.now().toString())
                .build();

        res.send(obj);
    }

    public static void handleHola2(ServerRequest req, ServerResponse res) {
        var name = req.path().pathParameters().get("name");

        Persona obj = Persona.builder()
                .name("Hola " + name)
                .hora(LocalDate.now())
                .build();

        res.send(obj);
    }

    public static void handleBooks(ServerRequest req, ServerResponse res) {
        var books = dbClient.execute()
                .createQuery("select * from books")
                .execute()
                .map(row -> Book.builder()
                        .isbn(row.column("isbn").getString())
                        .title(row.column("title").getString())
                        .build())
                .toList();
        res.send(books);
    }

    public static void main(String[] args) {
        Config config = Config.create();
        var configHttp = config.get("server");
        var configDb = config.get("db");

        dbClient = DbClient.builder()
                .config(configDb)
                .build();

        WebServer.builder()
                //                .port(8080)
                .config(configHttp)
                .mediaContext(ctx -> ctx
                        .mediaSupportsDiscoverServices(true)
                        .addMediaSupport(JsonpSupport.create())
                        .addMediaSupport(JsonbSupport.create())
                )
                .routing(it -> it
                        .get("/hola1/{name}", MiAplicacionDistribuida::handleHola1)
                        .get("/hola2/{name}", MiAplicacionDistribuida::handleHola2)
                        .get("/books", MiAplicacionDistribuida::handleBooks)
                )
                .build()
                .start();

    }
}
