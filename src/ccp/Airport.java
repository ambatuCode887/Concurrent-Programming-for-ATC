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
    //shared resources
    private Gate[] gates = new Gate[3];
    private Runway runway;
    private RefuelTruck refuelTruck;
    private Kitchen kitchen;
    private boolean takeoffWaiting = false;
    
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
        gates[0] = new Gate(1, this); //create 3 gates
        gates[1] = new Gate(2, this);
        gates[2] = new Gate(3, this);
        this.refuelTruck = refuelTruck;
        this.runway = runway;
        this.kitchen = kitchen;
    }
    
    public synchronized Gate requestDock(int planeNumber) throws InterruptedException {
        Gate gate = getAvailableGate();
        if(gate != null) {
            gate.reserve(); //set it as isOccupied = true meaning a thread is occupying it
            System.out.println("Airport: Gate-" + gate.getGateNumber() + 
                " assigned to Plane-" + planeNumber + ".");
        }
        return gate;
    }
    
    public synchronized void requestUndock(Gate gate, int planeNumber) {
        gate.undock(); //meaning it's no longer occupied and it will wake up all the thread wanted to dock
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
        runwaySemaphore.acquire(); //when a plane is occupying the runway
        runway.land(); //then told other thread to wait
    }
    
    public void releaseRunway() {
        runway.takeoff(); //when the runway is free and notify other plane that are waiting to land
        runwaySemaphore.release(); //then release the permit and wake the other plane
    }
    
    public void requestRunwayTakeoff() throws InterruptedException {
        runwaySemaphore.acquire(); //acquiring permit, however it will block if runway busy
        runway.land(); //reuse land() to mark runway is occupied
    }
    
    public void releaseRunwayAfterTakeoff() {
        runway.takeoff(); //mark runway is free
        runwaySemaphore.release(); //release permit, wakes waiting plane
    }
    
    public synchronized Gate enterAirport() throws InterruptedException {
        waitingQueue.add(Thread.currentThread()); //join the landing queue
        while(planesOnGround >= 3 || emergencyWaiting || takeoffWaiting || 
              waitingQueue.peek() != Thread.currentThread()) { //all these condition wait if airport are full, if there are emergency
            // if there are taking off planes, wait if not at the front of queue
            wait(); //release lock and sleep
        }
        waitingQueue.poll(); //remove self from queue  
        planesOnGround++; //increment planes on ground
        Gate gate = getAvailableGate(); //get first available gate
        gate.reserve(); //mark gate as occupied
        return gate; //return assigned gate to ATC
    }

    public synchronized Gate enterAirportEmergency() throws InterruptedException {
        emergencyWaiting = true; //if emergency are true
        notifyAll(); //then wake normal planes to re-check condition
        Gate gate = null;
        while(gate == null || takeoffWaiting) {
            gate = getAvailableGate(); //get the first free gate
            if(gate == null || takeoffWaiting) {
                wait(); //if gate is occupied then wait
            }
        }
        gate.reserve(); //reserve the gate immediately to prevent race condition
        emergencyWaiting = false; //if the emergency were false then go back to normal
        planesOnGround++; //increment plane on ground
        notifyAll(); //wake up the thread
        return gate; //return assigned gate to ATC
    }

    public synchronized void exitAirport() {
        planesOnGround--; //decrement plane on ground
        notifyAll(); //wake up the thread
    }

    public synchronized void waitForDock() throws InterruptedException {
        while(!planeDocked) {
            wait(); //if the plane is not docked then wait
        }
        planeDocked = false;
    }

    public synchronized void notifyDocked() {
        planeDocked = true; // if the plane is docked then wake the entire thread
        notifyAll();
    }

    public synchronized Gate getAvailableGate() {
        for(Gate gate : gates) { // reserve gate immediately while it still lock
            if(!gate.isOccupied()) {
                return gate;
            }
        }
        return null; //all gate is full
    }
    
    public synchronized void signalTakeoff() {
        takeoffWaiting = true;
        notifyAll();//waking up the sleeping threads
    }

    public synchronized void takeoffComplete() {
        takeoffWaiting = false;
        notifyAll();
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
