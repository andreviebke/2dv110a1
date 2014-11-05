package inventory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestStorageLocation {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnNull() {
		new StorageLocation(null);
	}

}
