package testing;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.Test;

import application.Task;

public class TestTask {
	
	public LocalDate dueDate = LocalDate.now().plusDays(3l);
	public Task t1 = new Task("first", "test1", Task.MEDIUM, dueDate, 30, "default");
	public Task t2 = new Task("second");

	@Test
	public void testName() {
		assertEquals(t1.getName(), "first");
		t1.setName("new Name");
		assertEquals(t1.getName(), "new Name");
		
		assertEquals(t2.getName(), "second");
		t2.setName("");
		assertEquals(t2.getName(), "UndefinedTask");
	}
	
	@Test
	public void testDetails() {
		assertEquals(t1.getDetails(), "test1");
		assertTrue(t1.setDetails("new Details"));
		assertEquals(t1.getDetails(), "new Details");

		String longDetails = String.join("", Collections.nCopies(55, "a"));
		assertFalse(t1.setDetails(longDetails));
		assertEquals(t1.getDetails(), String.join("", Collections.nCopies(50, "a")));
		
		assertNull(t2.getDetails());
	}
	
	@Test
	public void testPriority() {
		assertEquals(t1.getPriority(), Task.MEDIUM);
		assertTrue(t1.setPriority(Task.HIGH));
		assertEquals(t1.getPriority(), Task.HIGH);
		
		assertFalse(t1.setPriority(-55));
		assertEquals(t1.getPriority(), Task.HIGH);
		
		assertEquals(t2.getPriority(), Task.MEDIUM);
	}
	
	@Test
	public void testDeadline() {
		assertEquals(t1.getDeadline(), dueDate);
		assertTrue(t1.setDeadline(dueDate.plusDays(1l)));
		assertEquals(t1.getDeadline(), dueDate.plusDays(1l));
		
		assertFalse(t1.setDeadline(dueDate.minusDays(5l)));
		assertEquals(t1.getDeadline(), dueDate.plusDays(1l));
		
		assertNull(t2.getDeadline());
	}
	
	@Test
	public void testRepeatPeriod() {
		assertEquals(t1.getRepeatPeriod(), 30);
		assertTrue(t1.setRepeatPeriod(5));
		assertEquals(t1.getRepeatPeriod(), 5);
		
		assertFalse(t1.setRepeatPeriod(-2));
		assertEquals(t1.getRepeatPeriod(), 5);
		
		assertEquals(t2.getRepeatPeriod(), -1);
		assertTrue(t2.setRepeatPeriod(2));
		assertEquals(t2.getRepeatPeriod(), 2);
		assertEquals(t2.getDeadline(), LocalDate.now().plusDays(2));
	}
	
	@Test
	public void testCategory() {
		assertEquals(t1.getCategory(), "default");
		assertTrue(t1.setCategory("new Category"));
		assertEquals(t1.getCategory(), "new Category");
		
		String longCategory = String.join("", Collections.nCopies(17, "a"));
		assertFalse(t1.setCategory(longCategory));
		assertEquals(t1.getCategory(), "new Category");
		
		assertNull(t2.getCategory());
	}
	
	@Test
	public void testUrgency() {
		Task temp = new Task("temp", "testing urgency", Task.LOW, dueDate, 3, "default");
		assertEquals(temp.getUrgency(), Task.LOW - 1, 0.01d);
		Task defaultTemp = new Task("defaultTemp");
		assertEquals(defaultTemp.getUrgency(), Task.MEDIUM - (1d/0d), 0.01d);
	}
	
	@Test
	public void testIsActive() {
		Task defaultTemp = new Task("defaultTemp");
		assertTrue(t1.isActive());
		assertTrue(defaultTemp.isActive());
//		currently, condition of non-repeating task with deadline is unreachable
	}

}
