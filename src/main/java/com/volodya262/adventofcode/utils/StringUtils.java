package com.volodya262.adventofcode.utils;

import java.util.List;

public class StringUtils {
	public static char[][] toCharArray(List<String> lines) {
		return lines.stream()
				.map(String::toCharArray)
				.toArray(char[][]::new);
	}
}
