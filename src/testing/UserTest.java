package testing;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import application.Task;
import application.User;

/**
 * @author Tian
 * 
 */
public class UserTest {
	public User user;
	public Task def;
	
	/*
	 * Tests functionality of adding new task
	 * Checks initial size of Task lists
	 */
	@Test
	public void testAddTasks() {
		user = new User("Bob");
		assertEquals(user.getActiveTasks().size(), 0);
		assertEquals(user.getCompletedTasks().size(), 0);
		
		def = new Task("default");
		
		assertTrue(user.newTask(def));
		assertTrue(user.newTask(def));
		assertFalse(user.newTask(null));
		assertEquals(2, user.getActiveTasks().size());
		assertEquals(0, user.getCompletedTasks().size());
	}
	
	/*
	 * Tests inaction of update() on tasks which are still active
	 * Unreachable code: removes tasks which are no longer active
	 */
	@Test
	public void testUpdate() {
		user = new User("Bob");
		Task custom = new Task("custom", "N/A", Task.LOW, LocalDate.now().plusDays(-30l), 1, "testing");
		user.newTask(custom);
		assertEquals(1, user.getActiveTasks().size());
		user.update();
		assertEquals(1, user.getActiveTasks().size());		
	}
	
	/*
	 * Tests normal case of marking tasks as finished
	 * Unreachable case of completedTasks.size() > MAX_COMPLETED_SIZE
	 * This is due to being unable to add tasks beyond MAX_COMPLETED_SIZE in newTask()
	 */
	@Test
	public void testFinishTasks() {
		user = new User("Alice");
		def = new Task("default");
		Task custom = new Task("custom", "N/A", Task.LOW, LocalDate.now().plusDays(3l), 1, "testing");
		for(int i=0; i < 5; i++) {
			assertTrue(user.newTask(custom));
		}
		for(int i=0; i < 3; i++) {
			assertTrue(user.finishTask(custom));
		}
		assertEquals(2, user.getActiveTasks().size());
		assertEquals(3, user.getCompletedTasks().size());		
	}
	
	/*
	 * Tests special cases of marking tasks as finished
	 * Addresses case of trying to fill active or complete beyond capacity
	 */
	@Test
	public void testFinishTasksOverflow() {
		user = new User("Alice");
		def = new Task("default");
		for(int i=0; i < 15; i++) {
			user.newTask(def);
		}
		assertEquals(15, user.getActiveTasks().size());
		assertFalse(user.newTask(def));
		assertEquals(15, user.getActiveTasks().size());

		for(int i=0; i < 15; i++) {
			user.finishTask(def);
		}
		assertEquals(15, user.getCompletedTasks().size());
		assertFalse(user.finishTask(def));
		assertEquals(15, user.getCompletedTasks().size());
	}

	/*
	 * Tests getting and adding hours of work for a given task
	 * Covers case of adding to a Task which itself hasn't been added to the user
	 */
	@Test
	public void testWorkHrs() {
		user = new User("Mallory");
		def = new Task("default");
		assertFalse(user.addHours(def, 2));
		
		user.newTask(def);
		assertEquals(0, def.getWorkHrs());
		assertTrue(user.addHours(def, 2));
		assertEquals(2, def.getWorkHrs());
	}
	
	/*
	 * Tests retrieval of categories from active and completed tasks
	 * Note: loses information when tasks are removed from completed
	 */
	@Test
	public void testCategories() {
		user = new User("Alice");
		def = new Task("default");
		Task custom = new Task("custom", "N/A", Task.LOW, LocalDate.now().plusDays(3l), 1, "testing");
		assertTrue(user.getCategories().isEmpty());
		user.newTask(def);
		user.newTask(def);
		user.newTask(custom);
		user.finishTask(custom);
		assertEquals(user.getCategories().size(), 2);
	}
	
	/*
	 * Tests work distribution over the current categories in active and completed
	 * Covers case of null category and a nonexistent category
	 */
	@Test
	public void testWorkDist() {
		user = new User("Alice");
		Task custom1 = new Task("custom1", "N/A", Task.LOW, LocalDate.now().plusDays(3l), 1, "testing");
		Task custom2 = new Task("custom2", "N/A", Task.LOW, LocalDate.now().plusDays(4l), 2, "testing");
		Task custom3 = new Task("custom3", "prod", Task.MEDIUM, LocalDate.now().plusDays(10l), 3, "production");
		def = new Task("default");
		user.newTask(custom1);
		user.newTask(custom2);
		user.newTask(def);
		user.addHours(custom1, 3);
		user.addHours(custom2, 1);
		user.addHours(def, 15);
		user.finishTask(custom1);
		user.newTask(custom3);
		user.addHours(custom2, 0);
		
		ArrayList<String> relevant = new ArrayList<>(Arrays.asList("testing", "production", null, "nonexistent"));
		ArrayList<Integer> workDist = user.getWorkDistribution(relevant);
		assertTrue(workDist.get(0) == 4);
		assertTrue(workDist.get(1) == 0);
	}
	
	/*
	 * Tests access of static time frame variables in User class
	 */
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
	
	/*
	 * Tests retrieval of work history
	 * 
	 */
	@Test
	public void testWorkHist() {
		user = new User("Alice");
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
