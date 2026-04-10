/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;

/**
 *
 * @author User
 */
import java.util.LinkedList;
import java.util.Queue;

public class Kitchen implements Runnable {
    private boolean isPreparing = false;
    private Queue<Integer> waitingPlanes = new LinkedList<>();

    @Override
    public void run() {
        while(true) {
            int planeNumber = -1;
            synchronized(this) {
                while(waitingPlanes.isEmpty()) {
                    try {
                        wait(); //wait until a plane requests food
                    } catch(InterruptedException ex) {
                        return; //kitchen shuts down
                    }
                }
                planeNumber = waitingPlanes.poll();
                isPreparing = true;
                System.out.println("Kitchen: Preparing food for Plane-" + planeNumber + ".");
            }
            try {
                Thread.sleep(2000);
            } catch(InterruptedException ex) {}

            synchronized(this) {
                isPreparing = false;
                System.out.println("Kitchen: Food loaded onto Plane-" + planeNumber + ".");
                notifyAll();
            }
        }
    }

    public synchronized void requestFood(int planeNumber) throws InterruptedException {
        waitingPlanes.add(planeNumber);
        System.out.println("Kitchen: Plane-" + planeNumber + " added to food queue.");
        notifyAll(); // wake up kitchen
        while(isPreparing || waitingPlanes.contains(planeNumber)) {
            wait(); // wait until food is ready
        }
    }
}
