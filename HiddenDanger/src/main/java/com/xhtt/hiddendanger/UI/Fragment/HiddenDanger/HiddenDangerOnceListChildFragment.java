package com.xhtt.hiddendanger.UI.Fragment.HiddenDanger;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hg.hollowgoods.UI.Base.BaseFragment;
import com.xhtt.hiddendanger.Bean.HiddenDanger.Company;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDanger;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.R;

/**
 * Created by Hollow Goods on 2019-04-09.
 */
public class HiddenDangerOnceListChildFragment extends BaseFragment {

    private RadioGroup status;

    private HiddenDangerListFragment fragment1;
    private HiddenDangerListFragment fragment2;
    private int checkedPosition = 0;
    private Company parentData;

    @Override
    public int bindLayout() {
        return R.layout.fragment_hidden_danger_once_list_child;
    }

    @Override
    public void initParamData() {

        parentData = baseUI.getParam(ParamKey.ParentData.getValue(), null);

        if (parentData == null) {
            parentData = new Company();
        }
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle bundle) {

        baseUI.setCommonTitleViewVisibility(false);

        status = baseUI.findViewById(R.id.rg_status);

        return null;
    }

    @Override
    public void setListener() {

        status.setOnCheckedChangeListener((group, checkedId) -> {

            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            hideFragment(ft);

            if (checkedId == R.id.rb_unchanged) {
                if (fragment1 == null) {
                    fragment1 = new HiddenDangerListFragment();
                    fragment1.setFragmentArguments(
                            "1",
                            new String[]{ParamKey.Status.getValue(), ParamKey.ParentData.getValue()},
                            new Object[]{HiddenDanger.STATUS_UNCHANGED, parentData}
                    );
                    ft.add(R.id.fl_content, fragment1);
                } else {
                    ft.show(fragment1);
                }

                checkedPosition = 0;
            } else {
                if (fragment2 == null) {
                    fragment2 = new HiddenDangerListFragment();
                    fragment2.setFragmentArguments(
                            "2",
                            new String[]{ParamKey.Status.getValue(), ParamKey.ParentData.getValue()},
                            new Object[]{HiddenDanger.STATUS_CHANGED, parentData}
                    );
                    ft.add(R.id.fl_content, fragment2);
                } else {
                    ft.show(fragment2);
                }

                checkedPosition = 1;
            }

            ft.commitAllowingStateLoss();
        });

        ((RadioButton) status.getChildAt(0)).setChecked(true);
    }

    private void hideFragment(FragmentTransaction ft) {

        if (fragment1 != null) {
            ft.hide(fragment1);
        }

        if (fragment2 != null) {
            ft.hide(fragment2);
        }
    }

}
