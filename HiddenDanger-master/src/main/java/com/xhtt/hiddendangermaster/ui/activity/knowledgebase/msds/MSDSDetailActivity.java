package com.xhtt.hiddendangermaster.ui.activity.knowledgebase.msds;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hg.hollowgoods.constant.HGCommonResource;
import com.hg.hollowgoods.constant.HGConstants;
import com.hg.hollowgoods.ui.base.mvp.BaseMVPActivity;
import com.hg.hollowgoods.util.anim.recyclerview.adapters.ScaleInAnimationAdapter;
import com.hg.hollowgoods.util.anim.recyclerview.animators.LandingAnimator;
import com.hg.hollowgoods.widget.itemdecoration.sticky.PinnedHeaderItemDecoration;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.adapter.knowledgebase.msds.MSDSDetailAdapter;
import com.xhtt.hiddendangermaster.bean.knowledgebase.msds.MSDS;
import com.xhtt.hiddendangermaster.bean.knowledgebase.msds.MSDSDetail;
import com.xhtt.hiddendangermaster.constant.Constants;
import com.xhtt.hiddendangermaster.constant.ParamKey;

import java.util.ArrayList;

/**
 * MSDS界面
 *
 * @author HG
 */

public class MSDSDetailActivity extends BaseMVPActivity<MSDSDetailPresenter> implements MSDSDetailContract.View {

    private RecyclerView result;

    private MSDSDetailAdapter adapter;
    private ArrayList<MSDSDetail> data = new ArrayList<>();
    private MSDS parentData;

    @Override
    public int bindLayout() {
        return R.layout.activity_msds_detail;
    }

    @Override
    public void initParamData() {
        parentData = baseUI.getParam(ParamKey.ParentData, new MSDS());
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(HGCommonResource.BACK_ICON, R.string.title_activity_msdsdetail);

        result = findViewById(R.id.rv_result);

        result.setHasFixedSize(true);
        result.setItemAnimator(new LandingAnimator());
        result.setLayoutManager(new LinearLayoutManager(baseUI.getBaseContext()));
        PinnedHeaderItemDecoration.Builder builder = new PinnedHeaderItemDecoration.Builder(HGConstants.LIST_ITEM_TYPE_HEADER);
        result.addItemDecoration(builder.create());

        adapter = new MSDSDetailAdapter(baseUI.getBaseContext(), data);
        result.setAdapter(new ScaleInAnimationAdapter(adapter));

        initData();
    }

    @Override
    public void setListener() {

    }

    @Override
    public MSDSDetailPresenter createPresenter() {
        return new MSDSDetailPresenter();
    }

    private void initData() {

        ArrayList<MSDSDetail> temp = new ArrayList<>();
        String[] texts;

        // 第1部分标题
        MSDSDetail item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第1部分：化学品名称");
        temp.add(item);

        // 第1部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_1);
        texts = new String[]{
                parentData.getChemicalsNameCn(),// 1
                parentData.getChemicalsNameEn(),// 2
                parentData.getNameCn2(),// 3
                parentData.getNameEn2(),// 4
                parentData.getTechnicalManualNo(),// 5
                parentData.getCasno(),// 6
                parentData.getMolecularFormula(),// 7
                parentData.getMolecularWeight(),// 8
        };
        item.setTexts(texts);
        temp.add(item);

        // 第2部分标题
        item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第2部分：成分/组成信息");
        temp.add(item);

        // 第2部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_2);
        texts = new String[]{
                parentData.getContents(),// 1
        };
        item.setTexts(texts);
        temp.add(item);

        // 第3部分标题
        item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第3部分：危险性描述");
        temp.add(item);

        // 第3部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_3);
        texts = new String[]{
                parentData.getHealthHazard(),// 1
                parentData.getEnvironmentHazards(),// 2
                parentData.getRiskExplosion(),// 3
        };
        item.setTexts(texts);
        temp.add(item);

        // 第4部分标题
        item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第4部分：急救措施");
        temp.add(item);

        // 第4部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_4);
        texts = new String[]{
                parentData.getSkinContact(),// 1
                parentData.getEyeContact(),// 2
                parentData.getInhalation(),// 3
                parentData.getIngestion(),// 4
        };
        item.setTexts(texts);
        temp.add(item);

        // 第5部分标题
        item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第5部分：消防措施");
        temp.add(item);

        // 第5部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_5);
        texts = new String[]{
                parentData.getHazardousProperties(),// 1
                parentData.getHarmfulCombustionProducts(),// 2
                parentData.getExtinguishmentMethod(),// 3
        };
        item.setTexts(texts);
        temp.add(item);

        // 第6部分标题
        item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第6部分：泄露应急处理");
        temp.add(item);

        // 第6部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_6);
        texts = new String[]{
                parentData.getEmergencyHandling(),// 1
        };
        item.setTexts(texts);
        temp.add(item);

        // 第7部分标题
        item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第7部分：操作处理与储存");
        temp.add(item);

        // 第7部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_7);
        texts = new String[]{
                parentData.getHandlingPrecautions(),// 1
                parentData.getStorePrecautions(),// 2
        };
        item.setTexts(texts);
        temp.add(item);

        // 第8部分标题
        item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第8部分：接触控制/个体防护");
        temp.add(item);

        // 第8部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_8);
        texts = new String[]{
                parentData.getMacCn(),// 1
                parentData.getUssr(),// 2
                parentData.getTlvtn(),// 3
                parentData.getTlvwn(),// 4
                parentData.getEngineeringControl(),// 5
                parentData.getRespiratoryProtection(),// 6
                parentData.getEyeProtection(),// 7
                parentData.getBodyProtection(),// 8
                parentData.getHandProtection(),// 9
                parentData.getOtherProtection(),// 10
        };
        item.setTexts(texts);
        temp.add(item);

        // 第9部分标题
        item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第9部分：理化特性");
        temp.add(item);

        // 第9部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_9);
        texts = new String[]{
                parentData.getBasis(),// 1
                parentData.getAppearanceCharacter(),// 2
                parentData.getMeltingPoint(),// 3
                parentData.getBoil(),// 4
                parentData.getRelativeDensity(),// 5
                parentData.getRelativeVapourDensity(),// 6
                parentData.getSaturatedVaporPressure(),// 7
                parentData.getCombustionHeat(),// 8
                parentData.getCriticalTemperature(),// 9
                parentData.getCriticalPressure(),// 10
                parentData.getWaterPartitionCoefficient(),// 11
                parentData.getFlashPoint(),// 12
                parentData.getIgnitionTemperature(),// 13
                parentData.getUpperExplosiveLimit(),// 14
                parentData.getLowExplosiveLimit(),// 15
                parentData.getMainApplication(),// 16
        };
        item.setTexts(texts);
        temp.add(item);

        // 第10部分标题
        item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第10部分：稳定性和反应活性");
        temp.add(item);

        // 第10部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_10);
        texts = new String[]{
                parentData.getProhibitedSubstance(),// 1
        };
        item.setTexts(texts);
        temp.add(item);

        // 第11部分标题
        item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第11部分：毒理学资料");
        temp.add(item);

        // 第11部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_11);
        texts = new String[]{
                parentData.getLd50(),// 1
                parentData.getLc50(),// 2
        };
        item.setTexts(texts);
        temp.add(item);

        // 第12部分标题
        item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第12部分：生态学资料");
        temp.add(item);

        // 第12部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_12);
        texts = new String[]{
                parentData.getOtherHarmfulEffects(),// 1
        };
        item.setTexts(texts);
        temp.add(item);

        // 第13部分标题
        item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第13部分：废弃处置");
        temp.add(item);

        // 第13部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_13);
        texts = new String[]{
                parentData.getWasteDisposalMethod(),// 1
        };
        item.setTexts(texts);
        temp.add(item);

        // 第14部分标题
        item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第14部分：运输信息");
        temp.add(item);

        // 第14部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_14);
        texts = new String[]{
                parentData.getDangerousGoodsNo(),// 1
                parentData.getUnNo(),// 2
                parentData.getPackingGroup(),// 3
                parentData.getPackingMethod(),// 4
                parentData.getTransportConsiderations(),// 5
        };
        item.setTexts(texts);
        temp.add(item);

        // 第15部分标题
        item = new MSDSDetail();
        item.setItemType(HGConstants.LIST_ITEM_TYPE_HEADER);
        item.setLabel("第15部分：法规信息");
        temp.add(item);

        // 第15部分内容
        item = new MSDSDetail();
        item.setItemType(Constants.LIST_ITEM_TYPE_15);
        texts = new String[]{
                parentData.getRegulatoryInformation(),// 1
        };
        item.setTexts(texts);
        temp.add(item);

        data.clear();
        data.addAll(temp);
        adapter.refreshData(data);
    }

}
