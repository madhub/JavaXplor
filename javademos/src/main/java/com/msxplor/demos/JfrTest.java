package com.msxplor.demos;

import jdk.jfr.consumer.RecordingStream;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Builder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Sample to show the usage of programatic access to JFR Event Strea,
 */

// https://inside.java/2023/02/27/programmer-guide-to-jfr/
public class JfrTest {
    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        executorService.submit( () -> {
            try(var rstream = new RecordingStream();) {
                rstream.enable("jdk.CPULoad").withPeriod(Duration.ofSeconds(5));
                rstream.enable("jdk.TLSHandshake").withPeriod(Duration.ofSeconds(5));
                rstream.enable("jdk.ThreadStart").withPeriod(Duration.ofSeconds(1));


//        rstream.onEvent( "jdk.CPULoad",recordedEvent -> {
//            System.out.println( "On Event called");
//            float fl = recordedEvent.getFloat("jvmUser");
//            System.out.println(STR."Cpu Load \{fl}");
//        });

                rstream.onEvent(recordedEvent -> {
                    System.out.println("On Event called " + recordedEvent);

                });

                // this blocks the current Thread
                rstream.start();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        });

        // to Simulate TLS connections
         HttpClient newHttpClient = HttpClient.newHttpClient();
        for( int i = 0;i < 10;i++ ) {

            try {
                System.out.println( "Sending request");
                HttpRequest request1 = HttpRequest.newBuilder()
                        .uri(new URI("https://postman-echo.com/get"))
                        .GET()
                        .build();
                HttpResponse<String> response1 = HttpClient
                        .newBuilder()
                        .build()
                        .send(request1, HttpResponse.BodyHandlers.ofString());


                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://www.google.com"))
                        .HEAD()
                        .build();
                HttpResponse<String> response = 
                        newHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
               // System.out.println("Received Response "+response1.body());
            }catch (Exception exception) {
                System.out.println(exception);
            }

            System.out.println( "Sleeping for 2 sec");
            Thread.sleep(Duration.ofMillis(2000));


        }
        countDownLatch.countDown();
        executorService.shutdown();
        System.out.println( "Done");


    }
}
