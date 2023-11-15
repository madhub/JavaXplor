package com.msxplor.demos;

public class VirtualThreadDemos {
    
    public static void CreateVirtualThreads(String[] args) {

        // // various ways of creatng virtual threads
        // {
        //     // example 1
        //     var runningThread = Thread.startVirtualThread(() -> {
        //         //Code to execute in virtual thread
        //         System.out.println(STR."Is Virtual Thread: \{ Thread.currentThread().isVirtual() } ,Name:\{Thread.currentThread() }" );
        //     });
        //     runningThread.join();
        // }
        // {
        //     // example 2
        //     Runnable runnable = () -> System.out.println(STR. "Is Virtual Thread: \{ Thread.currentThread().isVirtual() } ,Name:\{Thread.currentThread() }" );
        //     Thread virtualThread = Thread.ofVirtual().start(runnable);
        //     virtualThread.join();
        // }
        // {
        //     // Using Executors.newVirtualThreadPerTaskExecutor()
        //     try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        //             executor.submit(() -> {
        //                 System.out.println(STR. "Is Virtual Thread: \{ Thread.currentThread().isVirtual() } ,Name:\{Thread.currentThread()}" );
        //                 try {
        //                     Thread.sleep(Duration.ofSeconds(1));
        //                 } catch (InterruptedException e) {
        //                     throw new RuntimeException(e);
        //                 }
        //             });

        //     }
        // }
        
    }
}
