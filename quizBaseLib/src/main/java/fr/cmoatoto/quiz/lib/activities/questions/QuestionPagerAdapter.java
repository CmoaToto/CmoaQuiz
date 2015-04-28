package fr.cmoatoto.quiz.lib.activities.questions;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;

import fr.cmoatoto.quiz.lib.elements.QuestionList;
import fr.cmoatoto.quiz.lib.elements.QuestionList.Question;

public class QuestionPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    private List<Question> mQuestions;
    private SparseArray<QuestionFragment> mFragmentMap = new SparseArray<>();
    private boolean mInfiniteQuestionList = false;
    private int mPlayer = 0;

    private JazzyViewPager mViewPager;

    public QuestionPagerAdapter(Context c, JazzyViewPager vp, FragmentManager fm, List<Question> items) {
        super(fm);
        this.mContext = c;
        this.mViewPager = vp;
        this.mQuestions = items;
    }

    public QuestionPagerAdapter(Context c, JazzyViewPager vp, FragmentManager fm, List<Question> items, boolean infiniteQuestionList) {
        this(c, vp, fm, items);
        this.mInfiniteQuestionList = infiniteQuestionList;
    }

    public QuestionPagerAdapter(Context c, JazzyViewPager vp, FragmentManager fm, List<Question> items, boolean infiniteQuestionList, int player) {
        this(c, vp, fm, items);
        this.mInfiniteQuestionList = infiniteQuestionList;
        this.mPlayer = player;
    }

    @Override
    public Fragment getItem(int position) {
        if (mQuestions.size() <= position) {
            mQuestions.addAll(QuestionList.getInstance(mContext).prepareQuestions().getNextMarathon());
        }
        if (mFragmentMap.indexOfKey(position) >= 0) {
            return mFragmentMap.get(position);
        }
        QuestionFragment f = new QuestionFragment();
        Bundle b = new Bundle();
        b.putSerializable(QuestionFragment.KEY_QUESTION, mQuestions.get(position));
        b.putInt(QuestionFragment.KEY_PLAYER, mPlayer);
        f.setArguments(b);
        mFragmentMap.put(position, f);
        return f;
    }

    @Override
    public int getCount() {
        if (mInfiniteQuestionList) {
            return Integer.MAX_VALUE;
        } else {
            return mQuestions.size();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Object obj = super.instantiateItem(container, position);
        mViewPager.setObjectForPosition(obj, position);
        return obj;
    }

}
