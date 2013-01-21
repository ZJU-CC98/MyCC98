package tk.djcrazy.MyCC98.task;

import java.util.Observable;
import java.util.Observer;

import android.util.Log;

public class TaskManager  extends Observable {
    private static final String TAG = "TaskManager";
    
    public static final Integer CANCEL_ALL = 1;
    
    public void cancelAll() {
         setChanged();
        notifyObservers(CANCEL_ALL);
    }
    
    public void addTask(Observer task) {
        super.addObserver(task);
    }
}