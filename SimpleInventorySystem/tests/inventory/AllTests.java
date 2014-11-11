package inventory;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestArticle.class, TestStock.class, TestStorageLocation.class })
public class AllTests {

}
