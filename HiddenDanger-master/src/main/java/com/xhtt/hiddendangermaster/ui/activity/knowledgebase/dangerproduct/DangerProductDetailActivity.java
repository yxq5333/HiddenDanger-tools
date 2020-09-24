package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.dangerproduct;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPActivity;
import com.hg.hollowgoods.util.anim.recyclerview.adapters.ScaleInAnimationAdapter;
import com.hg.hollowgoods.util.anim.recyclerview.animators.LandingAnimator;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.adapter.knowledgebase.dangerproduct.DangerProductDetailAdapter;
import com.xhtt.hiddendangermaster.bean.knowledgebase.dangerproduct.DangerProduct;
import com.xhtt.hiddendangermaster.bean.knowledgebase.dangerproduct.DangerProductDetail;
import com.xhtt.hiddendangermaster.constant.ParamKey;

import java.util.ArrayList;

/**
 * 危化品信息界面
 *
 * @author HG
 */

public class DangerProductDetailActivity extends BaseMVPActivity<DangerProductDetailPresenter> implements DangerProductDetailContract.View {

    private RecyclerView result;

    private DangerProductDetailAdapter adapter;
    private ArrayList<DangerProductDetail> data = new ArrayList<>();
    private DangerProduct parentData;

    @Override
    public int bindLayout() {
        return R.layout.activity_danger_product_detail;
    }

    @Override
    public void initParamData() {
        parentData = baseUI.getParam(ParamKey.ParentData, new DangerProduct());
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_danger_product_detail);

        result = findViewById(R.id.rv_result);

        result.setHasFixedSize(true);
        result.setItemAnimator(new LandingAnimator());
        result.setLayoutManager(new LinearLayoutManager(baseUI.getBaseContext()));

        adapter = new DangerProductDetailAdapter(baseUI.getBaseContext(), R.layout.item_danger_product_detail, data);
        result.setAdapter(new ScaleInAnimationAdapter(adapter));

        initData();
    }

    @Override
    public void setListener() {

    }

    @Override
    public DangerProductDetailPresenter createPresenter() {
        return new DangerProductDetailPresenter();
    }

    private void initData() {

        ArrayList<DangerProductDetail> temp = new ArrayList<>();

        DangerProductDetail item = new DangerProductDetail();
        item.setLabel("品名");
        item.setText(parentData.getNameProd());
        temp.add(item);

        item = new DangerProductDetail();
        item.setLabel("别名");
        item.setText(parentData.getNameAlias());
        temp.add(item);

        item = new DangerProductDetail();
        item.setLabel("英文名");
        item.setText(parentData.getNameEn());
        temp.add(item);

        item = new DangerProductDetail();
        item.setLabel("CAS");
        item.setText(parentData.getCas());
        temp.add(item);

        item = new DangerProductDetail();
        item.setLabel("危险性类别");
        item.setText(parentData.getTypeDanger());
        temp.add(item);

        item = new DangerProductDetail();
        item.setLabel("是否为高毒");
        if (parentData.getHighToxic() != null) {
            item.setText(parentData.getHighToxic() ? "是" : "否");
        } else {
            item.setText("");
        }
        temp.add(item);

        item = new DangerProductDetail();
        item.setLabel("是否为重点监管");
        if (parentData.getKeySupervision() != null) {
            item.setText(parentData.getKeySupervision() ? "是" : "否");
        } else {
            item.setText("");
        }
        temp.add(item);

        item = new DangerProductDetail();
        item.setLabel("是否为剧毒");
        if (parentData.getRankPoison() != null) {
            item.setText(parentData.getRankPoison() ? "是" : "否");
        } else {
            item.setText("");
        }
        temp.add(item);

        item = new DangerProductDetail();
        item.setLabel("是否为易制毒");
        if (parentData.getEasyMakePoison() != null) {
            item.setText(parentData.getEasyMakePoison() ? "是" : "否");
        } else {
            item.setText("");
        }
        temp.add(item);

        data.clear();
        data.addAll(temp);
        adapter.refreshData(data);
    }

}
