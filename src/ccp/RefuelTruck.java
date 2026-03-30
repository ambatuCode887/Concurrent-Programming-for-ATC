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
public class RefuelTruck {
    
    private boolean isOccupied = false;
    private Queue<Integer> waitingPlanes = new LinkedList<>();
    
    public synchronized void requestFuel(int planeNumber) throws InterruptedException {
        if(isOccupied) {
            System.out.println("Refuel Truck: Busy! Plane-" + planeNumber + " added to queue.");
            waitingPlanes.add(planeNumber);
        }
        while(isOccupied) {
            wait();
        }
        isOccupied = true;
        System.out.println("Refuel Truck: Now refueling Plane-" + planeNumber + ".");
    }

    public synchronized void refuelComplete(int planeNumber) {
        isOccupied = false;
        System.out.println("Refuel Truck: Finished refueling Plane-" + planeNumber + ".");
        notify();
    }
}
