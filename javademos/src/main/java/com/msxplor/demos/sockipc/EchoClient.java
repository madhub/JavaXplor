package com.msxplor.demos.sockipc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Client Application that connects to EchoServer
 * and sends number of messages
 */
public class EchoClient implements Runnable {

    private static Logger logger = System.getLogger("SimpleEchoClient");

    public static void startTestClient(String[] args) {

        int numberOfClients = 100;
        int numberOfMessagePerClient = 100;
        String ipAddresOrHostName = "localhost";
        int port = 4444;
        try (var executors = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, numberOfClients).forEach(idx -> {
                executors.submit(new EchoClient(ipAddresOrHostName, port, numberOfMessagePerClient));
            });

        }
        logger.log(Level.INFO, "Done");

    }

    private final String ip;
    private final int port;
    private final int numberOfMessages;

    public EchoClient(String ip, int port, int numberOfMessages) {

        this.ip = ip;
        this.port = port;
        this.numberOfMessages = numberOfMessages;
    }

    @Override
    public void run() {
        try (var clientSocket = new Socket(ip, port);
                var out = new PrintWriter(clientSocket.getOutputStream(), true);
                var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
            for (int index = 0; index < numberOfMessages; index++) {

                out.println("Random messsage " + Instant.now().toString());
                String resp = in.readLine();
                logger.log(Level.INFO, "[Msg from server] " + resp);
                // introduce delay to simulate the IO
                Thread.sleep(Duration.ofMillis(500));
            }
        } catch (IOException e) {
            logger.log(Level.ERROR,
                    "Unable to connect to remote client or Unable to read/write to connected client " + e.getMessage());
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "InterruptedException" + e.getMessage());
        }
    }
}
