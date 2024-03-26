//********************************************************************
//
//  Author:        Paul Ostos
//
//  Program #:     4
//
//  File Name:     MultiThread.java
//
//  Course:        COSC 4302 Operating Systems
//
//  Due Date:      4/6/2024
//
//  Instructor:    Prof. Fred Kumi
//
//  Java Version:  java 17.0.1 2021-10-19 LTS
//
//  Description:   This Java program, MultiThread.java, utilizes multithreading to estimate 
//                 the value of Pi using a Monte Carlo method. It generates a user-specified 
//                 number of random points and determines how many fall within the unit circle. 
//                 The program employs a mutex lock to ensure thread safety while updating the 
//                 count of points within the circle, allowing multiple threads to increment the 
//                 total count without conflicts.
//
//********************************************************************

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class MultiThread implements Runnable {

    private double userInput;
    private double pointsInCircle;

    public MultiThread(double userInput) {
        this.userInput = userInput;
    }

    // ***************************************************************
    //
    // Method: run
    //
    // Description: overridden run method, calls random point generator
    // and also updates the number of points in the circle
    //
    // Parameters: none
    //
    // Returns: N/A
    //
    // **************************************************************

    @Override
    public void run() {
        try {
            // generate the, user specified, number of random sets (x,y)
            Set<Point> randomPoints = generateRandomPoints((int) userInput);

            // update the count of points that land in the circle
            updatePointsInCircle(randomPoints);
        } catch (Exception e) {
            // handle exceptions
            System.err.println("Program Abort Reason: " + e.getMessage());
        }
    }

    // ***************************************************************
    //
    // Method: generateRandomPoints
    //
    // Description: generates random points using a HashSet<>
    //
    // Parameters: int userInput (number entered by user)
    //
    // Returns: randomPoints
    //
    // **************************************************************

    public Set<Point> generateRandomPoints(int userInput) {
        // use secureRandom to generate random numbers securely
        SecureRandom random = new SecureRandom();

        // use HashSet to store unique points and avoid duplicates
        Set<Point> randomPoints = new HashSet<>();

        while (randomPoints.size() < userInput) {
            double x = random.nextDouble() * 2 - 1; // random double -1 to 1 inclusive for x-coordinate
            double y = random.nextDouble() * 2 - 1; // random double -1 to 1 inclusive for y-coordinate
            randomPoints.add(new Point(x, y)); // add the new point to the hash set
        }
        return randomPoints;
    }

    // ***************************************************************
    //
    // Method: updatePointsInCircle
    //
    // Description: Uses a mutex lock to keep increment the total count of
    // points that land in the circle by assuring multiple threads
    // cannot access the global variable concurrently
    //
    // Parameters: Set<Point> randomPoints
    //
    // Returns: N/A
    //
    // **************************************************************

    public void updatePointsInCircle(Set<Point> randomPoints) {
        for (Point point : randomPoints) { // read as: for each point in the randomPoints set
            double x = point.getX(); // get the value of x
            double y = point.getY(); // get the value of y

            // check if point falls within the limits of the circle
            if (Math.sqrt(x * x + y * y) < 1.0) {
                pointsInCircle++;
            }
        }
        /*
         * Ensure thread safety. This prevents other threads from accessing the shared
         * variable concurrently. It then updates the count of points within the circle
         * by adding the value of pointsInCircle to Project4.pointsInCircle. Finally, it releases the lock
         * using Project4.lock.unlock(), allowing other threads to acquire the lock if needed
         */
        Project4.lock.lock();
        try {
            // update the count for points within the circle
            Project4.pointsInCircle += pointsInCircle;
        } finally {
            // release the lock after pointsInCirlce is updated
            Project4.lock.unlock();
        }
    }
}
