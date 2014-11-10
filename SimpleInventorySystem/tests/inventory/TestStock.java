package inventory;

import static org.junit.Assert.*;

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
		this.sut.setTemperature(19.9);		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowErrorOnABitTooWarm() {
		this.sut.setTemperature(30.1);	
	}
	
	@Test
	public void shouldSetTemparatureWhenJustWarmEnough() {
		this.sut.setTemperature(20.1);
		assertEquals(20.1, this.sut.getTemperature());
	}
	
	@Test
	public void shouldSetTemparatureWhenJustColdEnough() {
		this.sut.setTemperature(29.9);	
		assertEquals(29.9, this.sut.getTemperature());
	}

}
