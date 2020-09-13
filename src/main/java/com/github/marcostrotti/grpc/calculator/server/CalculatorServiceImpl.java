package com.github.marcostrotti.grpc.calculator.server;

import com.proto.calculator.*;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        Operators sumOperators = request.getSum();

        SumResponse response =  SumResponse.newBuilder()
                .setResult(sumOperators.getA() + sumOperators.getB())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void primeNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
        int divisor = 2;
        int number = request.getNumber();
        try {
            while (number > 1) {
                if (number % divisor == 0) {
                    PrimeNumberDecompositionResponse response = PrimeNumberDecompositionResponse.newBuilder()
                            .setResult(divisor)
                            .build();
                    responseObserver.onNext(response);
                    Thread.sleep(1000L);
                    number = number / divisor;
                } else {
                    divisor++;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            responseObserver.onCompleted();
        }

    }
}
