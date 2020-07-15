package application;

import java.util.Date;

public class Task {
	private static final int LOW = 1;
	private static final int MEDIUM = 2;
	private static final int HIGH = 3;
	
	private String name;
	private StringBuffer details = new StringBuffer(25);
	private int priority = MEDIUM;
	private Date deadline;
	private Date created;
	private int repeatPeriod = -1;
	private String category;

	public Task(String name) {
		this.name = name;
		// TODO Auto-generated constructor stub
	}

	public double getUrgency() {
		// TODO Auto-generated method stub
		return 0;
	}

}
