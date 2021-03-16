package com.xhtt.hiddendangermaster.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hg.zero.bean.eventbus.ZEvent;
import com.hg.zero.logger.ZLogger;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xhtt.hiddendangermaster.BuildConfig;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.constant.EventActionCode;
import com.xhtt.hiddendangermaster.ui.base.HDBaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * Created by Hollow Goods on 2019-07-24.
 */
public class WXEntryActivity extends HDBaseActivity implements IWXAPIEventHandler {

    /**
     * 微信登录相关
     */
    private IWXAPI api;

    @Override
    public int bindLayout() {
        return R.layout.activity_wx_entry;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        // 通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, BuildConfig.WXAndroidKey, true);
        // 将应用的appid注册到微信
        api.registerApp(BuildConfig.WXAndroidKey);
        // 注意：
        // 第三方开发者如果使用透明界面来实现WXEntryActivity，
        // 需要判断handleIntent的返回值，如果返回值为false，
        // 则说明入参不合法未被SDK处理，应finish当前透明界面，
        // 避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            boolean result = api.handleIntent(getIntent(), this);
            if (!result) {
                ZLogger.e("参数不合法，未被SDK处理，退出");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        ZLogger.e("baseReq:" + new Gson().toJson(baseReq));
    }

    /**
     * 登录返回数据
     * {
     * "code": "001wqPRw1qIhHg0bR4Rw1jeARw1wqPRm",
     * "country": "CN",
     * "lang": "zh_CN",
     * "state": "walk_more",
     * "url": "wxa99a40ea82cf675d://oauth?code=001wqPRw1qIhHg0bR4Rw1jeARw1wqPRm&state=walk_more",
     * "errCode": 0
     * }
     *
     * @param baseResp BaseResp
     */
    @Override
    public void onResp(BaseResp baseResp) {

        ZLogger.e("baseReq:" + new Gson().toJson(baseResp));
        ZLogger.e("baseResp:" + baseResp.errStr + "," + baseResp.openId + "," + baseResp.transaction + "," + baseResp.errCode);
        String result;

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";

                Map<String, Object> temp = new Gson().fromJson(new Gson().toJson(baseResp),
                        new TypeToken<Map<String, Object>>() {
                        }.getType()
                );
//                MyApplication.createApplication().setWeChatCode(temp.get("code") + "");

                ZEvent event = new ZEvent(EventActionCode.WE_CHAT_LOGIN_GET_CODE_SUCCESS);
                EventBus.getDefault().post(event);

                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                finish();
                break;
            default:
                result = "发送返回";
                finish();
                break;
        }

    }

}
