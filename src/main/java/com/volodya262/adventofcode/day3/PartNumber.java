package com.volodya262.adventofcode.day3;

public record PartNumber(int number, int i, int jStart, int jEnd) {
	static PartNumber of(String number, int i, int jStart, int jEnd) {
		return new PartNumber(Integer.parseInt(number), i, jStart, jEnd);
	}
}
