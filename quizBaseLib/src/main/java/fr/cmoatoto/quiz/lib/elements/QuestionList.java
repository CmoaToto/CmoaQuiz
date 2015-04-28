package fr.cmoatoto.quiz.lib.elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;
import fr.cmoatoto.quiz.lib.R;
import fr.cmoatoto.quiz.lib.utils.SharedPrefUtils;

/**
 * Loads an XML description of a list of Question and stores their attributes.
 * <p>
 * The QuestionList contains XML that looks like the following snippet:
 * </p>
 * 
 * <pre>
 * &lt;QuestionList&gt;
 *     &lt;Question
 *         questionId="1"
 *         question="What is the color of the Henri IV white horse ?"
 *         answer1="Blue"
 *         answer2="White"
 *         answer3="Brown"
 *         explanation="The answer is in the question..."
 *         submitter="CmoaToto"
 *         translated="true"
 *         answer=2 /&gt;
 *     ...
 * &lt;/QuestionList&gt;
 * </pre>
 */
public class QuestionList {

    private static QuestionList _questionListInstance;

    public static QuestionList getInstance(Context c) {
        if (_questionListInstance == null) {
            _questionListInstance = new QuestionList(c, R.xml.questions);
        }
        return _questionListInstance;
    }

    private static final String TAG = QuestionList.class.getName();

    private static final String TAG_QUESTION_LIST = "QuestionList";
    private static final String TAG_QUESTION = "Question";

    private static final String TAG_QUESTION_ID = "questionId";
    private static final String TAG_QUESTION_QUESTION = "question";
    private static final String TAG_QUESTION_ANSWER1 = "answer1";
    private static final String TAG_QUESTION_ANSWER2 = "answer2";
    private static final String TAG_QUESTION_ANSWER3 = "answer3";
    private static final String TAG_QUESTION_EXPLANATION = "explanation";
    private static final String TAG_QUESTION_SUBMITTER = "submitter";
    private static final String TAG_QUESTION_ANSWER = "answer";
    private static final String TAG_QUESTION_TRANSLATED = "translated";

    private Context mContext;
    private int mParserId;

    private List<Question> mAllQuestions = new ArrayList<>();
    private List<Question> mAllTranslatedQuestions = new ArrayList<>();
    private List<Question> mNextMarathon = new ArrayList<>();
    private List<Question> mTranslatedNextMarathon = new ArrayList<>();

    /**
     * Container for {@link Question}s in the {@link QuestionList}.
     */
    public static class Question implements Serializable {

        private static final long serialVersionUID = -7716914118718302604L;
        private int id;
        private String question;
        private String answer1;
        private String answer2;
        private String answer3;
        private String submitter;
        private int answer;
        private String explanation;
        private boolean translated;

        public Question() {
        }

        public Question(Resources res, XmlResourceParser parser) {

            id = parser.getAttributeIntValue(null, TAG_QUESTION_ID, -1);
            translated = getBooleanValue(res, parser, TAG_QUESTION_TRANSLATED);
            question = getStringValue(res, parser, TAG_QUESTION_QUESTION);
            answer1 = getStringValue(res, parser, TAG_QUESTION_ANSWER1);
            answer2 = getStringValue(res, parser, TAG_QUESTION_ANSWER2);
            answer3 = getStringValue(res, parser, TAG_QUESTION_ANSWER3);
            submitter = getStringValue(res, parser, TAG_QUESTION_SUBMITTER);
            explanation = getStringValue(res, parser, TAG_QUESTION_EXPLANATION);
            if ("".equals(explanation)) {
                explanation = null;
            }
            answer = parser.getAttributeIntValue(null, TAG_QUESTION_ANSWER, -1);
        }

        protected void setQuestion(Question question) {
            this.id = question.id;
            this.translated = question.translated;
            this.question = question.question;
            this.answer1 = question.answer1;
            this.answer2 = question.answer2;
            this.answer3 = question.answer3;
            this.submitter = question.submitter;
            this.explanation = question.explanation;
            this.answer = question.answer;
        }

        private String getStringValue(Resources res, XmlResourceParser parser, String attribute) {
            int id = parser.getAttributeResourceValue(null, attribute, -1);
            return id != -1 ? res.getString(id) : parser.getAttributeValue(null, attribute);
        }

        private boolean getBooleanValue(Resources res, XmlResourceParser parser, String attribute) {
            return Boolean.parseBoolean(getStringValue(res, parser, attribute));
        }

        public boolean isUsable() {
            return id != -1 && question != null && answer1 != null && answer2 != null && answer3 != null && submitter != null && answer != -1;
        }

        public boolean isTranslated() {
            return translated;
        }

        public int getId() {
            return id;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer1() {
            return answer1;
        }

        public String getAnswer2() {
            return answer2;
        }

        public String getAnswer3() {
            return answer3;
        }

        public String getSubmitter() {
            return submitter;
        }

        public boolean isRightAnswer(int answer) {
            return this.answer == answer;
        }

        public String getExplanation() {
            return explanation;
        }

        public String getAnswerfromIndex(int i) {
            switch (i) {
            case 1:
                return getAnswer1();
            case 2:
                return getAnswer2();
            case 3:
                return getAnswer3();
            default:
                return null;
            }
        }

        public String getAnswer() {
            switch (answer) {
            case 1:
                return getAnswer1();
            case 2:
                return getAnswer2();
            case 3:
                return getAnswer3();
            default:
                return null;
            }
        }

    }

    private QuestionList(Context c, int parserId) {
        this.mContext = c;
        this.mParserId = parserId;
    }

    public void initQuestionLists() {
        Resources res = mContext.getResources();
        XmlResourceParser parser = res.getXml(mParserId);
        Question question;
        mAllQuestions.clear();
        mAllTranslatedQuestions.clear();

        try {
            int event;
            while ((event = parser.next()) != XmlResourceParser.END_DOCUMENT) {
                if (event == XmlResourceParser.START_TAG) {
                    String tag = parser.getName();
                    if (TAG_QUESTION_LIST.equals(tag)) {
                        parseQuestionListAttributes(res, parser);
                    } else if (TAG_QUESTION.equals(tag)) {
                        question = createQuestionFromXml(res, parser);
                        if (question.isUsable()) {
                            mAllQuestions.add(question);
                            if (question.isTranslated()) {
                                mAllTranslatedQuestions.add(question);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Parse error:" + e);
            e.printStackTrace();
        }
    }

    /**
     * Creates a (Array)List<Question> from the given xml key layout file. Weeds out questions not usable and already answered.
     *
     * @return
     */
    public QuestionList prepareQuestions() {
        mNextMarathon = randomizeQuestions(removeAnsweredQuestions(mAllQuestions));
        mTranslatedNextMarathon = randomizeQuestions(removeAnsweredQuestions(mAllTranslatedQuestions));
        return this;
    }

    public List<Question> getNext10Questions() {
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 10 && i < getNextMarathon().size(); i++) {
            questions.add(getNextMarathon().get(i));
        }
        return questions;
    }

    public List<Question> getNextMarathon() {
        return SharedPrefUtils.getAcceptNotTranslatedQuestion(mContext) ? mNextMarathon : mTranslatedNextMarathon;
    }

    public List<Question> getAllQuestions() {
        return mAllQuestions;
    }

    private void parseQuestionListAttributes(Resources res, XmlResourceParser parser) {
    }

    private Question createQuestionFromXml(Resources res, XmlResourceParser parser) {
        return new Question(res, parser);
    }

    private List<Question> removeAnsweredQuestions(List<Question> baseQuestionList) {
        List<Question> notAnsweredQuestions = new ArrayList<>();
        List<Integer> answeredQuestionIdList = SharedPrefUtils.getAnsweredQuestionIdList(mContext);
        int answeredQuestionsCount = 0;
        for (Question question : baseQuestionList) {
            if (!answeredQuestionIdList.contains(question.getId())) {
                notAnsweredQuestions.add(question);
            } else {
                answeredQuestionsCount++;
            }
        }

        if (answeredQuestionsCount > 0 && notAnsweredQuestions.size() < 10) {
            SharedPrefUtils.clearAnsweredQuestionIds(mContext);
            return removeAnsweredQuestions(baseQuestionList);
        }

        return notAnsweredQuestions;
    }

    private List<Question> randomizeQuestions(List<Question> questionList) {
        List<Question> baseQuestionList = new ArrayList<>(questionList);
        List<Question> nextQuiz = new ArrayList<>();

        Random rand = new Random();
        while (baseQuestionList.size() > 0) {
            Question q = baseQuestionList.get(rand.nextInt(baseQuestionList.size()));
            nextQuiz.add(q);
            baseQuestionList.remove(q);
        }

        return nextQuiz;
    }

    int getTotalQuestion() {
        return mAllQuestions.size();
    }

    public int getTotalTranslatedQuestion() {
        return mAllTranslatedQuestions.size();
    }

    public int getTotalNotTranslatedQuestion() {
        return getTotalQuestion() - getTotalTranslatedQuestion();
    }
}
