package testing;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import application.Task;
import application.TaskComparator;

public class TaskComparatorTest {
	private static final String FIRST = "first";
	private static final String SECOND = "second";
	private static final String TEST1 = "test1";
	private static final String DEFAULT = "default";

	@Test
	public void testEqPriority() {
		TaskComparator comp = new TaskComparator();
		
//		default constructor
		Task t1 = new Task(FIRST);
		Task t2 = new Task(SECOND);
		assertEquals(comp.compare(t1, t2), 0);
		
		LocalDate dueDate = LocalDate.now().plusDays(3l);
		t1 = new Task(FIRST, TEST1, 2, dueDate, 0, DEFAULT);
		t2 = new Task(SECOND, TEST1, 2, dueDate, 0, DEFAULT);
		assertEquals(comp.compare(t1, t2), 0);
	}
	
	@Test
	public void testUneqPriority() {
		LocalDate dueDate = LocalDate.now().plusDays(3l);
		Task t1 = new Task(FIRST, TEST1, 2, dueDate, 0, DEFAULT);
		Task t2 = new Task(SECOND, TEST1, 3, dueDate, 0, DEFAULT);
		TaskComparator comp = new TaskComparator();
		assertEquals(1, comp.compare(t1, t2));
		
		t1 = new Task(FIRST, TEST1, 2, dueDate, 0, DEFAULT);
		t2 = new Task(SECOND, TEST1, 1, dueDate, 0, DEFAULT);
		assertEquals(-1, comp.compare(t1, t2));
	}

}
