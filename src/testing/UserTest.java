package testing;

import static org.junit.Assert.*;

import java.time.LocalDate;
import org.junit.Test;

import application.Task;
import application.User;

/**
 * @author Meghan
 * 
 */
public class UserTest {
	private User user;
	private Task def;
	private static final String TESTING = "testing";
	private static final String CUSTOM = "custom";
	private static final String DEFAULT = "default";
	private static final String USERNAME = "username";
	
	/*
	 * Tests functionality of adding new task
	 * Checks initial size of Task lists
	 */
	@Test
	public void testAddTasks() {
		user = new User(USERNAME);
		assertEquals(user.getActiveTasks().size(), 0);
		assertEquals(user.getCompletedTasks().size(), 0);
		
		def = new Task(DEFAULT);
		
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
		user = new User(USERNAME);
		Task custom = new Task(CUSTOM, "N/A", Task.LOW, LocalDate.now().plusDays(-30l), 1, TESTING);
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
		user = new User(USERNAME);
		def = new Task(DEFAULT);
		Task custom = new Task(CUSTOM, "N/A", Task.LOW, LocalDate.now().plusDays(3l), 1, TESTING);
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
		user = new User(USERNAME);
		def = new Task(DEFAULT);
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

}
