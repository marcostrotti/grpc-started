package com.github.marcostrotti.grpc.greeting.server;

import com.proto.greet.*;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        Greeting greeting = request.getGreeting();
        String result = "Hello "+ greeting.getFirstName() + " " + greeting.getLastName();
        GreetResponse response = GreetResponse.newBuilder()
                .setResult(result)
                .build();
        //send the response
        responseObserver.onNext(response);
        //complete the response
        responseObserver.onCompleted();
    }

    @Override
    public void greetManyTimes(GreetManyTimeRequest request, StreamObserver<GreetManyTimeResponse> responseObserver) {
        Greeting greeting = request.getGreeting();

        try {
            for (int i=0; i<10;i++){
                String result = "Hello "+ greeting.getFirstName() + " " + greeting.getLastName() + " response number: "+ i;
                GreetManyTimeResponse greetManyTimeResponse = GreetManyTimeResponse.newBuilder()
                        .setResult(result)
                        .build();
                responseObserver.onNext(greetManyTimeResponse);

                    Thread.sleep(2000L);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            responseObserver.onCompleted();
        }

    }

    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
        StreamObserver<LongGreetRequest> requestObserver = new StreamObserver<LongGreetRequest>() {

            String result = "";

            @Override
            public void onNext(LongGreetRequest value) {
                result += "Hello "+ value.getGreeting().getFirstName() + " !";
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(LongGreetResponse.newBuilder()
                        .setResult(result)
                        .build());
                responseObserver.onCompleted();
            }
        };
        return requestObserver;
    }
}