package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.msds;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.ui.base.BaseActivity;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.msds.MSDSFragment;

/**
 * MSDS界面
 *
 * @author HG
 */

public class MSDSActivity extends BaseActivity {

    private final int SEARCH_TYPE_FUZZY_QUERY = 0;
    private final int SEARCH_TYPE_PRECISE_QUERY = 1;

    private ImageView searchType;
    private MSDSFragment msdsFragment;

    public int searchTypeFilter = SEARCH_TYPE_FUZZY_QUERY;

    @Override
    public int bindLayout() {
        return R.layout.activity_msds;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_msds);
        Toolbar.LayoutParams tlp = new Toolbar.LayoutParams(Gravity.END | Gravity.CENTER_VERTICAL);
        baseUI.addCommonTitleOtherView(
                searchType = (ImageView) View.inflate(baseUI.getBaseContext(), R.layout.item_msds_right_menu, null),
                tlp
        );
        searchType.setImageResource(R.drawable.ic_mo_hu);

        String searchKey = baseUI.getParam(ParamKey.StringData, "");

        msdsFragment = new MSDSFragment();
        if (!TextUtils.isEmpty(searchKey)) {
            msdsFragment.setFragmentArguments(
                    "1",
                    new Enum[]{ParamKey.StringData},
                    new Object[]{searchKey}
            );
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_msds, msdsFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void setListener() {

        searchType.setOnClickListener(v -> {
            if (searchTypeFilter == SEARCH_TYPE_FUZZY_QUERY) {
                searchTypeFilter = SEARCH_TYPE_PRECISE_QUERY;
                searchType.setImageResource(R.drawable.ic_jing_que);
            } else {
                searchTypeFilter = SEARCH_TYPE_FUZZY_QUERY;
                searchType.setImageResource(R.drawable.ic_mo_hu);
            }

            msdsFragment.doSearch();
        });
    }

}
