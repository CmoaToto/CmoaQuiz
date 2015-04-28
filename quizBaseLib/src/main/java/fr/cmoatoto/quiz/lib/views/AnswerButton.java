package fr.cmoatoto.quiz.lib.views;

import android.content.Context;
import android.util.AttributeSet;

public class AnswerButton extends QuizButton {

    private int index = -1;
    private boolean isSelectedAnswer = false;

    public AnswerButton(Context context) {
        super(context);
    }

    public AnswerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnswerButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSelectedAnswer() {
        return isSelectedAnswer;
    }

    public void setSelectedAnswer(boolean isSelectedAnswer) {
        this.isSelectedAnswer = isSelectedAnswer;
    }

}
