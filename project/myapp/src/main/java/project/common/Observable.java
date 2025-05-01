package project.common;
/**
 * interface for observer pattern
 *
 * This interface was derived from the course-provided .jar library 
 * for the IJA 2024 project at FIT VUT.
 *
 * Original author: Ing. Radek Kočí Ph.D.
 */

public interface Observable {
    void addObserver(Observer var1);
 
    void removeObserver(Observer var1);
 
    void notifyObservers();
 }
 
