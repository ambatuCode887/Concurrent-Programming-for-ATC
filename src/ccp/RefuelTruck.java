/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;

import java.util.LinkedList;
import java.util.Queue;
/**
 *
 * @author User
 */
public class RefuelTruck implements Runnable {
    private boolean isOccupied = false;
    private Queue<Integer> waitingPlanes = new LinkedList<>();
    
    public synchronized void requestFuel(int planeNumber) throws InterruptedException {
        waitingPlanes.add(planeNumber);
        System.out.println("Refuel Truck: Plane-" + planeNumber + " added to queue.");
        
        notifyAll(); //tell the truck a plane is waiting
        
        while(waitingPlanes.contains(planeNumber)){
            wait();
        }
    }
    
        @Override
        public void run() {
        while(true) {
            int planeNumber = -1;
            synchronized(this) {
                while(waitingPlanes.isEmpty()) {
                    try {
                        wait(); //sleep until a plane requests fuel
                    } catch(InterruptedException ex) {
                        return; //truck shuts down
                    }
                }
                planeNumber = waitingPlanes.poll(); //get next plane
                isOccupied = true;
                System.out.println("Refuel Truck: Now refueling Plane-" + planeNumber + ".");
            }
            try {
                Thread.sleep(3000);
            } catch(InterruptedException ex) {
            
            }

            synchronized(this) {
                isOccupied = false;
                System.out.println("Refuel Truck: Finished refueling Plane-" + planeNumber + ".");
                notifyAll(); //wake up waiting planes
            }
        }
    }
}
