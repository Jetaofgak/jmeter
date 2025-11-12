package com.benchmark;

import com.benchmark.util.EMF;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import java.net.URI;

public class Main {

    public static final String BASE_URI = "http://localhost:8081/";

    public static HttpServer startServer() {
        final Application application = new Application();
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), application, false);

        // Désactiver la page d'erreur HTML de Grizzly
        server.getServerConfiguration().setPassTraceRequest(true);
        server.getServerConfiguration().setDefaultErrorPageGenerator(null);

        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException("Failed to start server", e);
        }

        return server;
    }

    public static void main(String[] args) {
        try {
            final HttpServer server = startServer();

            System.out.println("========================================");
            System.out.println("Jersey JAX-RS Application started!");
            System.out.println("Base URI: " + BASE_URI);
            System.out.println("========================================");
            System.out.println("Endpoints disponibles:");
            System.out.println("  GET    " + BASE_URI + "categories");
            System.out.println("  GET    " + BASE_URI + "categories/{id}");
            System.out.println("  GET    " + BASE_URI + "categories/code/{code}");
            System.out.println("  POST   " + BASE_URI + "categories");
            System.out.println("  PUT    " + BASE_URI + "categories/{id}");
            System.out.println("  DELETE " + BASE_URI + "categories/{id}");
            System.out.println("  GET    " + BASE_URI + "items");
            System.out.println("  GET    " + BASE_URI + "items/{id}");
            System.out.println("  GET    " + BASE_URI + "items/sku/{sku}");
            System.out.println("  GET    " + BASE_URI + "items/category/{categoryId}");
            System.out.println("  POST   " + BASE_URI + "items");
            System.out.println("  PUT    " + BASE_URI + "items/{id}");
            System.out.println("  DELETE " + BASE_URI + "items/{id}");
            System.out.println("========================================");
            System.out.println("Press ENTER to stop the server...");
            System.in.read();

            server.shutdownNow();
            EMF.close();
            System.out.println("Server stopped.");

        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
