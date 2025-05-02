package project.common;


/**
 * class for field of observers 
 *
 * This class was derived from the course-provided .jar library 
 * for the IJA 2024 project at FIT VUT.
 *
 * Original author: Ing. Radek Kočí Ph.D.
 */

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractObservableField implements ToolField {
   private final Set<Observer> observers = new HashSet();

   public AbstractObservableField() {
   }

   public void addObserver(Observer var1) {
      this.observers.add(var1);
   }

   public void removeObserver(Observer var1) {
      this.observers.remove(var1);
   }

   public void notifyObservers() {
      this.observers.forEach((var1) -> {
         var1.update(this);
      });
   }
}