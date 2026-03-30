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
    private int planesOnGround = 0;
    private boolean emergencyWaiting = false;
    private Queue<Thread> waitingQueue = new LinkedList<>();
    
    public Airport(Runway runway, RefuelTruck refuelTruck){
        gates[0] = new Gate(1);
        gates[1] = new Gate(2);
        gates[2] = new Gate(3);
        this.refuelTruck = refuelTruck;
        this.runway = runway;
    }
    
    public Runway getRunway(){
        return runway;
    }
    
    public RefuelTruck getRefuelTruck() {
    return refuelTruck;
    }
    
    public synchronized void enterAirport() throws InterruptedException{
        while(planesOnGround >= 3 || emergencyWaiting){
            waitingQueue.add(Thread.currentThread());
            while(planesOnGround >= 3 || emergencyWaiting || waitingQueue.peek() != Thread.currentThread()){
                wait();
            }
            waitingQueue.poll();
        }
        planesOnGround++;
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
    
    public synchronized void enterAirportEmergency() throws InterruptedException {
        emergencyWaiting = true;
        notifyAll();
        while(getAvailableGate() == null) {
            wait();
        }
        emergencyWaiting = false;
        planesOnGround++;
        notifyAll();
    }

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
