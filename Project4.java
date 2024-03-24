//********************************************************************
//
//  Author:        Instructor
//
//  Program #:     4
//
//  File Name:     Program4.java
//
//  Course:        COSC 4302 Operating Systems
//
//  Due Date:      4/6/2024
//
//  Instructor:    Prof. Fred Kumi
//
//  Java Version:  java 17.0.1 2021-10-19 LTS
//
//  Description:   An interesting way of calculating 𝜋 by using a technique known
//                 as Monte Carlo, which involves randomization. 
//
//********************************************************************

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Project4 {

    // global variables for keeping track of count across threads
    static int pointsInCircle = 0;
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        // create an instance Project4 object
        Project4 obj = new Project4();

        // initialize containers for values
        double userInput;
        double estimatedPi;
        int numCores;

        // display developer info
        obj.developerInfo();

        // get user input for generating random sets
        userInput = obj.getUserInput();

        // get the number of CPU cores
        numCores = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of CPU cores: " + numCores);

        // create a thread pool with a fixed size equal to the number of CPU cores
        ExecutorService executor = Executors.newFixedThreadPool(numCores);

        for (int i = 0; i < numCores; i++) {
            // create a multiThread instance and send equal parts to each CPU core
            MultiThread myMultiThread = new MultiThread(userInput / numCores);

            // execute the thread
            executor.execute(myMultiThread);
        }

        // shutdown the executor
        executor.shutdown();

        try {
            // wait until all threads terminate
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            // calculate output of the estimated value of pi
            estimatedPi = estimatePi(userInput);
            System.out.println("Estimated Value of Pi: " + estimatedPi);

        } catch (InterruptedException e) {
            // handle errors
            System.err.println("Program Abort Reason: " + e.getMessage());
        }
    }

    public double getUserInput() {
        int userInput = 0;
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("Enter the number of random points you wish to have generated:  ");
                userInput = scanner.nextInt();

                if (userInput > 0) {
                    validInput = true; // set flag to true if input is valid
                } else {
                    System.err.println("Invalid input. Enter a positive integer greater than 0.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Enter an integer.");
                scanner.next(); // consume invalid input, avoid infinite loop
            }
        }
        scanner.close();
        return userInput;
    }

    public void developerInfo() {
        System.out.println("\n*********************************************");
        System.out.println("Name:    Paul Ostos ");
        System.out.println("Course:  COSC 4302 Operating Systems ");
        System.out.println("Program: 4 ");
        System.out.println("\n*********************************************\n");
    }

    public static double estimatePi(double userInput) {
        // estimate pi using the given formula
        return 4.0 * (pointsInCircle / userInput);
    }
}
