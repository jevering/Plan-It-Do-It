package testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TaskComparatorTest.class, TaskTest.class, UserTest.class, UserTest2.class })
public class AllTests {

}
