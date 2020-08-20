package testing;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import application.Task;
import application.TaskComparator;

public class TaskComparatorTest {

	@Test
	public void testEqPriority() {
		TaskComparator comp = new TaskComparator();
		
//		default constructor
		Task t1 = new Task("first");
		Task t2 = new Task("second");
		assertEquals(comp.compare(t1, t2), 0);
		
		LocalDate dueDate = LocalDate.now().plusDays(3l);
		t1 = new Task("first", "test1", 2, dueDate, 0, "default");
		t2 = new Task("second", "test1", 2, dueDate, 0, "default");
		assertEquals(comp.compare(t1, t2), 0);
	}
	
	@Test
	public void testUneqPriority() {
		LocalDate dueDate = LocalDate.now().plusDays(3l);
		Task t1 = new Task("first", "test1", 2, dueDate, 0, "default");
		Task t2 = new Task("second", "test1", 3, dueDate, 0, "default");
		TaskComparator comp = new TaskComparator();
		assertEquals(comp.compare(t1, t2), -1);
		
		t1 = new Task("first", "test1", 2, dueDate, 0, "default");
		t2 = new Task("second", "test1", 1, dueDate, 0, "default");
		assertEquals(comp.compare(t1, t2), 1);
	}

}
