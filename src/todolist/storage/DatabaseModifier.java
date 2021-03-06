// @@author A0131334W
package todolist.storage;

import java.io.IOException;
import java.util.ArrayList;

import todolist.model.Task;

 
public class DatabaseModifier {
    public static String EXCEPEPTION_REPEATED_TASK = "The task has already existed!";
    public static String EXCEPTION_TASKNOTEXIST = "The task to delete does not exist!";

    private ArrayList<Task> taskList;
   
    protected DatabaseModifier() {
        taskList = new ArrayList<Task>();
    }

    /**
     * add a task to the arraylist after checking whether it is already existing
     * 
     * @param tasks
     *            list of tasks to which the task to add
     * @param task
     *            the task to be added
     * @return the resultant task list
     * @throws IOException
     *             if the task is already existing
     */
    public ArrayList<Task> addTask(ArrayList<Task> tasks, Task task) throws IOException {
        this.taskList = tasks;
        
        if (isExistingTask(task)) {
            throw new IOException(EXCEPEPTION_REPEATED_TASK);
        }

        taskList.add(0, task);
        
        return taskList;
    }

    // helper method for add function
    private boolean isExistingTask(Task task) {
        return taskList.contains(task);
    }

    /**
     * delete the specific task from the list of tasks
     * 
     * @param tasks         list of tasks from where the task to be deleted
     * 
     * @param taskToDelete  the task to be deleted
     * 
     * @return taskList     the list of task after deleting the required task
     * 
     * @throws IOException  when the task to delete not in the task list
     */
    public ArrayList<Task> deleteTask(ArrayList<Task> tasks, Task taskToDelete) throws IOException {
        taskList = tasks;
        
        boolean isListEmpty = false;
        isListEmpty = taskList.size() == 0;
        
        if (isListEmpty) {
            throw new IOException(EXCEPTION_TASKNOTEXIST);
        }

        Integer index = searchForIndexOfTask(taskToDelete);

        if (index == null) {
            throw new IOException(EXCEPTION_TASKNOTEXIST);
        }
        
        taskList.remove(taskList.get(index));

        return taskList;
    }

    // helper methods
    private Integer searchForIndexOfTask(Task taskToDelete) {  	
        for (int i = 0; i < taskList.size(); i++) {
        	
            Task currentTask = taskList.get(i);

            String nameOfCurrentTask = currentTask.getName().getName();
            
            String nameOfTaskToDelete = taskToDelete.getName().getName();
            
            if (nameOfCurrentTask.equalsIgnoreCase(nameOfTaskToDelete)) {
                return i;
            }
        }
        return null;
    }
}
