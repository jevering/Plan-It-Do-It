package testing;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import application.Task;
import application.User;

public class UserTest {
	/*
	 * Checks initial size of Task lists
	 * Tests adding new tasks to activeTasks
	 */
	@Test
	public void testAddTasks() {
		User user = new User("Bob");
		assertEquals(user.getActiveTasks().size(), 0);
		assertEquals(user.getCompletedTasks().size(), 0);
		
		Task def = new Task("default");
		
		assertTrue(user.newTask(def));
		assertTrue(user.newTask(def));
		assertFalse(user.newTask(null));
		assertEquals(2, user.getActiveTasks().size());
		assertEquals(0, user.getCompletedTasks().size());
	}
	
	@Test
	public void testUpdate() {
		User user = new User("Bob");
		Task custom = new Task("custom", "N/A", Task.LOW, LocalDate.now().plusDays(-30l), 1, "testing");
		user.newTask(custom);
		assertEquals(1, user.getActiveTasks().size());
		user.update();
		assertEquals(1, user.getActiveTasks().size());		
	}
	
	@Test
	public void testFinishTasks() {
		User user = new User("Alice");
		Task def = new Task("default");
		Task custom = new Task("custom", "N/A", Task.LOW, LocalDate.now().plusDays(3l), 1, "testing");
		for(int i=0; i < 14; i++) {
			user.newTask(custom);
		}
		assertTrue(user.newTask(custom));
		assertFalse(user.newTask(def));
		assertTrue(user.finishTask(custom));
		assertFalse(user.finishTask(def));
		
		assertEquals(14, user.getActiveTasks().size());
		assertEquals(1, user.getCompletedTasks().size());		
	}
	
	@Test
	public void testFinishTasksOverflow() {
		User user = new User("Alice");
		Task def = new Task("default");
		for(int i=0; i < 15; i++) {
			user.newTask(def);
			user.finishTask(def);
		}
		assertEquals(15, user.getCompletedTasks().size());
		user.newTask(def);
		assertTrue(user.finishTask(def));
		assertEquals(15, user.getCompletedTasks().size());
	}

	@Test
	public void testWorkHrs() {
		User user = new User("Mallory");
		Task def = new Task("default");
		assertFalse(user.addHours(def, 2));
		
		user.newTask(def);
		assertEquals(0, def.getWorkHrs());
		assertTrue(user.addHours(def, 2));
		assertEquals(2, def.getWorkHrs());
	}
	
	@Test
	public void testCategories() {
		User user = new User("Alice");
		Task def = new Task("default");
		Task custom = new Task("custom", "N/A", Task.LOW, LocalDate.now().plusDays(3l), 1, "testing");
		assertTrue(user.getCategories().isEmpty());
		user.newTask(def);
		user.newTask(def);
		user.newTask(custom);
		assertEquals(user.getCategories().size(), 2);
	}
	
	@Test
	public void testWorkDist() {
		User user = new User("Alice");
		Task custom1 = new Task("custom1", "N/A", Task.LOW, LocalDate.now().plusDays(3l), 1, "testing");
		Task custom2 = new Task("custom2", "N/A", Task.LOW, LocalDate.now().plusDays(4l), 2, "testing");
		Task custom3 = new Task("custom3", "prod", Task.MEDIUM, LocalDate.now().plusDays(10l), 3, "production");
		Task def = new Task("default");
		user.newTask(custom1);
		user.newTask(custom2);
		user.newTask(def);
		user.addHours(custom1, 3);
		user.addHours(custom2, 1);
		user.addHours(def, 15);
		user.finishTask(custom1);
		user.newTask(custom3);
		user.addHours(custom2, 0);
		
		ArrayList<String> relevant = new ArrayList<>(Arrays.asList("testing", "production"));
		ArrayList<Integer> workDist = user.getWorkDistribution(relevant);
		assertTrue(workDist.get(0) == 4);
		assertTrue(workDist.get(1) == 0);
	}
	
	@Test
	public void testHistSplit() {
		assertEquals(7, User.getHistSize(1));
		assertEquals(30, User.getHistSize(2));
		assertEquals(365, User.getHistSize(3));
		assertEquals(0, User.getHistSize(0));
		
		assertEquals(1, User.getHistSplit(1));
		assertEquals(7, User.getHistSplit(2));
		assertEquals(30, User.getHistSplit(3));
		assertEquals(0, User.getHistSplit(0));
	}
	
	@Test
	public void testWorkHist() {
		User user = new User("Alice");
		Task custom1 = new Task("custom1", "N/A", Task.LOW, LocalDate.now().plusDays(3l), 1, "testing");
		Task custom2 = new Task("custom3", "prod", Task.MEDIUM, LocalDate.now().plusDays(10l), 3, "production");
		assertNull(user.getWorkHistory(-1));
		user.newTask(custom1);
		user.newTask(custom2);
		user.addHours(custom1, 9);
		user.addHours(custom2, 15);
		user.addHours(custom1, 2);
		ArrayList<Integer> hist = user.getWorkHistory(User.TF_WEEK);
		assertEquals(26, (int)hist.get(0));
	}
}
