package example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExampleTest {

	@Test
	public void simpleTest() {
		final String value = "content";
		assertEquals("content",value);
	}
}
