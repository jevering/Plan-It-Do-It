package testing;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.Test;

import application.Task;

/**
 * @author Tian
 * 
 */
public class TaskTest {
	
	private final LocalDate dueDate = LocalDate.now().plusDays(3l);
	private static final String FIRST = "first";
	private static final String SECOND = "second";
	private static final String TEST1 = "test1";
	private static final String DEFAULT = "default";
	private static final String NEWCATEGORY = "new Category";

/*
 * Tests getting and setting name attribute of Task
 * Edge case of <blank> name being converted to UndefinedTask
 */
	@Test
	public void testName() {
		Task t1 = new Task(FIRST, TEST1, Task.MEDIUM, dueDate, 30, DEFAULT);
		assertEquals(FIRST, t1.getName());
		t1.setName("new Name");
		assertEquals("new Name", t1.getName());
		
		Task t2 = new Task(SECOND);
		assertEquals(SECOND, t2.getName());
		t2.setName("");
		assertEquals("UndefinedTask", t2.getName());
	}

/*
 * Tests getting and setting detail attribute of Task
 * Expects truncated return for detail longer than DETAILS_LENGTH = 50
 * Default constructor instance returning NULL
 */
	@Test
	public void testDetails() {
		Task t1 = new Task(FIRST, TEST1, Task.MEDIUM, dueDate, 30, DEFAULT);
		assertEquals(TEST1, t1.getDetails());
		assertTrue(t1.setDetails("new Details"));
		assertEquals("new Details", t1.getDetails());

		String longDetails = String.join("", Collections.nCopies(55, "a"));
		assertFalse(t1.setDetails(longDetails));
		assertEquals(String.join("", Collections.nCopies(50, "a")), t1.getDetails());

		Task t2 = new Task(SECOND);
		assertNull(t2.getDetails());
	}

/*
 * Tests getting and setting priority attribute of Task
 * Edge case of priority != LOW, MEDIUM, HIGH
 */
	@Test
	public void testPriority() {
		Task t1 = new Task(FIRST, TEST1, Task.MEDIUM, dueDate, 30, DEFAULT);
		assertEquals(Task.MEDIUM, t1.getPriority());
		assertTrue(t1.setPriority(Task.HIGH));
		assertEquals(Task.HIGH, t1.getPriority());
		
		assertFalse(t1.setPriority(-55));
		assertEquals(Task.HIGH, t1.getPriority());
		
		Task t2 = new Task(SECOND);
		assertEquals(Task.MEDIUM, t2.getPriority());
	}

/*
 * Tests getting and setting deadline attribute of Task
 * Edge case of setting deadline before now()
 * Default constructor instance returning NULL
 */
	@Test
	public void testDeadline() {
		Task t1 = new Task(FIRST, TEST1, Task.MEDIUM, dueDate, 30, DEFAULT);
		assertEquals(dueDate, t1.getDeadline());
		assertTrue(t1.setDeadline(dueDate.plusDays(1l)));
		assertEquals(dueDate.plusDays(1l), t1.getDeadline());
		
		assertFalse(t1.setDeadline(dueDate.minusDays(5l)));
		assertFalse(t1.setDeadline(null));
		assertEquals(dueDate.plusDays(1l), t1.getDeadline());
		
		Task t2 = new Task(SECOND);
		assertNull(t2.getDeadline());
	}

/*
 * Tests getting and setting repeatPeriod attribute of Task
 * Edge case of assigning negative values
 * Changing period of default constructor instance also changes deadline
 */
	@Test
	public void testRepeatPeriod() {
		Task t1 = new Task(FIRST, TEST1, Task.MEDIUM, dueDate, 30, DEFAULT);
		assertEquals(30, t1.getRepeatPeriod());
		assertTrue(t1.setRepeatPeriod(5));
		assertEquals(5, t1.getRepeatPeriod());
		
		assertFalse(t1.setRepeatPeriod(-2));
		assertEquals(5, t1.getRepeatPeriod());
		
		Task t2 = new Task(SECOND);
		assertEquals(-1, t2.getRepeatPeriod());
		assertTrue(t2.setRepeatPeriod(2));
		assertEquals(2, t2.getRepeatPeriod());
		assertEquals(LocalDate.now().plusDays(2), t2.getDeadline());
	}

/*
 * Tests getting and setting category attribute of Task
 * Expects no change for category length greater than CAT_LENGTH = 15
 * Default constructor instance returning NULL
 */
	@Test
	public void testCategory() {
		Task t1 = new Task(FIRST, TEST1, Task.MEDIUM, dueDate, 30, DEFAULT);
		assertEquals(DEFAULT, t1.getCategory());
		assertTrue(t1.setCategory(NEWCATEGORY));
		assertEquals(NEWCATEGORY, t1.getCategory());
		
		String longCategory = String.join("", Collections.nCopies(17, "a"));
		assertFalse(t1.setCategory(longCategory));
		assertEquals(NEWCATEGORY, t1.getCategory());
		
		Task t2 = new Task(SECOND);
		assertNull(t2.getCategory());
	}

/*
 * Tests getting and altering urgency of Task
 */	
	@Test
	public void testUrgency() {
		Task temp = new Task("temp", "testing urgency", Task.LOW, dueDate, 3, DEFAULT);
		assertEquals(Task.LOW - (3d/4d), temp.getUrgency(), 0.01d);
		Task defaultTemp = new Task("defaultTemp");
		assertEquals(Task.MEDIUM - (1d/(0+1d)), defaultTemp.getUrgency(), 0.01d);
	}

/*
 * Tests getting and setting active status of Task
 * False return is currently an unreachable condition
 */
	@Test
	public void testIsActive() {
		Task t1 = new Task(FIRST, TEST1, Task.MEDIUM, dueDate, 30, DEFAULT);
		Task t2 = new Task(SECOND, "test2", Task.MEDIUM, dueDate, -1, DEFAULT);
		Task defaultTemp = new Task("defaultTemp");
		assertTrue(t1.isActive());
		assertTrue(t2.isActive());
		assertTrue(defaultTemp.isActive());
	}

/*
 * Tests getting, setting, and adding to workHrs attribute of Task
 * Edge case of negative value input
 */
	@Test
	public void testAddWorkHrs() {
		Task t1 = new Task(FIRST, TEST1, Task.MEDIUM, dueDate, 30, DEFAULT);
		assertEquals(0, t1.getWorkHrs());
		Task t2 = new Task(SECOND);
		assertEquals(0, t2.getWorkHrs());
		assertTrue(t2.setWorkHrs(13));
		assertEquals(13, t2.getWorkHrs());
		
		assertTrue(t2.addWorkHrs(2));
		assertEquals(15, t2.getWorkHrs());
		
		assertFalse(t2.setWorkHrs(-1));
		assertEquals(15, t2.getWorkHrs());
		assertFalse(t2.addWorkHrs(-1));
		assertEquals(15, t2.getWorkHrs());
	}

}
