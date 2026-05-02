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
    // Shared resources
    private Gate[] gates = new Gate[3];
    private Runway runway;
    private RefuelTruck refuelTruck;
    private Kitchen kitchen;
    
    //semaphores for resource control
    private Semaphore runwaySemaphore = new Semaphore(1, "Runway");
    private Semaphore gateSemaphore = new Semaphore(3, "Gate");
    
    //ground control
    private int planesOnGround = 0;
    private boolean emergencyWaiting = false;
    private boolean planeDocked = true;
    private Queue<Thread> waitingQueue = new LinkedList<>();
    
    //statistics
    private List<Long> waitingTimes = new ArrayList<>();

    public Airport(Runway runway, RefuelTruck refuelTruck, Kitchen kitchen) {
        gates[0] = new Gate(1, this);
        gates[1] = new Gate(2, this);
        gates[2] = new Gate(3, this);
        this.refuelTruck = refuelTruck;
        this.runway = runway;
        this.kitchen = kitchen;
    }

    //implementing POV 
    
    public synchronized Gate requestDock(int planeNumber) throws InterruptedException {
        Gate gate = getAvailableGate();
        if(gate != null) {
            gate.reserve();
            System.out.println("Airport: Gate-" + gate.getGateNumber() + 
                " assigned to Plane-" + planeNumber + ".");
        }
        return gate;
    }
    
    public synchronized void requestUndock(Gate gate, int planeNumber) {
        gate.undock();
        System.out.println("Airport: Plane-" + planeNumber + 
            " undocked from Gate-" + gate.getGateNumber() + ".");
    }
    
    public void requestRefuel(int planeNumber) throws InterruptedException {
        refuelTruck.requestFuel(planeNumber);
    }
 
    public void requestFood(int planeNumber) throws InterruptedException {
        kitchen.requestFood(planeNumber);
    }
    
    public void requestRunwayLand() throws InterruptedException {
        runwaySemaphore.acquire();
        runway.land();
    }
    
    public void releaseRunway() {
        runway.takeoff();
        runwaySemaphore.release();
    }
    
    public void requestRunwayTakeoff() throws InterruptedException {
        runwaySemaphore.acquire();
        runway.land();
    }
    
    public void releaseRunwayAfterTakeoff() {
        runway.takeoff();
        runwaySemaphore.release();
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

    public synchronized void exitAirport() {
        planesOnGround--;
        notifyAll();
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

    public synchronized Gate getAvailableGate() {
        for(Gate gate : gates) {
            if(!gate.isOccupied()) {
                return gate;
            }
        }
        return null;
    }

    public Gate[] getGates() {
        return gates;
    }

    public Runway getRunway() {
        return runway;
    }

    public RefuelTruck getRefuelTruck() {
        return refuelTruck;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }

    public synchronized boolean isFull() {
        return planesOnGround >= 3;
    }
    
    public synchronized void addWaitingTime(long waitingTime) {
        waitingTimes.add(waitingTime);
    }

    public synchronized void printStatistics() {
        if(waitingTimes.isEmpty()) {
            System.out.println("No waiting time data available.");
            return;
        }
        long max = 0, min = Long.MAX_VALUE, total = 0;
        for(long time : waitingTimes) {
            if(time > max) max = time;
            if(time < min) min = time;
            total += time;
        }
        long average = total / waitingTimes.size();
        System.out.println("Max waiting time: " + max + "ms");
        System.out.println("Min waiting time: " + min + "ms");
        System.out.println("Average waiting time: " + average + "ms");
    }
}
