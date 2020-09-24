package com.xhtt.hiddendanger.UI.Activity.Common;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.hg.hollowgoods.Adapter.ViewPagerAdapter.FragmentViewPager2Adapter;
import com.hg.hollowgoods.Bean.EventBus.Event;
import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.Constant.HGParamKey;
import com.hg.hollowgoods.UI.Base.BaseMVPActivity;
import com.hg.hollowgoods.UI.Base.Message.Toast.t;
import com.hg.hollowgoods.Widget.MagicIndicator.MagicIndicator;
import com.hg.hollowgoods.Widget.MagicIndicator.ViewPagerHelper;
import com.hg.hollowgoods.Widget.MagicIndicator.buildins.commonnavigator.CommonNavigator;
import com.hg.hollowgoods.Widget.TabBar.SimpleMagicIndicatorAdapter;
import com.hg.hollowgoods.Widget.TabBar.SimpleMagicIndicatorData;
import com.hg.hollowgoods.Widget.TabBar.SimpleMagicIndicatorMode;
import com.hg.hollowgoods.Widget.TabBar.TabBarHelper;
import com.xhtt.hiddendanger.Bean.Common.CommonChooseItem;
import com.xhtt.hiddendanger.Constant.EventActionCode;
import com.xhtt.hiddendanger.Constant.ParamKey;
import com.xhtt.hiddendanger.R;
import com.xhtt.hiddendanger.UI.Activity.Common.Contract.AreaSelectorContract;
import com.xhtt.hiddendanger.UI.Activity.Common.Presenter.AreaSelectorPresenter;
import com.xhtt.hiddendanger.UI.Fragment.Common.AreaFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 地址选择界面
 * <p>
 * Created by Hollow Goods on 2020-04-08
 */

public class AreaSelectorActivity extends BaseMVPActivity<AreaSelectorPresenter> implements AreaSelectorContract.View, TabBarHelper {

    // 是否为固定项TabBar
    private final boolean isFixed = true;

    private MagicIndicator magicIndicator;
    private ViewPager2 viewPager2;

    private CommonNavigator commonNavigator;
    private ArrayList<SimpleMagicIndicatorData> magicIndicatorData = new ArrayList<>();
    private FragmentViewPager2Adapter viewPager2Adapter;
    private ArrayList<Fragment> fragmentData = new ArrayList<>();
    private AreaFragment province;
    private AreaFragment city;
    private AreaFragment district;
    private AreaFragment town;

    @Override
    public Activity addToExitGroup() {
        return this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_area_selector;
    }

    @Nullable
    @Override
    public Object initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, "行政区域选择");
        baseUI.setCommonRightTitleText(R.string.sure);

        magicIndicator = findViewById(R.id.magicIndicator);
        viewPager2 = findViewById(R.id.viewPager2);

        refreshTabBarItemData();

        viewPager2Adapter = new FragmentViewPager2Adapter(this, fragmentData);
        viewPager2.setAdapter(viewPager2Adapter);

        SimpleMagicIndicatorAdapter magicIndicatorAdapter = new SimpleMagicIndicatorAdapter(viewPager2, magicIndicatorData, SimpleMagicIndicatorMode.Line_Fixed_Width_And_Scale);
        commonNavigator = new CommonNavigator(baseUI.getBaseContext());
        if (isFixed) {
            commonNavigator.setAdjustMode(true);
        } else {
            commonNavigator.setScrollPivotX(0.5F);
        }
        commonNavigator.setAdapter(magicIndicatorAdapter);
        magicIndicator.setNavigator(commonNavigator);

        ViewPagerHelper.bind(magicIndicator, viewPager2);

        return null;
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
            t.error(R.string.please_choose_province);
            return;
        }

        if ((cityItem = city.getCheckedItem()) == null) {
            t.error(R.string.please_choose_city);
            return;
        }

        if ((districtItem = district.getCheckedItem()) == null) {
            t.error(R.string.please_choose_district);
            return;
        }

        if ((townItem = town.getCheckedItem()) == null) {
            t.error(R.string.please_choose_town);
            return;
        }

        Event event = new Event(EventActionCode.CHECKED_AREA);
        event.addObj(HGParamKey.RequestCode.getValue(), baseUI.requestCode);
        event.addObj(ParamKey.Province.getValue(), provinceItem);
        event.addObj(ParamKey.City.getValue(), cityItem);
        event.addObj(ParamKey.District.getValue(), districtItem);
        event.addObj(ParamKey.Town.getValue(), townItem);
        EventBus.getDefault().post(event);

        finishMyActivity();
    }

    @Override
    public void refreshTabBarItemData() {

        magicIndicatorData.clear();
        fragmentData.clear();

        magicIndicatorData.add(new SimpleMagicIndicatorData().setTitle(getString(R.string.province)));
        fragmentData.add(province = new AreaFragment());

        magicIndicatorData.add(new SimpleMagicIndicatorData().setTitle(getString(R.string.city)));
        fragmentData.add(city = new AreaFragment());

        magicIndicatorData.add(new SimpleMagicIndicatorData().setTitle(getString(R.string.district)));
        fragmentData.add(district = new AreaFragment());

        magicIndicatorData.add(new SimpleMagicIndicatorData().setTitle(getString(R.string.town)));
        fragmentData.add(town = new AreaFragment());
    }

    @Override
    public void refreshTabBar() {
        // 必须先调用该方法
        commonNavigator.notifyDataSetChanged();
        viewPager2Adapter.notifyDataSetChanged();
    }

}
