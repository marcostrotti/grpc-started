package com.github.marcostrotti.grpc.greeting.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.Operators;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello from Client");

        GreetingClient main = new GreetingClient();

        main.run();
    }

    public void run(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        //doUnaryCall(channel);
        //doServerStreamingCall(channel);
        doClientStreamingCall(channel);
        System.out.println("shutting Down Channel");
        channel.shutdown();
    }

    private void doUnaryCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Marcos")
                .setLastName("Trotti")
                .build();
        //Unary Communication
        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        GreetResponse response = greetClient.greet(greetRequest);
        System.out.println(response.getResult());
    }

    private void doServerStreamingCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
        GreetManyTimeRequest greetManyTimeRequest =  GreetManyTimeRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Marcos")
                        .setLastName("Trotti")
                        .build()
                )
                .build();
        greetClient.greetManyTimes(greetManyTimeRequest).forEachRemaining( greetManyTimeResponse -> {
            System.out.println(greetManyTimeResponse.getResult());
        });
    }

    private void doClientStreamingCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<LongGreetRequest> requestObserver = asyncClient.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse value) {
                System.out.println("Received a response from the server");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending responses");
                latch.countDown();
            }
        });

        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                                    .setFirstName("Marcos")
                                    .build())
                .build()
        );
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Benja")
                        .build())
                .build()
        );

        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
