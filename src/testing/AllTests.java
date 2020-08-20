package testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TaskComparatorTest.class, TaskTest.class, UserTest.class })
public class AllTests {

}
