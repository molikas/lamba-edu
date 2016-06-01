package edu.rx.examples;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class IterationTest {

	private Iteration iteration = new Iteration();
	
	@Test
	public void printRegular() {
		iteration.printStrings();
	}

	@Test
	public void printRX() {
		iteration.rx_printStrings();
	}
	
	@Test
	public void printException() {
		iteration.names = Arrays.asList("Trump", "Hilary", null, "Ted");
		iteration.rx_printStrings();
	}
	
	@Test
	public void testMe(){
		NumberSum ns = new NumberSum();
		ns.sumInput();
	}
}
