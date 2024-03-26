//********************************************************************
//
//  Author:        Paul Ostos
//
//  Program #:     4
//
//  File Name:     Point.java
//
//  Course:        COSC 4302 Operating Systems
//
//  Due Date:      4/6/2024
//
//  Instructor:    Prof. Fred Kumi
//
//  Java Version:  java 17.0.1 2021-10-19 LTS
//
//  Description:   This Java class, named Point, represents a point in a two-dimensional 
//                 Cartesian coordinate system. It encapsulates the x and y 
//                 coordinates of the point, providing methods to access these coordinates. 
//                 The class includes a constructor to initialize the coordinates upon 
//                 object creation, and getter methods to retrieve the x and y values.         
//
//********************************************************************


class Point {
    private double x;
    private double y;

    // constructor 
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    // setter and getter methods for points x and y
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
