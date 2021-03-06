package application;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Task implements Comparable<Object> {
	public static final int LOW = 1;
	public static final int MEDIUM = 2;
	public static final int HIGH = 3;
	private static final int CAT_LENGTH = 15;
	private static final int DETAILS_LENGTH = 50;
	private static final int TASK_NAME_LENGTH = 15;
	
	private String name;
	private String details;
	private int priority;
	private LocalDate deadline;
	private LocalDate created;
	private int repeatPeriod;
	private String category;
	private int workHours;

	public Task(String name) {
		this.setName(name);
		details = null;
		priority = MEDIUM;
		deadline = null;
		created = LocalDate.now();
		repeatPeriod = -1;
		category = null;
		workHours = 0;
	}
	
	public Task(String name, String details, int priority, LocalDate deadline, 
			int repeatPeriod, String category) {
		this.setName(name);
		this.setDetails(details);
		this.setPriority(priority);
		created = LocalDate.now();
		this.setDeadline(deadline);
		this.setRepeatPeriod(repeatPeriod);
		this.setCategory(category);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (!name.isEmpty()) {
			this.name = name.substring(0, Math.min(name.length(), TASK_NAME_LENGTH));
		} else {
			this.name = "UndefinedTask";
		}
	}
	
	public String getDetails() {
		return details;
	}

	/**
	 * Sets the details of the task
	 * @param details is the description of the task, as collected from the user
	 * @return true if the details parameter is less than the size of the buffer
	 * and false if it is longer than the buffer. 
	 */
	public boolean setDetails(String details) {
		int end = Math.min(details.length(), DETAILS_LENGTH);
		this.details = details.substring(0, end);
		return (details.length() <= DETAILS_LENGTH);
	}
	
	public int getPriority() {
		return priority;
	}
	
	/**
	 * Set the priority of the task, as specified by the user
	 * @param priority the priority of the task and can be any of the task constants,
	 * LOW, MEDIUM, or HIGH
	 * @return true if the priority is valid and false otherwise
	 */
	public boolean setPriority(int priority) {
		if (priority == Task.LOW || priority == Task.MEDIUM || priority == Task.HIGH) {
			this.priority = priority;
			return true;
		} else {
			return false;
		}
	}
	
	public LocalDate getDeadline() {
		return deadline;
	}
	
	/**
	 * Sets the deadline of the task
	 * @param deadline is the deadline of the task, as collected from the user
	 * @return true if the deadline is after the created date and false otherwise
	 */
	public boolean setDeadline(LocalDate deadline) {
		if (deadline != null && deadline.isAfter(created)) {
			this.deadline = deadline;
			
			/* This if is to ensure that the repeat period is not shorter 
			 * than the task time from inception to completion, if the task
			 * is to be repeated at all.
			 */ 
			if (repeatPeriod > 0) {
				this.setRepeatPeriod(repeatPeriod);
			}
			return true;
		} else {
			return false;
		}
	}
	
	public int getRepeatPeriod() {
		return repeatPeriod;
	}
	
	/**
	 * Sets how often the task should be done. The deadline always supersedes 
	 * the period of repetition, so if a deadline has already been set, then that 
	 * is the fastest that a task can be repeated.
	 * @param days a positive integer
	 * @return true if days is positive and false otherwise
	 */
	public boolean setRepeatPeriod(int days) {
		if (days > 0) {
			/* Check if a deadline has been set and set it to the repeat period if not
			 */
			if (deadline == null) {
				this.setDeadline(LocalDate.now().plusDays(days));
			}
			
			/* If the days parameter is shorter than the time to the deadline, then set 
			 * the repeat period to the time to the deadline
			 */
			repeatPeriod = Math.max(days, (int) ChronoUnit.DAYS.between(created, deadline));
			return true;
		} else {
			return false;
		}
	}
	
	public String getCategory() {
		return category;
	}
	
	/**
	 * sets the category of the task
	 * @param cat is the category of the task, as defined by the user
	 * @return true if the category isn't too large, as defined by CAT_LENGTH, and
	 * false otherwise
	 */
	public boolean setCategory(String cat) {
		if (cat.isEmpty() || cat.length() > CAT_LENGTH) {
			return false;
		} else {
			category = cat;
			return true;
		}
	}

	/**
	 * Gets the urgency of the task, as calculated from the priority, date of 
	 * creation, date of completion, and current date
	 * @return the urgency
	 */
	public double getUrgency() {
		LocalDate today = LocalDate.now();
		double urgency = priority;
		long daysLeft;
		long daysPassed;
		long daysTotal;
		
		daysPassed = ChronoUnit.DAYS.between(created, today);
		if (deadline != null) {
			daysLeft = ChronoUnit.DAYS.between(today, deadline);
			daysTotal = ChronoUnit.DAYS.between(created, deadline);
			urgency -= (((double) daysLeft)/((double) daysTotal+1));
		} else {
			urgency -= (1/((double) daysPassed+1));
		}
		return urgency;
	}

	public boolean isActive() {
		LocalDate today = LocalDate.now();
		long daysPassed;
		long daysTotal;
		long maxDays;
		
		daysPassed = ChronoUnit.DAYS.between(created, today);
		
		if (repeatPeriod > 0) {
			// repeated tasks will stay active till the period of repetition ends
			maxDays = repeatPeriod;
		} else if (deadline != null) {
			daysTotal = ChronoUnit.DAYS.between(created, deadline);
			maxDays = daysTotal * 5 / 4; 
			/* Tasks with a deadline, but not repeated will remain active 25% longer
			 * than the duration of the task
			 */
		} else {
			// Tasks with no deadline and no repetition will always be active
			maxDays = daysPassed+1;
		}
		
		return (maxDays >= daysPassed);
	}
	
	public boolean addWorkHrs(int newHours) {
		if (newHours >= 0) {
			workHours += newHours;
			return true;
		}
		return false;
	}
	
	public boolean setWorkHrs(int totalHrs) {
		if (totalHrs >= 0) {
			workHours = totalHrs;
			return true;
		}
		return false;
	}
	
	public int getWorkHrs() {
		return workHours;
	}

	/* The default comparator for tasks is to assume they're equivalent
	 * Note: this class has a natural ordering that is inconsistent with equals.
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object arg0) {
		return 0;
	}
	
	/*
	 * Must compare object instances, since no unique ids are given to Tasks
	 * Comparison is needed for .contains() in lookups of active on complete Tasks
	 */
	@Override
	public boolean equals(Object arg0) {
		return this == arg0;
	}
	
	/*
	 * Assumes no actual use of hashCode()
	 * Reconsider this implementation if actually using in hash
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name, details, priority);
	}
}
