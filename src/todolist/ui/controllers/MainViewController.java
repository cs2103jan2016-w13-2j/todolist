package todolist.ui.controllers;

import java.io.File;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Scanner;

import todolist.MainApp;
import todolist.common.UtilityLogger;
import todolist.common.UtilityLogger.Component;
import todolist.model.Task;
import todolist.ui.TaskWrapper;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

//@@author A0123994W

/* 
 * MainViewController controls and manipulates data for display on the main display area, for the main tab.
 * 
 * @author Huang Lie Jun (A0123994W)
 */
public class MainViewController {

    // Logger Messages
    private static final String MESSAGE_CALL_LOGIC_COMPONENT = "User input detected at UI component --calling--> Logic component for processing.";
    private static final String MESSAGE_CLEAR_TEXTFIELD = "Text field cleared for next input.";
    private static final String ERROR_PROCESSING_USER_INPUT = "Error processing user input.";
    private static final String MESSAGE_UPDATED_MAIN_TASKLIST = "Updated display task list [HOME].";
    private static final String MESSAGE_HIGHLIGHT_ITEM = "Item #%1$s in display task list highlighted.";
    private static final String MESSAGE_HIGHLIGHT_ITEM_NOT_FOUND = "Item to be highlighted cannot be found in display task list.";
    private static final String NIGHT_MODE = "night-mode";
    private static final String DAY_MODE = "day-mode";

    // Model data
    protected ObservableList<TaskWrapper> tasksToDisplay = null;

    // Main application linkback
    private MainApp mainApplication = null;

    /*** Views ***/
    @FXML
    protected ListView<TaskWrapper> listView = null;

    // Logger
    UtilityLogger logger = null;

    protected int index = 0;

    // @@author A0130620B
    // Temporary attributes for testing
    public String path = "demo.txt";
    public int demoCounter = 0;

    // @@author A0123994W
    /*** Controller Functions ***/

    /*
     * Constructor initializes to contain the display data structure.
     *
     */
    public MainViewController() {
        // Initialise models
        tasksToDisplay = FXCollections.observableArrayList();
        listView = new ListView<TaskWrapper>();
        logger = new UtilityLogger();

    }

    /*
     * setMainApp takes a MainApp reference and stores it locally as the
     * mainApplication reference.
     * 
     * @param MainApp mainApp is the reference provided to the calling function
     * 
     */
    public void setMainApp(MainApp mainApp) {
        mainApplication = mainApp;
    }

    /*
     * initialize prepares the task list.
     * 
     */
    @FXML
    public void initialize() {
        initTaskListView();
    }

    /*
     * initTaskListView initializes the task list by setting the list properties
     * and default format.
     * 
     */
    public void initTaskListView() {
        listView.setCellFactory(new Callback<ListView<TaskWrapper>, javafx.scene.control.ListCell<TaskWrapper>>() {
            @Override
            public ListCell<TaskWrapper> call(ListView<TaskWrapper> listView) {
                return new TaskListCell();
            }
        });

        VBox.setVgrow(listView, Priority.ALWAYS);
        HBox.setHgrow(listView, Priority.ALWAYS);

    }

    /*
     * setCommandLineCallback takes in a textfield and sets a function as the
     * action callback function.
     * 
     * @param TextField commandField is the command line that reads the user
     * input
     * 
     */
    public void setCommandLineCallback(TextField commandField) {
        // Set Callback for TextField
        EventHandler<ActionEvent> commandHandler = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String commandString = commandField.getText();
                // Command command = new Command(commandString);
                // System.out.println(command.getCommand());

                // Pass command line input for processing
                try {

                    commandField.clear();
                    logger.logAction(Component.UI, MESSAGE_CLEAR_TEXTFIELD);

                    // System.out.println(commandString);

                    if (commandString.equals(NIGHT_MODE)) {
                        Scene scene = mainApplication.getPrimaryStage().getScene();
                        scene.getStylesheets().remove(mainApplication.dayModeTheme);
                        scene.getStylesheets().add(mainApplication.nightModeTheme);
                        mainApplication.getPrimaryStage().setScene(scene);
                        mainApplication.getPrimaryStage().show();

                    } else if (commandString.equals(DAY_MODE)) {
                        Scene scene = mainApplication.getPrimaryStage().getScene();
                        scene.getStylesheets().remove(mainApplication.nightModeTheme);
                        scene.getStylesheets().add(mainApplication.dayModeTheme);
                        mainApplication.getPrimaryStage().setScene(scene);
                        mainApplication.getPrimaryStage().show();

                    } else {
                        mainApplication.uiHandlerUnit.process(commandString);
                        logger.logComponentCall(Component.UI, MESSAGE_CALL_LOGIC_COMPONENT);
                    }

                } catch (Exception exception) {
                    logger.logError(Component.UI, ERROR_PROCESSING_USER_INPUT);
                    exception.printStackTrace();
                }
            }
        };

        commandField.setOnAction(commandHandler);
    }

    // @@author A0130620B
    /*** Temporary Functions for Testing ***/

    public ArrayList<String> demoFileHandler(String path) {
        ArrayList<String> myList = new ArrayList<String>();
        try {

            File file = new File(path);
            Scanner scr = new Scanner(file);
            while (scr.hasNextLine()) {
                String temp = scr.nextLine();
                myList.add(temp);
                System.out.println(temp);
            }
            scr.close();
        } catch (Exception e) {

        }
        return myList;
    }

    Boolean isDemoing = false;

    public void setCommandLineCallbackDemo(TextField commandField) {
        // Set Callback for TextField

        EventHandler<ActionEvent> commandHandler = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (!isDemoing) {
                    String commandString = commandField.getText();
                    // Command command = new Command(commandString);
                    // System.out.println(command.getCommand());

                    // Pass command line input for processing
                    try {

                        commandField.clear();
                        logger.logAction(Component.UI, MESSAGE_CLEAR_TEXTFIELD);
                        if (commandString.equals("Start demo")) {
                            isDemoing = true;
                        } else {
                            mainApplication.uiHandlerUnit.process(commandString);
                            logger.logComponentCall(Component.UI, MESSAGE_CALL_LOGIC_COMPONENT);
                        }
                    } catch (Exception exception) {
                        logger.logError(Component.UI, ERROR_PROCESSING_USER_INPUT);
                        exception.printStackTrace();
                    }
                } else {
                    synchronized (this) {
                        ArrayList<String> demoList = demoFileHandler(path);
                        String commandString = demoList.get(demoCounter);
                        demoCounter++;
                        if (commandString.equals("exit")) {
                            isDemoing = false;
                        } else {
                            // System.out.println(event.getEventType());

                            // Pass command line input for processing
                            commandField.clear();

                            final Animation animation = new Transition() {
                                {
                                    setCycleDuration(new Duration(commandString.length() * 50));
                                }

                                protected void interpolate(double frac) {
                                    final int length = commandString.length();
                                    final int n = Math.round(length * (float) frac);
                                    commandField.setText(commandString.substring(0, n));
                                }

                            };

                            animation.setOnFinished(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    mainApplication.uiHandlerUnit.process(commandString);
                                }
                            });

                            animation.play();
                        }
                    }
                }
            }
        };

        commandField.setOnAction(commandHandler);
    }

    // @@author A0123994W
    /*** View Access Functions ***/

    /*
     * getTaskListView returns the current list view of tasks.
     * 
     * @return ListView<TaskWrapper> listView
     * 
     */
    public ListView<TaskWrapper> getTaskListView() {
        return listView;
    }

    /*** Model Access Functions ***/

    /*
     * getTasks returns the list of tasks stored.
     * 
     * @return ObservableList<TaskWrapper> tasks
     * 
     */
    public ObservableList<TaskWrapper> getTasks() {
        return tasksToDisplay;
    }

    /*
     * setTasks takes a list of tasks and sets it as the list of tasks to
     * display by associating with the list view. This overrides all the tasks
     * in the current list of display tasks.
     * 
     * @param ArrayList<Task> tasks is the list of tasks to replace the current
     * display list
     * 
     */
    public void setTasks(ArrayList<Task> tasks) {

        // List provided by logic must be valid
        assert (tasks != null);

        ArrayList<TaskWrapper> arrayOfWrappers = new ArrayList<TaskWrapper>();
        listView.getItems().clear();

        // Convert Task to TaskWrapper for display handling
        for (int i = 0; i < tasks.size(); ++i) {
            if (!tasks.get(i).getDoneStatus()) {
                TaskWrapper wrappedTask = new TaskWrapper(tasks.get(i));
                arrayOfWrappers.add(wrappedTask);
            }
        }

        listView.getItems().addAll(arrayOfWrappers);
        

        logger.logAction(Component.UI, MESSAGE_UPDATED_MAIN_TASKLIST);
    }

    /*** Utility Functions ***/

    /*
     * highlight gets the index of the task to highlight and put that index item
     * on focus.
     * 
     * @param Task task is the task to be highlighted.
     */
    public void highlight(Task task) {
        int index = searchInList(task);

        // Only highlights if the task is within range and can be found in the
        // list
        if (index != -1) {
            listView.getSelectionModel().select(index);
            listView.getFocusModel().focus(index);
            listView.scrollTo(index);
            logger.logAction(Component.UI, String.format(MESSAGE_HIGHLIGHT_ITEM, Integer.toString(index)));
        }
    }

    /*
     * searchInList takes a task and searches for its position in the current
     * display list.
     * 
     * @param Task task is the task to locate
     * 
     * @return int searchIndex is the position of the task in the list of tasks
     * 
     */
    private int searchInList(Task task) {

        // Return the index if the task can be found
        for (int i = 0; i < listView.getItems().size(); ++i) {
            if (listView.getItems().get(i).getTaskObject().equals(task)) {
                return i;
            }
        }

        // Otherwise, denote as cannot be found by -1
        logger.logAction(Component.UI, MESSAGE_HIGHLIGHT_ITEM_NOT_FOUND);
        return -1;
    }

    /*
     * isCompleted checks if a given task has already been completed.
     * 
     * @param Task task is the given task to check for completion
     * 
     * @return boolean isCompleted
     * 
     */
    protected Boolean isCompleted(Task task) {
        return task.getDoneStatus();
    }

    /*
     * getTaskAt returns the task at the position specified in the list view, or
     * null if it is not found.
     * 
     * @param int pos
     * 
     * @return Task task
     * 
     */
    public Task getTaskAt(int pos) {
        ObservableList<TaskWrapper> itemList = listView.getItems();
        if (pos >= 1 && pos <= itemList.size()) {
            return itemList.get(pos - 1).getTaskObject();
        } else {
            return null;
        }
    }

    public int getCompletedCount() {
        int count = 0;
        for (TaskWrapper task : listView.getItems()) {
            if (task.getIsCompleted()) {
                count += 1;
            }
        }

        return count;
    }

    public void setPageIndex(int homeTab) {
        index = homeTab;
    }

    public int getPageIndex() {
        return index;
    }
}
