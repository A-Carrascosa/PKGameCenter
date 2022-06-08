package com.example.gamecenter;

public class DataListActivityScore {

    private String userText;
    private String scoreText;
    private String gameText;

    public DataListActivityScore() {
    }

    public DataListActivityScore(String userText, String scoreText, String gameText) {
        this.userText = userText;
        this.scoreText = scoreText;
        this.gameText = gameText;
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }

    public String getScoreText() {
        return scoreText;
    }

    public void setScoreText(String scoreText) {
        this.scoreText = scoreText;
    }

    public String getGameText() {
        return gameText;
    }

    public void setGameText(String gameText) {
        this.gameText = gameText;
    }
}
