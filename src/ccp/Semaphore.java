/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;

/**
 *
 * @author User
 */
public class Semaphore {
    private int permits;      
    private String name;        

    public Semaphore(int permits, String name) {
        this.permits = permits;
        this.name = name;
    }

    /**
     * Acquires a permit - blocks if no permits available
     * Uses while loop instead of if to guard against spurious wakeups
     * while loop re-checks condition every time thread is woken
     */
    public synchronized void acquire() throws InterruptedException {
        while(permits <= 0) { //release lock and sleep until notified
            wait();
        }
        permits--; //consume one permit
    }

    public synchronized void release() {
        permits++; //release a permit then wakes up all waiting threads using notifyAll(), if use notify() it will causes thread starvation
        notifyAll();
    }

     /**
     * Non-blocking attempt to acquire a permit
     * Returns true if successful, false if no permits available
     */
    public synchronized boolean tryAcquire() {
        if(permits > 0) {
            permits--;
            return true;
        }
        return false;
    }

    public synchronized int getPermits() {
        return permits; //return current number of available permit
    }
}
