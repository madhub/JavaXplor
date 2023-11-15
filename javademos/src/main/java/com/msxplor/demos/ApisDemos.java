package com.msxplor.demos;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Interface 	                Signature 	        Examples
UnaryOperator<T> 	        T apply(T t) 	        String::toLowerCase, Math::tan
BinaryOperator<T> 	        T apply(T t1, T t2) 	BigInteger::add, Math::pow
Function<T, R> 	                R apply(T t) 	        Arrays::asList, Integer::toBinaryString
Predicate<T, U> 	        boolean test(T t, U u) 	String::isEmpty, Character::isDigit
Supplier<T> 	                T get() 	        LocalDate::now, Instant::now
Consumer<T> 	                void accept(T t) 	System.out::println, Error::printStackTrace
 Reference https://mkyong.com/tutorials/java-8-tutorials/
 */
public class ApisDemos {

    // https://docs.oracle.com/javase/8/docs/api/java/util/StringJoiner.html
    // https://mkyong.com/java8/java-8-stringjoiner-example/
    public static void usingStringJoiner() {

        List<String> friends = List.of("Chandler","Monica","Phoebe","Joey","Ross","Rachel");
        StringJoiner sj = new StringJoiner(",","[","]");
        for (String string : friends) {
            sj.add(string);
        }
        System.out.println(sj.toString());

        // StringJoiner is used internally by static String.join().
        System.out.println(String.join("/", friends));


        String result = friends.stream().map(x -> x).collect(Collectors.joining(" | "));
        System.out.println(result);
        
        
    }
     public static void usingProcessHandle() throws IOException {
         // since Java 9
        ProcessHandle process = ProcessHandle.current();
        // current process info
        ProcessHandle.Info info = process.info();
        ProcessHandle.of(1234);
        Stream<ProcessHandle> allProcesses = ProcessHandle.allProcesses();

        Process sleepy = new ProcessBuilder("sleep", "60s").start();
        
        ProcessHandle handle = ProcessHandle.of(sleepy.pid()).get();
        handle.onExit()
                .thenRun(() -> {
                    System.out.println("Alive: " + sleepy.isAlive());
                    System.out.println("Exit code: " + sleepy.exitValue());
                });
        sleepy.destroy();

     }
     public static void UsingCollectionApi() {
        List.of("First", "Second", "Third")
                .forEach(System.out::println);

        Set.of(1, 2, 3, 4, 5)
                .forEach(System.out::println);

        Map.of("K1", "V1", "K2", "V2", "K3", "V3", "K4", "V4", "K5", "V5")
                .forEach((key, value) -> System.out.println(key + " -> " + value));

        Map.ofEntries(
                        Map.entry("K1", "V1"),
                        Map.entry("K2", "V2"),
                        Map.entry("K3", "V3"),
                        Map.entry("K4", "V4"),
                        Map.entry("K5", "V5")
                )
                .forEach((key, value) -> System.out.println(key + " -> " + value));

    
     }
    
}
