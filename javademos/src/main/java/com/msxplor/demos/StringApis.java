package com.msxplor.demos;

public class StringApis {
    
    public static void linesFromInputString() {
        String input = "Hello\n World";
        input.lines().forEach(System.out::println);
    }

}
