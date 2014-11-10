package inventory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestStock {

	private Stock sut;
	
	@Before
	public void setUp() throws Exception {
		this.sut = new Stock();
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowErrorOnABitTooCold() {

		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowErrorOnABitTooWarm() {
		
	}

}
