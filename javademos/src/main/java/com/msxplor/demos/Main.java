package com.msxplor.demos;

import java.io.IOException;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 * Main driver program to tests other classes
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        
        var random  = RandomGenerator.getDefault();
        System.out.println(random.getClass());
          RandomGeneratorFactory.all()
              .map(fac -> fac.group()+ " : " +fac.name())
              .sorted()
              .forEach(System.out::println);
        
    }
}