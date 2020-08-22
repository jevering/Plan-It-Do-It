package application;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author Jerry
 *
 */
public class User {
	private static final int MAX_COMPLETED_SIZE = 15;
	private static final int MAX_ACTIVE_SIZE = 15;
	private static final int MAX_WORK_SIZE = 365;
	
	public static final int MAX_NAME_LENGTH = 20;
	
	public static final int TF_WEEK = 1;
	public static final int TF_MONTH = 2;
	public static final int TF_YEAR = 3;
	
	private String name;
	private PriorityQueue<Task> activeTasks;
	private PriorityQueue<Task> completedTasks;
	private ArrayList<Integer> workHistory;
	private LocalDate lastUpdated;
	
	
	/**
	 * This is a constructor for a new user. Returns a new user instance
	 * @param name is the username of the user
	 */
	public User(String name) {
		this.setName(name);
		activeTasks = new PriorityQueue<>(MAX_ACTIVE_SIZE, new TaskComparator());
		completedTasks = new PriorityQueue<>(MAX_COMPLETED_SIZE);
		workHistory = new ArrayList<>(MAX_WORK_SIZE);
		workHistory.add(0,0);
		lastUpdated = LocalDate.now();
	}

	/**
	 * Getter for the currently active tasks
	 * @return the priority queue of all active tasks
	 */
	public PriorityQueue<Task> getActiveTasks() {
		return activeTasks;
	}
	
	/**
	 * Getter for the completed tasks
	 * @return the queue of all completed tasks, ordered by completion date
	 */
	public PriorityQueue<Task> getCompletedTasks() {
		return completedTasks;
	}

	/**
	 * Add a new task to the active tasklist
	 * @param task is the instance of the task to be added
	 * @return a boolean indicating the addition was successful. returns false if there are too many tasks
	 * and true otherwise
	 */
	public boolean newTask(Task task) {
		if (task != null && activeTasks.size() < MAX_ACTIVE_SIZE) {
			activeTasks.add(task);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Update the user profile by going through and adding work days that had not previously been added
	 * and remove expired tasks from the active tasklist
	 */
	public void update() {
		LocalDate today = LocalDate.now();
		long daysMissed = ChronoUnit.DAYS.between(lastUpdated, today);
		
		for (int i = 0; i < daysMissed; i++) {
			workHistory.add(0, 0);
		}
		
		Iterator<Task> activeIter = activeTasks.iterator();
		Task task;
		while (activeIter.hasNext()) {
			task = activeIter.next();
			if (!task.isActive()) {
				activeTasks.remove(task);
			}
		}
		
		lastUpdated = today;
	}
	
	/**
	 * Add work hours to a task
	 * @param task the task upon which was worked
	 * @param hours the new hours worked
	 * @return a boolean indicating if the task was in the active list
	 */
	public boolean addHours(Task task, int hours) {
		if (activeTasks.contains(task)) {
			task.addWorkHrs(hours);
			workHistory.set(0, workHistory.get(0) + hours);
			return true;
		}
		return false;
	}
	
	/**
	 * Completing a task removes it from the active list and adds it to the completed list
	 * @param task is the task which was completed
	 * @return if the task was originally in the active list
	 */
	public boolean finishTask(Task task) {
		boolean success = activeTasks.remove(task);
		if (success) {
			completedTasks.add(task);
			while (completedTasks.size() > MAX_COMPLETED_SIZE) {
				completedTasks.poll();
			}
		}
		
		return success;
	}
	
	/**
	 * Gets a list of all the categories which the user has used to specify tasks. The list has 
	 * not repeats
	 * @return a nonrepeating list of categories to be used for historical analysis.
	 */
	public List<String> getCategories() {
		ArrayList<String> categories = new ArrayList<>();
		ArrayList<Task> allTasks = new ArrayList<>();
		allTasks.addAll(activeTasks);
		allTasks.addAll(completedTasks);
		Iterator<Task> tasks = allTasks.iterator();
		Task task;
		String category;
		while (tasks.hasNext()) {
			task = tasks.next();
			category = task.getCategory();
			if (!categories.contains(category)) {
				categories.add(category);
			}
		}
		
		return categories;
	}
	
	/**
	 * Gets a list of hours worked among active and recently completed tasks, sorted by
	 * the category.
	 * @param categories an arraylist of categories for work time analysis
	 * @return the arraylist of hours worked in each of the categories provided
	 */
	public List<Integer> getWorkDistribution(List<String> categories) {
		int size = categories.size();
		Task task;
		int index;
		
		ArrayList<Integer> workDist = new ArrayList<>(size);
		
		ArrayList<Task> allTasks = new ArrayList<>();
		allTasks.addAll(activeTasks);
		allTasks.addAll(completedTasks);
		Iterator<Task> tasks = allTasks.iterator();
		
		while (tasks.hasNext()) {
			task = tasks.next();
			index = categories.indexOf(task.getCategory());
			if (index >= 0) {
				workDist.set(index, workDist.get(index) + task.getWorkHrs());
			}
		}
		
		return workDist;
	}
	
	/**
	 * Given a time frame, this function returns the number of days to be analyzed from
	 * the work history
	 * @param timeFrame is an indicator of the time to be analyzed, denoted by the constants,
	 * TF_WEEK, TF_MONTH, TF_YEAR
	 * @return the number of days to be analyzed
	 */
	public static int getHistSize(int timeFrame) {
		switch (timeFrame) {
			case TF_WEEK:
				return 7;
			case TF_MONTH:
				return 30;
			case TF_YEAR:
				return 365;
			default:
				return 0;
		}
	}
	
	/**
	 * Get the split for a specific time frame eg: a week is split by 1 day, a month
	 * is split by 1 week (7 days) and a year is split by 1 month (30 days)
	 * @param timeFrame the time frame to be analyzed (TF_WEEK, TF_MONTH, TF_YEAR)
	 * @return the split
	 */
	public static int getHistSplit(int timeFrame) {
		switch (timeFrame) {
			case TF_WEEK:
				return 1;
			case TF_MONTH:
				return 7;
			case TF_YEAR:
				return 30;
			default:
				return 0;
		}
	}
	
	/**
	 * Gets the work history for the past time frame
	 * @param timeFrame the time frame to be analyzed (TF_WEEK, TF_MONTH, TF_YEAR)
	 * @return an arraylist of the hours worked over the past timeframe
	 */
	public List<Integer> getWorkHistory(int timeFrame) {
		int size = getHistSize(timeFrame);
		int split = getHistSplit(timeFrame);
		int hours = 0;
		
		ArrayList<Integer> desiredHistory = new ArrayList<>();
		
		if (size != 0 && split != 0) {
			
			for (int i = 0; i < size/split; i++) {
				for (int j = 0; j < split; j++) {
					hours += workHistory.get(split*i + j);
				}
				desiredHistory.add(i, hours);
				hours = 0;
			}
		} 
		
		return desiredHistory;
	}

	public void resort(Task task) {
		activeTasks.remove(task);
		activeTasks.add(task);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		int nameLength = Math.min(name.length(), MAX_NAME_LENGTH);
		this.name = name.substring(0, nameLength);
	}

}
