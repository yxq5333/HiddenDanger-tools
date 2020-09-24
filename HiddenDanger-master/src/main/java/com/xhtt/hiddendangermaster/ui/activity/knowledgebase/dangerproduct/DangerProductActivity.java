package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.dangerproduct;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.ui.base.BaseActivity;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.dangerproduct.DangerProductFragment;

/**
 * 危化品安全信息界面
 *
 * @author HG
 */

public class DangerProductActivity extends BaseActivity {

    private final int SEARCH_TYPE_FUZZY_QUERY = 0;
    private final int SEARCH_TYPE_PRECISE_QUERY = 1;

    private ImageView searchType;
    private DangerProductFragment dangerProductFragment;

    public int searchTypeFilter = SEARCH_TYPE_FUZZY_QUERY;

    @Override
    public int bindLayout() {
        return R.layout.activity_danger_product;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_danger_product);
        Toolbar.LayoutParams tlp = new Toolbar.LayoutParams(Gravity.END | Gravity.CENTER_VERTICAL);
        baseUI.addCommonTitleOtherView(
                searchType = (ImageView) View.inflate(baseUI.getBaseContext(), R.layout.item_msds_right_menu, null),
                tlp
        );
        searchType.setImageResource(R.drawable.ic_mo_hu);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_dangerProduct, dangerProductFragment = new DangerProductFragment());
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

            dangerProductFragment.doSearch();
        });
    }

}
