package com.github.marcostrotti.grpc.calculator.server;

import com.github.marcostrotti.grpc.greeting.server.GreetServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CalculatorServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(50051)
                .addService(new CalculatorServiceImpl())
                .build();
        server.start();
        System.out.println("Calculator Start");
        Runtime.getRuntime().addShutdownHook(new Thread( () ->{
            System.out.println("Recived shutdown");
            server.shutdown();
            System.out.println("Shutdown Complete");
        } ));

        server.awaitTermination();
    }
}

