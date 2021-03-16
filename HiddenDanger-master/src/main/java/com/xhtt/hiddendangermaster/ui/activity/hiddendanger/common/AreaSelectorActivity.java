package com.xhtt.hiddendangermaster.ui.activity.hiddendanger.common;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.hg.zero.adapter.viewpager.ZFragmentViewPager2Adapter;
import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.config.ZCommonResource;
import com.hg.zero.constant.ZParamKey;
import com.hg.zero.toast.Zt;
import com.hg.zero.widget.magicindicator.ZMagicIndicator;
import com.hg.zero.widget.magicindicator.ZViewPagerHelper;
import com.hg.zero.widget.magicindicator.buildins.commonnavigator.ZCommonNavigator;
import com.hg.zero.widget.magicindicator.simple.ZSimpleMagicIndicatorAdapter;
import com.hg.zero.widget.magicindicator.simple.ZSimpleMagicIndicatorData;
import com.hg.zero.widget.magicindicator.simple.ZSimpleMagicIndicatorMode;
import com.hg.zero.widget.magicindicator.simple.ZTabBarHelper;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.common.CommonChooseItem;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.common.contract.AreaSelectorContract;
import com.xhtt.hiddendangermaster.ui.activity.hiddendanger.common.presenter.AreaSelectorPresenter;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPActivity;
import com.xhtt.hiddendangermaster.ui.fragment.hiddendanger.common.AreaFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 地址选择界面
 * <p>
 * Created by Hollow Goods on 2020-04-08
 */

public class AreaSelectorActivity extends HDBaseMVPActivity<AreaSelectorPresenter> implements AreaSelectorContract.View, ZTabBarHelper {

    // 是否为固定项TabBar
    private final boolean isFixed = true;

    private ZMagicIndicator magicIndicator;
    private ViewPager2 viewPager2;

    private ZCommonNavigator commonNavigator;
    private ArrayList<ZSimpleMagicIndicatorData> magicIndicatorData = new ArrayList<>();
    private ZFragmentViewPager2Adapter viewPager2Adapter;
    private ArrayList<Fragment> fragmentData = new ArrayList<>();
    private AreaFragment province;
    private AreaFragment city;
    private AreaFragment district;
    private AreaFragment town;

    @Override
    public int bindLayout() {
        return R.layout.activity_area_selector;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(ZCommonResource.getBackIcon(), "行政区域选择");
        baseUI.setCommonRightTitleText(R.string.sure);

        magicIndicator = findViewById(R.id.magicIndicator);
        viewPager2 = findViewById(R.id.viewPager2);

        refreshTabBarItemData();

        viewPager2Adapter = new ZFragmentViewPager2Adapter(this, fragmentData);
        viewPager2.setAdapter(viewPager2Adapter);

        ZSimpleMagicIndicatorAdapter magicIndicatorAdapter = new ZSimpleMagicIndicatorAdapter(viewPager2, magicIndicatorData, ZSimpleMagicIndicatorMode.Line_Fixed_Width_And_Scale);
        commonNavigator = new ZCommonNavigator(baseUI.getBaseContext());
        if (isFixed) {
            commonNavigator.setAdjustMode(true);
        } else {
            commonNavigator.setScrollPivotX(0.5F);
        }
        commonNavigator.setAdapter(magicIndicatorAdapter);
        magicIndicator.setNavigator(commonNavigator);

        ZViewPagerHelper.bind(magicIndicator, viewPager2);
    }

    @Override
    public void setListener() {

        province.setOnCommonCheckedListener(checkedItem -> {

            magicIndicatorData.get(0).setTitle(checkedItem.getItem());
            magicIndicatorData.get(1).setTitle(getString(R.string.city));
            magicIndicatorData.get(2).setTitle(getString(R.string.district));
            magicIndicatorData.get(3).setTitle(getString(R.string.town));
            refreshTabBar();

            city.getData(checkedItem.getId());
            district.clearData();
            town.clearData();

            viewPager2.setCurrentItem(1);
        });

        city.setOnCommonCheckedListener(checkedItem -> {

            magicIndicatorData.get(1).setTitle(checkedItem.getItem());
            magicIndicatorData.get(2).setTitle(getString(R.string.district));
            magicIndicatorData.get(3).setTitle(getString(R.string.town));
            refreshTabBar();

            district.getData(checkedItem.getId());
            town.clearData();

            viewPager2.setCurrentItem(2);
        });

        district.setOnCommonCheckedListener(checkedItem -> {

            magicIndicatorData.get(2).setTitle(checkedItem.getItem());
            magicIndicatorData.get(3).setTitle(getString(R.string.town));
            refreshTabBar();

            town.getData(checkedItem.getId());

            viewPager2.setCurrentItem(3);
        });

        town.setOnCommonCheckedListener(checkedItem -> {
            magicIndicatorData.get(3).setTitle(checkedItem.getItem());
            refreshTabBar();
        });
    }

    @Override
    public void initViewDelay() {
        province.getData(0);
    }

    @Override
    public AreaSelectorPresenter createPresenter() {
        return new AreaSelectorPresenter(this);
    }

    @Override
    public void onRightTitleClick(View view, int id) {

        CommonChooseItem provinceItem;
        CommonChooseItem cityItem;
        CommonChooseItem districtItem;
        CommonChooseItem townItem;

        if ((provinceItem = province.getCheckedItem()) == null) {
            Zt.error(R.string.please_choose_province);
            return;
        }

        if ((cityItem = city.getCheckedItem()) == null) {
            Zt.error(R.string.please_choose_city);
            return;
        }

        if ((districtItem = district.getCheckedItem()) == null) {
            Zt.error(R.string.please_choose_district);
            return;
        }

        if ((townItem = town.getCheckedItem()) == null) {
            Zt.error(R.string.please_choose_town);
            return;
        }

        ZEvent event = new ZEvent(EventActionCode.CHECKED_AREA);
        event.addObj(ZParamKey.RequestCode, baseUI.requestCode);
        event.addObj(ParamKey.Province, provinceItem);
        event.addObj(ParamKey.City, cityItem);
        event.addObj(ParamKey.District, districtItem);
        event.addObj(ParamKey.Town, townItem);
        EventBus.getDefault().post(event);

        finishMyActivity();
    }

    @Override
    public void refreshTabBarItemData() {

        magicIndicatorData.clear();
        fragmentData.clear();

        magicIndicatorData.add(new ZSimpleMagicIndicatorData().setTitle(getString(R.string.province)));
        fragmentData.add(province = new AreaFragment());

        magicIndicatorData.add(new ZSimpleMagicIndicatorData().setTitle(getString(R.string.city)));
        fragmentData.add(city = new AreaFragment());

        magicIndicatorData.add(new ZSimpleMagicIndicatorData().setTitle(getString(R.string.district)));
        fragmentData.add(district = new AreaFragment());

        magicIndicatorData.add(new ZSimpleMagicIndicatorData().setTitle(getString(R.string.town)));
        fragmentData.add(town = new AreaFragment());
    }

    @Override
    public void refreshTabBar() {
        // 必须先调用该方法
        commonNavigator.notifyDataSetChanged();
        viewPager2Adapter.notifyDataSetChanged();
    }

}
