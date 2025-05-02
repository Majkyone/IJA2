package project.common;
/**
 * interface
 *
 * This interface was derived from the course-provided .jar library 
 * for the IJA 2024 project at FIT VUT.
 *
 * Original author: Ing. Radek Kočí Ph.D.
 */

public interface ToolEnvironment {
    int rows();
 
    int cols();
 
    ToolField fieldAt(int var1, int var2);
 }
 