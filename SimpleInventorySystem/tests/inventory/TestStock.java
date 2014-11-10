package inventory;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestStock {

	private Stock sut;
	private static double JUST_BELOW_LOW = Stock.MIN_TEMP - 0.1;
	private static double JUST_ABOVE_HIGH = Stock.MAX_TEMP + 0.1;
	private static double JUST_BELOW_HIGH = Stock.MAX_TEMP - 0.1;
	private static double JUST_ABOVE_LOW = Stock.MIN_TEMP + 0.1;

	@Before
	public void setUp() throws Exception {
		this.sut = new Stock();
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowErrorOnABitTooCold() {
		this.sut.setTemperature(TestStock.JUST_BELOW_LOW);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowErrorOnABitTooWarm() {
		this.sut.setTemperature(TestStock.JUST_ABOVE_HIGH);
	}

	@Test
	public void shouldSetTemparatureWhenJustWarmEnough() {
		this.sut.setTemperature(TestStock.JUST_ABOVE_LOW);
		assertEquals(TestStock.JUST_ABOVE_LOW, this.sut.getTemperature());
	}

	@Test
	public void shouldSetTemparatureWhenJustColdEnough() {
		this.sut.setTemperature(TestStock.JUST_BELOW_HIGH);
		assertEquals(TestStock.JUST_BELOW_HIGH, this.sut.getTemperature());
	}

}
