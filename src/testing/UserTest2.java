package testing;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import application.Task;
import application.User;

public class UserTest2 {
	private User user;
	private Task def;
	private static final String TESTING = "testing";
	private static final String CUSTOM = "custom";
	private static final String DEFAULT = "default";
	private static final String PRODUCTION = "production";
	private static final String USERNAME = "username";

	/*
	 * Tests getting and adding hours of work for a given task
	 * Covers case of adding to a Task which itself hasn't been added to the user
	 */
	@Test
	public void testWorkHrs() {
		user = new User(USERNAME);
		def = new Task(DEFAULT);
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
		user = new User(USERNAME);
		def = new Task(DEFAULT);
		Task custom = new Task(CUSTOM, "N/A", Task.LOW, LocalDate.now().plusDays(3l), 1, TESTING);
		assertTrue(user.getCategories().isEmpty());
		user.newTask(def);
		user.newTask(def);
		user.newTask(custom);
		user.finishTask(custom);
		assertEquals(2, user.getCategories().size());
	}
	
	/*
	 * Tests work distribution over the current categories in active and completed
	 * Covers case of null category and a nonexistent category
	 */
	@Test
	public void testWorkDist() {
		user = new User(USERNAME);
		Task custom1 = new Task("custom1", "N/A", Task.LOW, LocalDate.now().plusDays(3l), 1, TESTING);
		Task custom2 = new Task("custom2", "N/A", Task.LOW, LocalDate.now().plusDays(4l), 2, TESTING);
		Task custom3 = new Task("custom3", "prod", Task.MEDIUM, LocalDate.now().plusDays(10l), 3, PRODUCTION);
		def = new Task(DEFAULT);
		user.newTask(custom1);
		user.newTask(custom2);
		user.newTask(def);
		user.addHours(custom1, 3);
		user.addHours(custom2, 1);
		user.addHours(def, 15);
		user.finishTask(custom1);
		user.newTask(custom3);
		user.addHours(custom2, 0);
		
		ArrayList<String> relevant = new ArrayList<>(Arrays.asList(TESTING, PRODUCTION, null, "nonexistent"));
		ArrayList<Integer> workDist = (ArrayList<Integer>) user.getWorkDistribution(relevant);
		assertTrue(workDist.get(0) == 4);
		assertTrue(workDist.get(1) == 0);
		assertTrue(workDist.get(2) == 15);
		assertTrue(workDist.get(3) == 0);
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
	 */
	@Test
	public void testWorkHist() {
		user = new User(USERNAME);
		Task custom1 = new Task("custom1", "N/A", Task.LOW, LocalDate.now().plusDays(3l), 1, TESTING);
		Task custom2 = new Task("custom3", "prod", Task.MEDIUM, LocalDate.now().plusDays(10l), 3, PRODUCTION);
		assertTrue(user.getWorkHistory(-1).size() == 0);
		user.newTask(custom1);
		user.newTask(custom2);
		user.addHours(custom1, 9);
		user.addHours(custom2, 15);
		user.addHours(custom1, 2);
		ArrayList<Integer> hist = (ArrayList<Integer>) user.getWorkHistory(User.TF_WEEK);
		assertEquals(26, (int)hist.get(0));
	}
	
	/*
	 * Tests getting and setting of name attribute
	 * Covers case of name length > MAX_NAME_LENGTH
	 */
	@Test
	public void testName() {
		user = new User(USERNAME);
		assertEquals(USERNAME, user.getName());
		user.setName("not" + USERNAME);
		assertEquals("not" + USERNAME, user.getName());
		
		String maxsizeName = String.join("", Collections.nCopies(User.MAX_NAME_LENGTH, "a"));
		user.setName(maxsizeName + "a");
		assertEquals(maxsizeName, user.getName());
	}

}
