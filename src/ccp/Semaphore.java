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

    public synchronized void acquire() throws InterruptedException {
        while(permits <= 0) {
            wait();
        }
        permits--;
    }

    public synchronized void release() {
        permits++;
        notifyAll();
    }

    public synchronized boolean tryAcquire() {
        if(permits > 0) {
            permits--;
            return true;
        }
        return false;
    }

    public synchronized int getPermits() {
        return permits;
    }
}
