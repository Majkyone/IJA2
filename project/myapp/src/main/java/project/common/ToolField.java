package project.common;

/**
 * interface for observer pattern
 *
 * This interface was derived from the course-provided .jar library 
 * for the IJA 2024 project at FIT VUT.
 *
 * Original author: Ing. Radek Kočí Ph.D.
 */

public interface ToolField extends Observable {
    void turn();
 
    boolean north();
 
    boolean east();
 
    boolean south();
 
    boolean west();
 
    boolean light();
 
    boolean isLink();
 
    boolean isBulb();
 
    boolean isPower();
 }
