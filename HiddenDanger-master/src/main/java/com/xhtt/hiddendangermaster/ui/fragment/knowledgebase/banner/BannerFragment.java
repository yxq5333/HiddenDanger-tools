package com.xhtt.hiddendangermaster.ui.fragment.knowledgebase.banner;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.ui.base.click.OnRecyclerViewItemClickOldListener;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPFragment;
import com.hg.hollowgoods.util.SystemAppUtils;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.adapter.knowledgebase.BannerAdapter;
import com.xhtt.hiddendangermaster.bean.knowledgebase.banner.Banner;
import com.xhtt.hiddendangermaster.bean.knowledgebase.banner.BannerAppUrl;
import com.xhtt.hiddendangermaster.constant.ParamKey;
import com.xhtt.hiddendangermaster.ui.activity.knowledgebase.main.KnowledgeBaseActivity;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 轮播图界面
 *
 * @author HG
 */

public class BannerFragment extends BaseMVPFragment<BannerPresenter> implements BannerContract.View {

    public static final int BANNER_TYPE_KNOWLEDGE_BASE = 1;
    public static final int BANNER_TYPE_HIDDEN_DANGER = 2;

    private DiscreteScrollView banner;
    private RadioGroup label;

    private InfiniteScrollAdapter infiniteScrollAdapter;
    private BannerAdapter adapter;
    private ArrayList<Banner> data = new ArrayList<>();
    private int[] banners;
    private int bannerPosition = 0;
    private Timer bannerTimer;
    private int location;
    private int bannerType;

    @Override
    public int bindLayout() {
        return R.layout.fragment_banner;
    }

    @Override
    public void initParamData() {

        location = baseUI.getParam(ParamKey.Location, Banner.LOCATION_MAIN_ACTIVITY);
        bannerType = baseUI.getParam(ParamKey.BannerType, BannerFragment.BANNER_TYPE_KNOWLEDGE_BASE);

        if (bannerType != 0) {
            if (bannerType == BANNER_TYPE_KNOWLEDGE_BASE) {
                banners = new int[2];
                banners[0] = R.mipmap.banner03;
                banners[1] = R.mipmap.banner04;
            } else if (bannerType == BANNER_TYPE_HIDDEN_DANGER) {
                banners = new int[2];
                banners[0] = R.mipmap.banner01;
                banners[1] = R.mipmap.banner02;
            }
        }
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleViewVisibility(false);

        banner = baseUI.findViewById(R.id.dsv_banner);
        label = baseUI.findViewById(R.id.rg_label);

        initBanner(null);
        doRefresh();
    }

    @Override
    public void setListener() {

    }

    @Override
    public BannerPresenter createPresenter() {
        return new BannerPresenter();
    }

    private void initBanner(ArrayList<Banner> webBanners) {

        // 方向
        banner.setOrientation(DSVOrientation.HORIZONTAL);
        // 滚动监听
        banner.addOnItemChangedListener((viewHolder, adapterPosition) -> {
            int position = infiniteScrollAdapter.getRealPosition(adapterPosition);
            onItemChanged(position);
        });

        Banner item;
        RadioButton radioButton;
        RadioGroup.LayoutParams rlp = new RadioGroup.LayoutParams(25, 25);
        rlp.setMarginStart(8);
        rlp.setMarginEnd(8);

        data.clear();
        label.removeAllViews();

        if (webBanners == null) {
            for (int t : banners) {
                item = new Banner();
                item.setRes(t);
                data.add(item);

                radioButton = new RadioButton(baseUI.getBaseContext());
                radioButton.setButtonDrawable(getResources().getDrawable(android.R.color.transparent));
                radioButton.setLayoutParams(rlp);
                radioButton.setBackgroundResource(R.drawable.selector_banner_label);
                label.addView(radioButton);
            }
        } else {
            for (Banner t : webBanners) {
                data.add(t);

                radioButton = new RadioButton(baseUI.getBaseContext());
                radioButton.setButtonDrawable(getResources().getDrawable(android.R.color.transparent));
                radioButton.setLayoutParams(rlp);
                radioButton.setBackgroundResource(R.drawable.selector_banner_label);
                label.addView(radioButton);
            }
        }

        // 创建普通适配器
        adapter = new BannerAdapter(baseUI.getBaseContext(), R.layout.item_banner, data);
        // 生成无限滚动适配器
        infiniteScrollAdapter = InfiniteScrollAdapter.wrap(adapter);
        // 把无限滚动适配器放入普通适配器，为了获取真实的Position
        adapter.setInfiniteAdapter(infiniteScrollAdapter);
        // 填充适配器
        banner.setAdapter(infiniteScrollAdapter);

        // 设置滚动动画时间
        banner.setItemTransitionTimeMillis(200);
        banner.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(1f)
                .build());

        onItemChanged(bannerPosition);
        startBanner();

        adapter.setOnBannerClickListener(new OnRecyclerViewItemClickOldListener(false) {
            @Override
            public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {

                if (data.get(position).getLinkType() == Banner.TYPE_URL) {
                    if (!TextUtils.isEmpty(data.get(position).getHttpUrl())) {
                        String url = data.get(position).getHttpUrl();
                        if (!url.toLowerCase().startsWith("http://")
                                && !url.toLowerCase().startsWith("https://")
                        ) {
                            url = "http://" + url;
                        }

                        new SystemAppUtils().openExplorer(baseUI.getBaseContext(), url);
                    }
                } else if (data.get(position).getLinkType() == Banner.TYPE_APP) {
                    if (TextUtils.equals(data.get(position).getAppUrl(), BannerAppUrl.KnowledgeBase.getUrl())) {
                        baseUI.startMyActivity(KnowledgeBaseActivity.class);
                    }
                }
            }
        });
    }

    private void onItemChanged(int position) {
        bannerPosition = position;
        ((RadioButton) label.getChildAt(bannerPosition)).setChecked(true);
    }

    private void startBanner() {

        if (bannerTimer != null) {
            bannerTimer.cancel();
            bannerTimer = null;
        }

        bannerTimer = new Timer();
        bannerTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                bannerPosition++;

                if (bannerPosition >= data.size()) {
                    bannerPosition = 0;
                }

                baseUI.getBaseContext().runOnUiThread(() -> {
                    int position = infiniteScrollAdapter.getClosestPosition(bannerPosition);
                    banner.smoothScrollToPosition(position);
                });
            }
        }, 3000, 3000);
    }

    @Override
    public void onDestroy() {

        if (bannerTimer != null) {
            bannerTimer.cancel();
            bannerTimer = null;
        }

        super.onDestroy();
    }

    private void doRefresh() {
        getData();
    }

    private void getData() {
        mPresenter.getData(location);
    }

    @Override
    public void getDataSuccess(ArrayList<Banner> tempData) {
        baseUI.getBaseContext().runOnUiThread(() -> {
            if (tempData != null && tempData.size() > 0) {
                bannerPosition = 0;
                initBanner(tempData);
            }
        });
    }

    @Override
    public void getDataError() {

    }

    @Override
    public void getDataFinish() {

    }
}
