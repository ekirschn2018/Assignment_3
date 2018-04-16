package com.evelynkirschner.assignment_3;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

abstract class TaskListItem {

    abstract public void display();
    abstract public String getPriority();
    abstract public String getTitle();
}

// single task
class Task extends TaskListItem implements Comparable<Task> {
    // instance fields
    protected String title, description;
    protected String priority;     // can be from 0 to 5 where 5 is the highest
    int BEFORE = -1, AFTER = 1;

    // Constructor
    public Task(String userTitle, String desc, String prty){
        this.title = userTitle;
        this.description = desc;
        this.priority = prty;
    }

    // methods required by TaskListItem
    // display the task
    public void display(){
        System.out.println( title + ": " + description + " (priority: " + priority + ")");
    }

    // get the priority
    public String getPriority() {
        return priority;
    }

    // get the title
    public String getTitle() {
        return title;
    }

    public int compareTo(Task t) {
        if (!getPriority().equals(t.getPriority())) {  // if the priorities are not the same need high to low
            if (Integer.parseInt(getPriority()) > Integer.parseInt(t.getPriority())) // comes first
                return BEFORE;
            else
                return AFTER;   //comes after
        }
        else {
            return getTitle().compareTo(t.getTitle());
        }
    }
}

// list of tasks
class TaskList extends TaskListItem {
    // instance fields
    private List<Task> _tasks = new ArrayList<>();
    private UserInterface ui = new UserInterface();
    int BEFORE = -1, AFTER = 1;

    // class field
    private static Scanner scanner = new Scanner(System.in);

    // Constructor - not needed

    // methods required by TaskListItem
    // display the task
    public void display(){
        for (TaskListItem task : _tasks)
            task.display();
    }

    // get the priority
    public String getPriority() {
        return getPriority();
    }

    // get the title
    public String getTitle() {
        return getTitle();
    }

    // methods

    // get task index
    private int getTaskIndex(String curTitle){
        int curTask = -1;

        for (TaskListItem task : _tasks) {
            curTask = _tasks.indexOf(task);
            if (_tasks.get(curTask).getTitle().equals(curTitle))
                return curTask;
        }
        return -1;  // if here, then did not find it
    }

    // add a task
    public void addTask(){
        String newTitle, newDesc, newPriority;
        String cancelFlag = "Cancel";

        // get the Title
        newTitle = ui.getTaskTitle("(type Cancel to Cancel adding a new task):",
                cancelFlag);
        if (newTitle.equals(cancelFlag))
            return;     // user is cancelling the add

        // get the Description
        newDesc = ui.getTaskDescription();
        // get the Priority
        newPriority = ui.getTaskPriority( 0, 5);
        // add it
        _tasks.add( new Task(newTitle, newDesc, newPriority));
    }

    // add a subtask
    public void addTask(String newTitle, String newDesc, String newPriority){
        _tasks.add( new Task(newTitle, newDesc, newPriority));
    }

    // remove a task
    public void removeTask(){
        String cancelFlag = "Cancel";
        String removeIt;
        int removeIndex;

        // get the title of the task to remove
        String removeTitle = ui.getTaskTitle("(type Cancel to Cancel removing a task):",
                cancelFlag);
        if (removeTitle.equals(cancelFlag)) {
            System.out.println("Remove canceled");
            return;     // user is cancelling the remove
        }
        // make sure the user wants to remove the task
        System.out.print("Are you sure? (Y/N)");
        removeIt = scanner.nextLine();
        if (removeIt.toUpperCase().contentEquals("Y")) {
            // agreeing to remove
            removeIndex = getTaskIndex(removeTitle);
            if ( removeIndex != -1 ) { // if it is a valid task
                // remove the task
                _tasks.remove(removeIndex);
            }
            else
                System.out.println("You do not have a task with the title '" + removeTitle + "' on your list");
        }
        else
            System.out.println("Remove canceled");
    }

    // edit a task
    public void editTask(){
        String cancelFlag = "Cancel";
        String editDescription, changeIt;
        String editPriority;
        int editIndex;

        // get the title of the task to edit
        String editTitle = ui.getTaskTitle("(type Cancel to Cancel editing a task):",
                cancelFlag);
        if (editTitle.equals(cancelFlag)) {
            System.out.println("Edit canceled");
            return;     // user is cancelling the edit
        }
        // make sure it is a valid task
        editIndex = getTaskIndex(editTitle);
        if ( editIndex != -1 ) {
            Task currentTask = _tasks.get(editIndex);
            // ask for new description
            System.out.println("Change the description? (Y/N)");
            changeIt = scanner.nextLine();
            if (changeIt.toUpperCase().contentEquals("Y"))
                editDescription = ui.getTaskDescription();
            else
                // keep it the same
                editDescription = currentTask.description;

            // ask for a new priority
            System.out.println("Change the priority? (Y/N)");
            changeIt = scanner.nextLine();
            if (changeIt.toUpperCase().contentEquals("Y"))
                editPriority = ui.getTaskPriority(0, 5);
            else
                // keep it the same
                editPriority = currentTask.priority;

            // now update it
            currentTask.priority = editPriority;
            currentTask.description = editDescription;
            _tasks.set(editIndex, currentTask);
        }
        else
            System.out.println("You do not have a task with the title '" + editTitle + "' on your list");
    }

    // List all tasks - just use display method

    // List tasks of a certain priority
    public void listPriorityTasks(String listPriority){
        // list all tasks in the list with that priority
        for (TaskListItem task : _tasks){
            if ( task.getPriority().equals(listPriority) ) //list it
                task.display();
        }
    }

    // List sorted tasks
    public void listSortedTasks(){
        List<Task> taskCopy = new ArrayList<>();

        taskCopy.addAll(_tasks);
        // first sort the tasks
        Collections.sort(taskCopy);

        // List them in sorted order
        for (Task task : taskCopy)
            task.display();
    }
}

class UserInterface {
    // class field
    private static Scanner scanner = new Scanner(System.in);

    // method to list the Menu Options
    private static void listMenuOptions(){

        System.out.println("Please choose an option:");
        System.out.println("(1) Add a task.");
        System.out.println("(2) Remove a task.");
        System.out.println("(3) Edit a task's description and/or priority.");
        System.out.println("(4) List all tasks.");
        System.out.println("(5) List tasks of a certain priority");
        System.out.println("(6) List sorted tasks");
        System.out.println("(0) Exit");
    }

    // method to get user input
    public int getUserOption(int loOpt, int hiOpt){

        String numberAsString;
        int userOption = loOpt;

        listMenuOptions();
        numberAsString = scanner.nextLine();
        boolean goodOption = false;
        while (!goodOption)
        {
            try{
                userOption = Integer.parseInt(numberAsString);     // Convert string to a integer
                if ( ( userOption >= loOpt ) && (userOption <= hiOpt) )  // check to see if range of options
                    goodOption = true;
                else {
                    System.out.println(userOption + " is not a valid option. ");
                    listMenuOptions();
                    numberAsString = scanner.nextLine();
                }
            }
            catch (NumberFormatException e) {
                System.out.println(numberAsString + " is not a valid option. ");
                listMenuOptions();
                numberAsString = scanner.nextLine();
            }
        }
        return userOption;
    }

    // get a title and make sure it is valid
    public String getTaskTitle(String donePrompt, String doneString ){
        // donePrompt: how to indicate you want to quit/cancel
        // doneString: if the input == the doneString then exit

        System.out.println("Enter the Title of the task " + donePrompt);
        String userInput = scanner.nextLine();

        String userString = "";
        boolean isString = false;
        while (!isString) {
            // if the string is the doneString, then return
            if ( userInput.toLowerCase().contentEquals(doneString.toLowerCase()) )
            {
                isString = true;
                userString = doneString;
            }
            else if ( userInput.equals("") )
            // if doneString is "" then captured above otherwise we want a string
            // so ask for another
            {
                System.out.println("You must enter a non-empty string.");
                System.out.println("Enter the Title of the task " + donePrompt);
                userInput = scanner.nextLine();
            }
            else
            {
                userString = userInput;
                isString = true;
            }
        }
        return userString;
    }

    // get a description and make sure it is valid
    public String getTaskDescription(){
        System.out.println("Enter the Description of the task:");
        String userInput = scanner.nextLine();

        String userString = "";
        boolean isString = false;
        while (!isString) {
            // if the string is null, then ask for another
            if ( userInput.equals("") )    // if doneString is "" then captured above otherwise we want a string
            {
                System.out.println("You must enter a non-empty string.");
                System.out.println("Enter the Description of the task:");
                userInput = scanner.nextLine();
            }
            else
            {
                userString = userInput;
                isString = true;
            }
        }
        return userString;
    }

    // get a priority and make sure it is valid
    public String getTaskPriority(int lowValue, int hiValue){
        // valid range of Priorities lowValue to hiValue

        // build the prompt for the priority
        String userPrompt = "Enter the Priority of the task (between " + lowValue + " (low) and " + hiValue + " (high)).";
        System.out.println(userPrompt);
        String userInput = scanner.nextLine();

        int userInt = 0;
        boolean isInt = false;
        while (!isInt) {
            try {
                userInt = Integer.parseInt(userInput);
                if ((userInt >= lowValue) && (userInt <= hiValue)) {
                    isInt = true;
                }
                else {
                    System.out.println("Priority must be between " + lowValue + " (low) and " + hiValue + " (high).");
                    System.out.println(userInput + " is not a valid priority. ");
                    System.out.println(userPrompt);
                    userInput = scanner.nextLine();
                }
            }
            catch (NumberFormatException e) {
                System.out.println(userInput + " is not a valid integer. ");
                System.out.println(userPrompt);
                userInput = scanner.nextLine();
            }
        }
        return userInput;
    }
}

public class Main {

    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        TaskList toDo = new TaskList();
        int userChoice;     // user input from the menu
        String listPriority;

        userChoice = ui.getUserOption(0,6);
        while (userChoice != 0){
            switch (userChoice) {
                case 1:         // Add a Task
                    toDo.addTask();
                    break;
                case 2:         // Remove a Task
                    toDo.removeTask();
                    break;
                case 3:         // Edit a Task
                    toDo.editTask();
                    break;
                case 4:         // List all Tasks
                    toDo.display();
                    break;
                case 5:         // List Tasks of a certain priority
                    // get the priority
                    listPriority = ui.getTaskPriority(0,5);
                    toDo.listPriorityTasks(listPriority);
                    break;
                case 6:         // List Tasks in order
                    toDo.listSortedTasks();
                    break;
            }
            userChoice = ui.getUserOption(0,6);
        }

        System.out.println("");
        System.out.println("A list of sub tasks");
        List<TaskListItem> bigList = new ArrayList<>();
        TaskList subList = new TaskList();

        bigList.add(new Task("Lone Task", "This task does not have any sub tasks", "3"));
        subList.addTask("Main Task 1", "This is the top task", "2");
        subList.addTask("Task 1.1", "Take out the trash", "4");
        subList.addTask("Task 1.2", "Do the laundry", "2");
        subList.addTask("Task 1.3", "Finish my homework", "5");
        bigList.add(subList);
        for (TaskListItem t : bigList){
           t.display();
        }


    }

}
