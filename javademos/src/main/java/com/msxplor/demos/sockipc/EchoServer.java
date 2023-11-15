package com.msxplor.demos.sockipc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class EchoServer {
    private static Logger logger = Logger.getLogger("EchoServer");

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        // Capture shutdown requests from the Virtual Machine.
        // This can occur when a user types Ctrl+C at the console
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                countDownLatch.countDown();
            }
        });


        try (var serverSocket = new ServerSocket(4444);
             var executors = Executors.newVirtualThreadPerTaskExecutor()) {
            logger.info("Accepting incoming connections on port " + serverSocket.getLocalPort());
            while (countDownLatch.getCount() != 0) {
                var clientSocket = serverSocket.accept();
                logger.info("Accepted connection from " + clientSocket.getRemoteSocketAddress());
                // submit Runnable task to  VirtualThread Executor
                executors.submit(new ClientHandler(clientSocket));
            }
            serverSocket.close();
            logger.info("Stopped accepting incoming connections.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Handle the connected client on VirtualThread Executor
     */
    public static class ClientHandler implements Runnable {

        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (var out = new PrintWriter(clientSocket.getOutputStream(), true);
                 var in = new BufferedReader(
                         new InputStreamReader(clientSocket.getInputStream()));) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    }
                    out.println(inputLine.toUpperCase());
                }
            } catch (IOException e) {
                EchoServer.logger.warning("Lost connection to " + this.clientSocket.getRemoteSocketAddress());
            } finally {
                // close the client socket using try-with resource
                try (clientSocket) {
                    EchoServer.logger.info("Closing  connection to " + this.clientSocket.getRemoteSocketAddress());
                } catch (IOException e) {
                    EchoServer.logger.severe("Exception while closing the connection  " + this.clientSocket.getRemoteSocketAddress());
                }
            }

        }
    }
}
