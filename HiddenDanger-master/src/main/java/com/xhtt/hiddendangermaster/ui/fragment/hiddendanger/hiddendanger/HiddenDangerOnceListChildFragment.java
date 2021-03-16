package com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.hiddendanger;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.Company;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDanger;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.ui.base.HDBaseFragment;

/**
 * Created by Hollow Goods on 2019-04-09.
 */
public class HiddenDangerOnceListChildFragment extends HDBaseFragment {

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

        super.initParamData();
        parentData = baseUI.getParam(ParamKey.ParentData, null);

        if (parentData == null) {
            parentData = new Company();
        }
    }

    @Override
    public void initView(View view, Bundle bundle) {

        baseUI.setCommonTitleViewVisibility(false);

        status = baseUI.findViewById(R.id.rg_status);
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
                            new Enum[]{ParamKey.Status, ParamKey.ParentData},
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
                            new Enum[]{ParamKey.Status, ParamKey.ParentData},
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
