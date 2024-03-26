//********************************************************************
//
//  Author:        Paul Ostos
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
    static ReentrantLock lock = new ReentrantLock(); // mutex lock

    //***************************************************************
    //
    //  Method:       main
    // 
    //  Description:  The main method of the program
    //
    //  Parameters:   String array
    //
    //  Returns:      N/A 
    //
    //**************************************************************

    public static void main(String[] args) {
        // create an instance Project4 object
        Project4 obj = new Project4();

        // display developer info
        obj.developerInfo();

        // get user input for generating random sets
        double userInput = obj.getUserInput();

        // get the number of CPU cores
        int numCores = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of CPU Cores in Thread Pool: " + numCores);

        // create threads to calculate pi
        obj.createThreads(userInput, numCores);

        // calculate the estimation of pi 
        obj.estimatePi(userInput);

    }

    //***************************************************************
    //
    //  Method:       developerInfo
    // 
    //  Description:  prints developer name and other important information 
    //
    //  Parameters:   none 
    //
    //  Returns:      N/A 
    //
    //**************************************************************


    public void developerInfo() {
        System.out.println("\n*********************************************");
        System.out.println("Name:    Paul Ostos ");
        System.out.println("Course:  COSC 4302 Operating Systems ");
        System.out.println("Program: 4 ");
        System.out.println("*********************************************\n");
    }

    //***************************************************************
    //
    //  Method:       getUserInput
    // 
    //  Description:  gets user input for random point generator
    //
    //  Parameters:   none
    //
    //  Returns:      int userInput
    //
    //**************************************************************


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
                System.err.println("Invalid input. Enter a positive integer greater than 0.");
                scanner.next(); // consume invalid input, avoid infinite loop
            }
        }
        scanner.close();
        return userInput;
    }

//***************************************************************
//
//  Method:       createThreads
// 
//  Description:  creates a fixed thread pool with a size equal to the number of CPU cores, 
//                using executor services, to perform Monte Carlo simulations for estimating the value of pi.
//
//  Parameters:   userInput - the total number of random points to generate
//                numCores - the number of CPU cores to utilize for parallel processing
//
//  Returns:      N/A
//
//**************************************************************


    public void createThreads(double userInput, int numCores){

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

        } catch (InterruptedException e) {
            // handle errors
            System.err.println("Program Abort Reason: " + e.getMessage());
        }
    }

    //***************************************************************
    //
    //  Method:       estimatePi
    // 
    //  Description:  calculates an estimation for the value of pi 
    //
    //  Parameters:   userInput 
    //
    //  Returns:      N/A
    //
    //**************************************************************


    public void estimatePi(double userInput) {
        // estimate pi using the given formula
        double estimatedPi =  4.0 * (pointsInCircle / userInput);
        System.out.println("Estimated Value of Pi: " + estimatedPi);
    }
}
