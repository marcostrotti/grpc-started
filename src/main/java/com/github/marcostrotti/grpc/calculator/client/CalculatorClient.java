package com.github.marcostrotti.grpc.calculator.client;

import com.proto.calculator.*;
import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorclient = CalculatorServiceGrpc.newBlockingStub(channel);
        Operators sum = Operators.newBuilder()
                .setA(10)
                .setB(5)
                .build();
        SumRequest sumRequest = SumRequest.newBuilder()
                .setSum(sum)
                .build();
        SumResponse sumResponse = calculatorclient.sum(sumRequest);
        System.out.println(sumResponse.getResult());

        System.out.println("Prime Decomposition");
        calculatorclient.primeNumberDecomposition(PrimeNumberDecompositionRequest.newBuilder()
                .setNumber(120)
                .build()).forEachRemaining( primeNumberDecompositionResponse ->  {
                          System.out.println(primeNumberDecompositionResponse.getResult());
        });
        System.out.println("shutting Down Channel");
        channel.shutdown();
    }
}
