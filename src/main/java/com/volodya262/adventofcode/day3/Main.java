package com.volodya262.adventofcode.day3;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.volodya262.adventofcode.utils.ResourceReader;
import com.volodya262.adventofcode.utils.StringUtils;

public class Main {
	public static void main(String[] args) throws IOException {
		var lines = ResourceReader.readLines("/day3/input.txt");
		var charsMatrix = StringUtils.toCharArray(lines);
		var adjacentPartNumbers = findAdjacentPartNumbers(lines, charsMatrix);

		var sum = adjacentPartNumbers.stream()
				.mapToInt(PartNumber::number)
				.sum();

		System.out.println(sum);
	}

	private static List<GearRatio> findGearRatios(char[][] charsMatrix) {
		var gearRatios = new ArrayList<GearRatio>();

		for (int i = 0; i < charsMatrix.length; ++i) {
			for (int j = 0; j < charsMatrix[i].length; ++j) {
				if (charsMatrix[i][j] == '*') {
					// this is a potential gear ratio
					var gearRatio = tryCreateGearRatio(charsMatrix, i, j);
					if (gearRatio != null) {
						gearRatios.add(gearRatio);
					}
				}
			}
		}

		return gearRatios;
	}

	private static List<PartNumber> findAdjacentPartNumbers(List<String> lines, char[][] charsMatrix) {
		var adjacentPartNumbers = new ArrayList<PartNumber>();

		for (int i = 0; i < lines.size(); ++i) {
			var partNumbersInLine = findPartNumbers(i, lines.get(i));

			var adjacentPartNumbersInLine = partNumbersInLine.stream()
					.filter(partNumber -> isAdjacentForPartNumber(
							charsMatrix, partNumber.i(), partNumber.jStart(), partNumber.jEnd()
					)).toList();

			adjacentPartNumbers.addAll(adjacentPartNumbersInLine);
		}

		return adjacentPartNumbers;
	}

	private final static Pattern partNumberPattern = Pattern.compile("(\\d+)");

	public static List<PartNumber> findPartNumbers(int lineIndex, String line) {
		var matcher = partNumberPattern.matcher(line);
		var partNumbers = new ArrayList<PartNumber>();
		while (matcher.find()) {
			var number = matcher.group();
			var jStart = matcher.start();
			var jEnd = matcher.end() - 1;

			partNumbers.add(PartNumber.of(number, lineIndex, jStart, jEnd));
		}

		return partNumbers;
	}

	public record GearRatio(int i, int j, int value1, int value2) {

	}

	public record GearRatioNumber(int value, int i, int jStart, int jEnd) {

	}

	private record Offset(int i, int j) {
	}

	private static final Offset[] offsets = new Offset[]{
			new Offset(0, -1), // left
			new Offset(-1, -1), // top-left
			new Offset(-1, 0), // top
			new Offset(-1, 1), // top-right
			new Offset(0, 1), // right
			new Offset(1, 1), // bottom-right
			new Offset(1, 0), // bottom
			new Offset(1, -1) // bottom-left
	};

	public static boolean isAdjacentForPartNumber(char[][] charsMatrix, int i, int jStart, int jEnd) {
		return isAdjacent(charsMatrix, i, jStart, jEnd, Main::isAdjacentPartNumberSymbol);
	}

	public static boolean isAdjacent(char[][] charsMatrix, int i, int jStart, int jEnd, Predicate<Character> isSymbol) {
		for (int j = jStart; j <= jEnd; j++) {
			for (Offset offset : offsets) {
				var iWithOffset = i + offset.i;
				var jWithOffset = j + offset.j;

				if (iWithOffset >= 0 && iWithOffset < charsMatrix.length
					&& jWithOffset >= 0 && jWithOffset < charsMatrix[iWithOffset].length) {
					var isAdjacent = isSymbol.test(charsMatrix[iWithOffset][jWithOffset]);

					if (isAdjacent) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private final static Set<Character> excludedSymbolsSet = Set.of(
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'
	);

	public static boolean isAdjacentPartNumberSymbol(char c) {
		return !excludedSymbolsSet.contains(c);
	}

	public static boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	public static GearRatio tryCreateGearRatio(char[][] charsMatrix, int i, int j) {
		var adjacentGearRatioNumbers = new ArrayList<GearRatioNumber>();
		for (Offset offset : offsets) {
			var iWithOffset = i + offset.i;
			var jWithOffset = j + offset.j;

			if (iWithOffset >= 0 && iWithOffset < charsMatrix.length
				&& jWithOffset >= 0 && jWithOffset < charsMatrix[iWithOffset].length) {
				if (!isDigit(charsMatrix[iWithOffset][jWithOffset])) {
					continue;
				}

				var gearRatioNumber = resolveGearRatioNumberFromSymbol(charsMatrix, iWithOffset, jWithOffset);
				adjacentGearRatioNumbers.add(gearRatioNumber);
			}
		}

		var uniqueGearRatioNumbers = adjacentGearRatioNumbers.stream().distinct().toList();

		if (uniqueGearRatioNumbers.size() != 2) {
			return null;
		}

		return new GearRatio(i, j, adjacentGearRatioNumbers.get(0).value, adjacentGearRatioNumbers.get(1).value);
	}

	private static class GearRatioNumberBuilder {
		private final int i;
		private final char centerChar;
		private int minJ;
		private int maxJ;
		private final List<Character> charsLeft = new ArrayList<>();
		private final List<Character> charsRight = new ArrayList<>();

		public GearRatioNumberBuilder(char centerChar, int i, int j) {
			this.i = i;
			this.minJ = j;
			this.maxJ = j;
			this.centerChar = centerChar;
		}

		public void addCharLeft(char c, int j) {
			charsLeft.add(c);
			minJ = Math.min(minJ, j);
			maxJ = Math.max(maxJ, j);
		}

		public void addCharRight(char c, int j) {
			charsRight.add(c);
			minJ = Math.min(minJ, j);
			maxJ = Math.max(maxJ, j);
		}

		public GearRatioNumber build() {
			var allChars = new ArrayList<Character>(charsLeft);
			allChars.add(centerChar);
			allChars.addAll(charsRight);

			var numberAsString = allChars.stream().map(String::valueOf).collect(Collectors.joining());
			return new GearRatioNumber(Integer.parseInt(numberAsString), i, minJ, maxJ);
		}
	}

	public static GearRatioNumber resolveGearRatioNumberFromSymbol(char[][] charsMatrix, int charI, int charJ) {
		if (!isDigit(charsMatrix[charI][charJ])) {
			throw new IllegalArgumentException("Not a digit");
		}

		var builder = new GearRatioNumberBuilder(charsMatrix[charI][charJ], charI, charJ);

		for (int j = charJ - 1; j > 0; j--) {
			if (isDigit(charsMatrix[charI][charJ])) {
				builder.addCharLeft(charsMatrix[charI][charJ], j);
			} else {
				break;
			}
		}

		for (int j = charJ + 1; j < charsMatrix[charI].length; j++) {
			if (isDigit(charsMatrix[charI][charJ])) {
				builder.addCharRight(charsMatrix[charI][charJ], j);
			} else {
				break;
			}
		}

		return builder.build();
	}
}
