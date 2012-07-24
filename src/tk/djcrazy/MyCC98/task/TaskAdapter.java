package tk.djcrazy.MyCC98.task;

public abstract class TaskAdapter implements TaskListener {

    public abstract String getName();

    public void onPreExecute(GenericTask task) {};
    public void onPostExecute(GenericTask task, TaskResult result) {};
    public void onProgressUpdate(GenericTask task, Object param) {};
    public void onCancelled(GenericTask task) {};
}
