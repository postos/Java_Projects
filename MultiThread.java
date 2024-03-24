import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class MultiThread implements Runnable {

    public double userInput;
    public double pointsInCircle;

    public MultiThread(double userInput) {
        this.userInput = userInput;
    }

    @Override
    public void run() {
        try{
        // generate the, user specified, number of random sets (x,y)
        Set<Point> randomPoints = generateRandomPoints((int) userInput);

        // update the count of points that land in the circle 
        updatePointsInCircle(randomPoints);
        } catch (Exception e){
            // handle exceptions 
            System.err.println("Program Abort Reason: " + e.getMessage());
        }
    }

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
         * variable concurrently. It then updates the count of points within the circle by adding the value
         * of pointsInCircle to Project4.pointsInCircle. Finally, it releases the lock
         * using Project4.lock.unlock(), allowing other threads to acquire the lock if needed 
         */
        Project4.lock.lock();
        try {
            // update the count for points within the circle
            Project4.pointsInCircle += pointsInCircle;
        } finally {
            // release the lock
            Project4.lock.unlock();
        }
    }
}
