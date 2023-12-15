package com.volodya262.adventofcode.day3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

	@ParameterizedTest
	@ValueSource(strings = {
			"""
			58+.......
			....592...
			.......755""",
			"""
			58........
			..+.592...
			.......755""",
			"""
			58........
			.+..592...
			.......755""",
			"""
			58........
			+...592...
			.......755"""
	})
	void isAdjacent_topLeftNumber__true(String s) {
		var lines = Arrays.stream(s.split(System.lineSeparator()))
				.filter(line -> !line.isBlank())
				.toList();

		var charsMatrix = lines.stream()
				.map(String::toCharArray)
				.toArray(char[][]::new);

		var i = 0;
		var jStart = 0;
		var jEnd = 1;

		var res = Main.isAdjacentForPartNumber(charsMatrix, i, jStart, jEnd);
		assertTrue(res);
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"""
			58........
			...+592...
			.......755""",
			"""
			58.+......
			....592...
			.......755""",
			"""
			58..+.....
			....592...
			.......755""",
			"""
			58...+....
			....592...
			.......755""",
			"""
			58....+...
			....592...
			.......755""",
			"""
			58.....+..
			....592...
			.......755""",
			"""
			58........
			....592+..
			.......755""",
			"""
			58........
			....592...
			......+755""",
			"""
			58........
			....592...
			.....+.755""",
			"""
			58........
			....592...
			....+..755""",
			"""
			58........
			....592...
			...+...755""",
			"""
			58........
			...+592...
			.......755"""
	})
	void isAdjacent_CenterNumber__true(String s) {
		var lines = Arrays.stream(s.split(System.lineSeparator()))
				.filter(line -> !line.isBlank())
				.toList();

		var charsMatrix = lines.stream()
				.map(String::toCharArray)
				.toArray(char[][]::new);

		var i = 1;
		var jStart = 4;
		var jEnd = 6;

		var res = Main.isAdjacentForPartNumber(charsMatrix, i, jStart, jEnd);
		assertTrue(res);
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"""
			58........
			....59....
			......+755""",
			"""
			58........
			....59.+..
			.......755""",
	})
	void isAdjacentForPartNumber_BottomRightNumber__true(String s) {
		var lines = Arrays.stream(s.split(System.lineSeparator()))
				.filter(line -> !line.isBlank())
				.toList();

		var charsMatrix = lines.stream()
				.map(String::toCharArray)
				.toArray(char[][]::new);

		var i = 2;
		var jStart = 7;
		var jEnd = 9;

		var res = Main.isAdjacentForPartNumber(charsMatrix, i, jStart, jEnd);
		assertTrue(res);
	}

	@Test
	void isAdjacentForPartNumber_edgeCase__false() {
		var s = """
				.......
				..803..
				......-
				""";

		var lines = Arrays.stream(s.split(System.lineSeparator()))
				.filter(line -> !line.isBlank())
				.toList();

		var charsMatrix = lines.stream()
				.map(String::toCharArray)
				.toArray(char[][]::new);

		var i = 1;
		var jStart = 2;
		var jEnd = 4;

		var res = Main.isAdjacentForPartNumber(charsMatrix, i, jStart, jEnd);
		assertFalse(res);
	}

	@Test
	void findPartNumbers() {
		var line = "58+.64..#.";
		var partNumbers = Main.findPartNumbers(0, line);

		var expected1 = new PartNumber(58, 0, 0, 1);
		var expected2 = new PartNumber(64, 0, 4, 5);

		assertEquals(2, partNumbers.size());
		assertEquals(expected1, partNumbers.get(0));
		assertEquals(expected2, partNumbers.get(1));

	}

	@Test
	void findPartNumbers2() {
		var line = "..803..";
		var partNumbers = Main.findPartNumbers(0, line);
		var expected = new PartNumber(803, 0, 2, 4);
		assertEquals(1, partNumbers.size());
		assertEquals(expected, partNumbers.getFirst());
	}

	@Test
	void findPartNumbers3() {
		var line = "..8....";
		var partNumbers = Main.findPartNumbers(0, line);
		var expected = new PartNumber(8, 0, 2, 2);
		assertEquals(1, partNumbers.size());
		assertEquals(expected, partNumbers.getFirst());
	}
}