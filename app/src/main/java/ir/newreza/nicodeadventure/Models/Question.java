package ir.newreza.nicodeadventure.Models;

public class Question {
    String questionText;
    int index, yesReferIndex, noReferIndex;

    public Question () {
        questionText = "";
        yesReferIndex = -1;
        noReferIndex = -1;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setYesReferIndex(int yesReferIndex) {
        this.yesReferIndex = yesReferIndex;
    }

    public void setNoReferIndex(int noReferIndex) {
        this.noReferIndex = noReferIndex;
    }

    public int getIndex() {
        return index;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getYesReferIndex() {
        return yesReferIndex;
    }

    public int getNoReferIndex() {
        return noReferIndex;
    }

}
