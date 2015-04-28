package fr.cmoatoto.quiz.lib.activities.gamerules;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;

import fr.cmoatoto.quiz.lib.elements.GameRulesList.GameRules;

public class GameRulesPagerAdapter extends FragmentPagerAdapter {

    private List<GameRules> mGameRules = new ArrayList<>();
    private SparseArray<GameRulesFragment> mFragmentMap = new SparseArray<>();

    private JazzyViewPager mViewPager;

    public GameRulesPagerAdapter(JazzyViewPager viewPager, FragmentManager fm) {
        super(fm);
        mViewPager = viewPager;
    }

    public void setGameRules(List<GameRules> gameRules) {
        mGameRules = gameRules;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragmentMap.get(position) != null) {
            return mFragmentMap.get(position);
        }
        GameRulesFragment f = new GameRulesFragment();
        Bundle b = new Bundle();
        b.putSerializable(GameRulesFragment.KEY_GAMERULES, mGameRules.get(position));
        b.putInt(GameRulesFragment.KEY_INDEX, position);
        b.putInt(GameRulesFragment.KEY_ELEMENTS, mGameRules.size());
        f.setArguments(b);
        mFragmentMap.put(position, f);
        return f;
    }

    @Override
    public int getCount() {
        return mGameRules.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Object obj = super.instantiateItem(container, position);
        mViewPager.setObjectForPosition(obj, position);
        return obj;
    }

    public GameRules getGameRules(int position) {
        return mGameRules.get(position);
    }

}
