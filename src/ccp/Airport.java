/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;


import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
/**
 *
 * @author User
 */
public class Airport {
    private Gate[] gates = new Gate[3];
    Runway runway;
    RefuelTruck refuelTruck;
    Kitchen kitchen;
    private int planesOnGround = 0;
    private int nextToEmbark = 1;
    private boolean emergencyWaiting = false;
    private boolean planeDocked = true;
    private Queue<Thread> waitingQueue = new LinkedList<>();
    
    public Airport(Runway runway, RefuelTruck refuelTruck, Kitchen kitchen){
        gates[0] = new Gate(1, this);
        gates[1] = new Gate(2, this);
        gates[2] = new Gate(3, this);
        this.refuelTruck = refuelTruck;
        this.runway = runway;
        this.kitchen = kitchen;
    }

    
    public Kitchen getKitchen(){
        return kitchen;
    }
    
    public Runway getRunway(){
        return runway;
    }
    
    public RefuelTruck getRefuelTruck() {
        return refuelTruck;
    }
    
    public synchronized Gate enterAirport() throws InterruptedException {
        waitingQueue.add(Thread.currentThread());
        while(planesOnGround >= 3 || emergencyWaiting || 
              waitingQueue.peek() != Thread.currentThread()) {
            wait();
        }
        waitingQueue.poll();
        planesOnGround++;
        Gate gate = getAvailableGate();
        gate.reserve();
        return gate;
    }

    
    public synchronized void exitAirport(){
         planesOnGround--;
         notifyAll();
    }
    
    public synchronized Gate getAvailableGate(){
        for (Gate gate : gates) {
            if (!gate.isOccupied()) {
                return gate;
            }
        }
        return null;
    }
    
    public Gate[] getGates() {
        return gates;
    }
    
    public synchronized boolean isFull() {
        return planesOnGround >= 3;
    }
    
    private List<Long> waitingTimes = new ArrayList<>();
    
    public synchronized void addWaitingTime(long waitingTime) {
        waitingTimes.add(waitingTime);
    }
    
    public synchronized Gate enterAirportEmergency() throws InterruptedException {
        emergencyWaiting = true;
        notifyAll();

        Gate gate = null;
        while(gate == null) {
            gate = getAvailableGate();
            if(gate == null) {
                wait();
            }
        }
        gate.reserve();
        emergencyWaiting = false;
        planesOnGround++;
        notifyAll();
        return gate;
    }
    
    public synchronized void waitForDock() throws InterruptedException {
        while(!planeDocked) {
            wait();
        }
            planeDocked = false;
    }
    
     public synchronized void notifyDocked() {
        planeDocked = true;
        notifyAll();
    }
     
//     public synchronized void waitForEmbarkTurn(int planeNumber) throws InterruptedException {
//        while(nextToEmbark != planeNumber) {
//            wait();
//        }
//    }
//
//    public synchronized void embarkComplete() {
//        nextToEmbark++;
//        notifyAll();
//    }

    public synchronized void printStatistics() {
        if(waitingTimes.isEmpty()) {
            System.out.println("No waiting time data available.");
            return;
        }

        long max = 0, min = Long.MAX_VALUE, total = 0;

        for (long time : waitingTimes) {
            if (time > max) max = time;
            if (time < min) min = time;
            total += time;
        }

        long average = total / waitingTimes.size();

        System.out.println("Max waiting time: " + max + "ms");
        System.out.println("Min waiting time: " + min + "ms");
        System.out.println("Average waiting time: " + average + "ms");
    }
    
}
