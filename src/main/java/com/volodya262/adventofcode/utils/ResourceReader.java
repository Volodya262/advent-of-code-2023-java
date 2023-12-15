package com.volodya262.adventofcode.utils;

import com.volodya262.adventofcode.day3.Main;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ResourceReader {
	public static List<String> readLines(String resourcePath) throws IOException {
		try (var resource = ResourceReader.class.getResourceAsStream(resourcePath)) {
			if (resource == null) {
				throw new RuntimeException("Resource not found: " + resourcePath);
			}

			var resourceAsString = new String(resource.readAllBytes());
			return Arrays.stream(resourceAsString.split(System.lineSeparator()))
					.filter(line -> !line.isBlank())
					.toList();
		}
	}
}
