package com.github.marcostrotti.grpc.greeting.server;

import com.github.marcostrotti.grpc.calculator.server.CalculatorServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GreetingServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello");
        Server server = ServerBuilder.forPort(50051)
                .addService(new GreetServiceImpl())
                .addService(new CalculatorServiceImpl())
                .build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( () ->{
            System.out.println("Recived shutdown");
            server.shutdown();
            System.out.println("Shutdown Complete");
        } ));

        server.awaitTermination();
    }
}

