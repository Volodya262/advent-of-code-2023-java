package com.volodya262.adventofcode.day2;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) throws IOException {
		var gameConfiguration = new GameConfiguration(12, 13, 14);

		try (var resource = Main.class.getResourceAsStream("/input.txt")) {
			assert resource != null;
			var resourceAsString = new String(resource.readAllBytes());

			var lines = Arrays.stream(resourceAsString.split(System.lineSeparator()))
					.filter(line -> !line.isBlank())
					.toList();

			var gameResults = parseGameResults(lines);

			var idsSum = calculateDay1SumOfIds(gameResults, gameConfiguration);
			var powersSum = calculateDay2PowersSum(gameResults);

			System.out.println(idsSum);
			System.out.println(powersSum);
		}
	}

	private static int calculateDay1SumOfIds(List<GameResult> gameResults, GameConfiguration gameConfiguration) {
		var possibleGameResults = gameResults.stream()
				.filter(gameResult -> gameResult.isPossible(gameConfiguration))
				.toList();

		return possibleGameResults.stream()
				.mapToInt(GameResult::gameNumber)
				.sum();
	}

	private static int calculateDay2PowersSum(List<GameResult> gameResults) {
		return gameResults.stream()
				.map(gameResult -> new GameConfiguration(
						gameResult.getMaximumRedAmount(),
						gameResult.getMaximumGreenAmount(),
						gameResult.getMaximumBlueAmount()
				))
				.mapToInt(gameResult -> gameResult.blueAmount() * gameResult.greenAmount() * gameResult.redAmount())
				.sum();
	}

	private static List<GameResult> parseGameResults(List<String> lines) {
		return lines.stream().map(Main::parseGameResult).toList();
	}

	// "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green" -> GameResult
	private static final Pattern gameResultNumberBodyPattern = Pattern.compile("Game (?<number>\\d+): (?<body>.+)");

	private static GameResult parseGameResult(String line) {
		var matcher = gameResultNumberBodyPattern.matcher(line);
		var isMatches = matcher.matches();
		assert isMatches;

		var gameNumber = Integer.parseInt(matcher.group("number"));
		var gameBody = matcher.group("body");

		var gameSetResults = Arrays.stream(gameBody.split(";"))
				.map(String::strip)
				.map(Main::parseGameSetResult)
				.toList();

		return new GameResult(gameNumber, gameSetResults);
	}

	private static final Pattern gameSetResultRedPattern = Pattern.compile("(?<red>\\d+) red");
	private static final Pattern gameSetResultBluePattern = Pattern.compile("(?<blue>\\d+) blue");
	private static final Pattern gameSetResultGreenPattern = Pattern.compile("(?<green>\\d+) green");

	private static GameSetResult parseGameSetResult(String gameSetResultLine) {
		var redMatcher = gameSetResultRedPattern.matcher(gameSetResultLine);
		var red = redMatcher.find() ? Integer.parseInt(redMatcher.group("red")) : 0;

		var blueMatcher = gameSetResultBluePattern.matcher(gameSetResultLine);
		var blue = blueMatcher.find() ? Integer.parseInt(blueMatcher.group("blue")) : 0;

		var greenMatcher = gameSetResultGreenPattern.matcher(gameSetResultLine);
		var green = greenMatcher.find() ? Integer.parseInt(greenMatcher.group("green")) : 0;

		return new GameSetResult(red, green, blue);
	}
}