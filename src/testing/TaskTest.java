package testing;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.Test;

import application.Task;

public class TaskTest {
	
	public final LocalDate dueDate = LocalDate.now().plusDays(3l);

/*
 * Tests getting and setting name attribute of Task
 * Edge case of <blank> name being converted to UndefinedTask
 */
	@Test
	public void testName() {
		Task t1 = new Task("first", "test1", Task.MEDIUM, dueDate, 30, "default");
		assertEquals(t1.getName(), "first");
		t1.setName("new Name");
		assertEquals(t1.getName(), "new Name");
		
		Task t2 = new Task("second");
		assertEquals(t2.getName(), "second");
		t2.setName("");
		assertEquals(t2.getName(), "UndefinedTask");
	}

/*
 * Tests getting and setting detail attribute of Task
 * Expects truncated return for detail longer than DETAILS_LENGTH = 50
 * Default constructor instance returning NULL
 */
	@Test
	public void testDetails() {
		Task t1 = new Task("first", "test1", Task.MEDIUM, dueDate, 30, "default");
		assertEquals(t1.getDetails(), "test1");
		assertTrue(t1.setDetails("new Details"));
		assertEquals(t1.getDetails(), "new Details");

		String longDetails = String.join("", Collections.nCopies(55, "a"));
		assertFalse(t1.setDetails(longDetails));
		assertEquals(t1.getDetails(), String.join("", Collections.nCopies(50, "a")));

		Task t2 = new Task("second");
		assertNull(t2.getDetails());
	}

/*
 * Tests getting and setting priority attribute of Task
 * Edge case of priority != LOW, MEDIUM, HIGH
 */
	@Test
	public void testPriority() {
		Task t1 = new Task("first", "test1", Task.MEDIUM, dueDate, 30, "default");
		assertEquals(t1.getPriority(), Task.MEDIUM);
		assertTrue(t1.setPriority(Task.HIGH));
		assertEquals(t1.getPriority(), Task.HIGH);
		
		assertFalse(t1.setPriority(-55));
		assertEquals(t1.getPriority(), Task.HIGH);
		
		Task t2 = new Task("second");
		assertEquals(t2.getPriority(), Task.MEDIUM);
	}

/*
 * Tests getting and setting deadline attribute of Task
 * Edge case of setting deadline before now()
 * Default constructor instance returning NULL
 */
	@Test
	public void testDeadline() {
		Task t1 = new Task("first", "test1", Task.MEDIUM, dueDate, 30, "default");
		assertEquals(t1.getDeadline(), dueDate);
		assertTrue(t1.setDeadline(dueDate.plusDays(1l)));
		assertEquals(t1.getDeadline(), dueDate.plusDays(1l));
		
		assertFalse(t1.setDeadline(dueDate.minusDays(5l)));
		assertFalse(t1.setDeadline(null));
		assertEquals(t1.getDeadline(), dueDate.plusDays(1l));
		
		Task t2 = new Task("second");
		assertNull(t2.getDeadline());
	}

/*
 * Tests getting and setting repeatPeriod attribute of Task
 * Edge case of assigning negative values
 * Changing period of default constructor instance also changes deadline
 */
	@Test
	public void testRepeatPeriod() {
		Task t1 = new Task("first", "test1", Task.MEDIUM, dueDate, 30, "default");
		assertEquals(t1.getRepeatPeriod(), 30);
		assertTrue(t1.setRepeatPeriod(5));
		assertEquals(t1.getRepeatPeriod(), 5);
		
		assertFalse(t1.setRepeatPeriod(-2));
		assertEquals(t1.getRepeatPeriod(), 5);
		
		Task t2 = new Task("second");
		assertEquals(t2.getRepeatPeriod(), -1);
		assertTrue(t2.setRepeatPeriod(2));
		assertEquals(t2.getRepeatPeriod(), 2);
		assertEquals(t2.getDeadline(), LocalDate.now().plusDays(2));
	}

/*
 * Tests getting and setting category attribute of Task
 * Expects no change for category length greater than CAT_LENGTH = 15
 * Default constructor instance returning NULL
 */
	@Test
	public void testCategory() {
		Task t1 = new Task("first", "test1", Task.MEDIUM, dueDate, 30, "default");
		assertEquals(t1.getCategory(), "default");
		assertTrue(t1.setCategory("new Category"));
		assertEquals(t1.getCategory(), "new Category");
		
		String longCategory = String.join("", Collections.nCopies(17, "a"));
		assertFalse(t1.setCategory(longCategory));
		assertEquals(t1.getCategory(), "new Category");
		
		Task t2 = new Task("second");
		assertNull(t2.getCategory());
	}

/*
 * Tests getting and altering urgency of Task
 */	
	@Test
	public void testUrgency() {
		Task temp = new Task("temp", "testing urgency", Task.LOW, dueDate, 3, "default");
		assertEquals(temp.getUrgency(), Task.LOW - 1, 0.01d);
		Task defaultTemp = new Task("defaultTemp");
		assertEquals(defaultTemp.getUrgency(), Task.MEDIUM - (1d/0d), 0.01d);
	}

/*
 * Tests getting and setting active status of Task
 * False return is currently an unreachable condition
 */
	@Test
	public void testIsActive() {
		Task t1 = new Task("first", "test1", Task.MEDIUM, dueDate, 30, "default");
		Task t2 = new Task("second", "test2", Task.MEDIUM, dueDate, -1, "default");
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
		Task t1 = new Task("first", "test1", Task.MEDIUM, dueDate, 30, "default");
		assertEquals(t1.getWorkHrs(), 0);
		Task t2 = new Task("second");
		assertEquals(t2.getWorkHrs(), 0);
		assertTrue(t2.setWorkHrs(13));
		assertEquals(t2.getWorkHrs(), 13);
		
		assertTrue(t2.addWorkHrs(2));
		assertEquals(t2.getWorkHrs(), 15);
		
		assertFalse(t2.setWorkHrs(-1));
		assertEquals(t2.getWorkHrs(), 15);
		assertFalse(t2.addWorkHrs(-1));
		assertEquals(t2.getWorkHrs(), 15);
	}

}
