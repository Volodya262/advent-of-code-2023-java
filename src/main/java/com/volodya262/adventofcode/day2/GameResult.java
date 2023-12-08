package com.volodya262.adventofcode.day2;

import java.util.List;

public record GameResult(int gameNumber, List<GameSetResult> gameSetResults) {
	public int getMaximumRedAmount() {
		return gameSetResults.stream().mapToInt(GameSetResult::redAmount).max().orElse(0);
	}

	public int getMaximumGreenAmount() {
		return gameSetResults.stream().mapToInt(GameSetResult::greenAmount).max().orElse(0);
	}

	public int getMaximumBlueAmount() {
		return gameSetResults.stream().mapToInt(GameSetResult::blueAmount).max().orElse(0);
	}

	public boolean isPossible(GameConfiguration gameConfiguration) {
		return getMaximumRedAmount() <= gameConfiguration.redAmount()
				&& getMaximumGreenAmount() <= gameConfiguration.greenAmount()
				&& getMaximumBlueAmount() <= gameConfiguration.blueAmount();
	}
}
