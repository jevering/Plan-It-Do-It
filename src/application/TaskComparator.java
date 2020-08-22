package application;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {

	public int compare(Task task1, Task task2) {
		double priority1 = task1.getUrgency();
		double priority2 = task2.getUrgency();
		
		if (priority1 > priority2) {
			return -1;
		} else if (priority1 < priority2) {
			return 1;
		} else {
			return 0;
		}
	}

}
