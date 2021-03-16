package com.xhtt.hiddendangermaster.ui.fragment.companymap;

import android.Manifest;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hg.zero.dialog.ZDialogConfig;
import com.hg.zero.listener.ZOnRecyclerViewItemClickOldListener;
import com.hg.zero.listener.ZOnViewClickListener;
import com.hg.zero.toast.Zt;
import com.hg.zero.ui.fragment.proxy.ZProxyConfig;
import com.hg.zero.ui.fragment.proxy.ZProxyHelper;
import com.hg.zero.util.ZBeanUtils;
import com.hg.zero.widget.refreshlayout.ZRefreshLayout;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.adapter.companymap.MapCompanyListAdapter;
import com.xhtt.hiddendangermaster.bean.CompanyMap;
import com.xhtt.hiddendangermaster.ui.base.HDBaseMVPFragment;
import com.xhtt.hiddendangermaster.ui.fragment.companymap.contract.CompanyMapContract;
import com.xhtt.hiddendangermaster.ui.fragment.companymap.presenter.CompanyMapPresenter;
import com.xhtt.hiddendangermaster.util.CallPhoneUtils;
import com.xhtt.hiddendangermaster.util.GuideUtils;
import com.xhtt.hiddendangermaster.util.LocationChangeUtils;

import org.jetbrains.annotations.NotNull;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 企业地图碎片
 * <p>
 * Created by Hollow Goods on 2020-04-01
 */

public class CompanyMapFragment extends HDBaseMVPFragment<CompanyMapPresenter> implements CompanyMapContract.View {

    private final int PERMISSION_CODE_LOCATION = 1000;
    private final int DIALOG_CODE_EXPORT = 2000;
    private final int DIALOG_CODE_ADD = 2001;
    private final int DIALOG_CODE_LOAD = 2002;
    private final int DIALOG_CODE_ASK_ADD = 2003;
    private final int DEFAULT_ZOOM_COUNT = 14;

    private MapView mMapView;
    private View add;
    private View dif;
    private View myLocation;

    private AppCompatTextView serviceCount;
    private Group addServiceCountGroup;

    @ViewInject(value = R.id.ZRefreshLayout)
    private ZRefreshLayout refreshLayout;

    private AMap aMap;
    // 定义一个UiSettings对象
    private UiSettings mUiSettings;
    private MyLocationStyle myLocationStyle;
    private boolean isFirstLocation = true;
    private Location myLocationTag;
    private String title;
    private ArrayList<CompanyMap> data = new ArrayList<>();
    private HashMap<Long, Marker> markers = new HashMap<>();
    private CompanyMap clickCompanyMap;
    private MapCompanyListAdapter mapCompanyListAdapter;
    private ArrayList<CompanyMap> mapCompanyListData = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.fragment_company_map;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        baseUI.setCommonTitleStyleAutoBackground(R.string.title_fragment_company_map);
//        baseUI.setCommonRightTitleText("导出");
        baseUI.initSearchView(refreshLayout, true);

        mMapView = baseUI.findViewById(R.id.map);
        add = baseUI.findViewById(R.id.fl_add);
        dif = baseUI.findViewById(R.id.fl_dif);
        myLocation = baseUI.findViewById(R.id.fl_myLocation);

        mapCompanyListAdapter = new MapCompanyListAdapter(baseUI.getBaseContext(), R.layout.item_map_company_list, mapCompanyListData);
        refreshLayout.initRecyclerView();
        refreshLayout.addItemDecoration(R.color.line);
        refreshLayout.setAdapter(mapCompanyListAdapter);

        initMapView(savedInstanceState);
    }

    @Override
    public void initViewDelay() {
        baseUI.baseDialog.showProgressDialog(
                new ZDialogConfig.ProgressConfig(DIALOG_CODE_LOAD)
                        .setContent("加载中，请稍候……")
        );
        mPresenter.getData();
    }

    @Override
    public void setListener() {

        baseUI.setOnPermissionsListener((isAgreeAll, requestCode, permissions, isAgree) -> {
            if (isAgreeAll) {
                if (requestCode == PERMISSION_CODE_LOCATION) {
                    initMyLocation();
                }
            }
        });

        checkPermission();

        add.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                mapZoom(1);
            }
        });

        dif.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                mapZoom(-1);
            }
        });

        myLocation.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (myLocationTag != null) {
                    moveMap(new LatLng(myLocationTag.getLatitude(), myLocationTag.getLongitude()), DEFAULT_ZOOM_COUNT);
                }
            }
        });

        mapCompanyListAdapter.setOnItemClickListener(new ZOnRecyclerViewItemClickOldListener(false) {
            @Override
            public void onRecyclerViewItemClick(View view, RecyclerView.ViewHolder viewHolder, int position) {
                refreshLayout.setVisibility(View.GONE);
                CompanyMap companyMap = mapCompanyListData.get(position);
                if (companyMap.getLat() == null || companyMap.getLng() == null) {
                    Zt.error("该企业未填位置信息");
                } else {
                    moveMap(new LatLng(companyMap.getLat(), companyMap.getLng()), 18);
                }
            }
        });

        baseUI.baseDialog.addOnDialogClickListener((code, result, backData) -> {
            if (result) {
                if (code == DIALOG_CODE_ASK_ADD) {
                    baseUI.baseDialog.showProgressDialog(
                            new ZDialogConfig.ProgressConfig(DIALOG_CODE_ADD)
                                    .setContent("提交中，请稍候……")
                    );
                    mPresenter.addServiceCount(clickCompanyMap.getCompanyId());
                }
            }
        });
    }

    @Override
    public CompanyMapPresenter createPresenter() {
        return new CompanyMapPresenter(getActivity());
    }

    @Override
    public void onRightTitleClick(View view, int id) {
        baseUI.baseDialog.showProgressDialog(
                new ZDialogConfig.ProgressConfig(DIALOG_CODE_EXPORT)
                        .setContent("导出中，请稍候……")
        );
        mPresenter.export();
    }

    private void initMapView(Bundle savedInstanceState) {

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
        }

        // 是否显示缩放按钮（+/-），默认是true：显示
        mUiSettings.setZoomControlsEnabled(false);

        // 设置缩放按钮的位置
        // mUiSettings.setZoomPosition( int position);

        // 是否显示指南针，默认是false：不显示
        mUiSettings.setCompassEnabled(false);

        // 是否显示比例尺，默认是false：不显示
        mUiSettings.setScaleControlsEnabled(false);

        // 设置logo位置
        // AMapOptions.LOGO_POSITION_BOTTOM_LEFT
        // LOGO边缘MARGIN（左边）
        // AMapOptions.LOGO_MARGIN_BOTTOM
        // LOGO边缘MARGIN（底部
        // AMapOptions.LOGO_MARGIN_RIGHT
        // LOGO边缘MARGIN（右边）
        // AMapOptions.LOGO_POSITION_BOTTOM_CENTER
        // Logo位置（地图底部居中）
        // AMapOptions.LOGO_POSITION_BOTTOM_LEFT
        // Logo位置（地图左下角）
        // AMapOptions.LOGO_POSITION_BOTTOM_RIGHT
        // Logo位置（地图右下角）
        // mUiSettings.setLogoPosition( int position);
        mUiSettings.setLogoBottomMargin(-100);

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
    }

    private void initMyLocation() {

        // 初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）
        // 如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();

        // 设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);

        // 设置定位类型
        // 只定位一次。
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        // 定位一次，且将视角移动到地图中心点。
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        // 连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
        //  myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        // 连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 以下三种模式从5.1.0版本开始提供
        // 连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        // 连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        // 连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);

        // 设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，.
        // 设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle.showMyLocation(true);

        // 设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_location_icon)));

        // 设置定位蓝点图标的锚点方法。
        // myLocationStyle.anchor(float u, float v);

        // 设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.strokeColor(Color.parseColor("#3B9CFF"));
        // 设置定位蓝点精度圆圈的填充颜色的方法。
        myLocationStyle.radiusFillColor(Color.parseColor("#883B9CFF"));
        // 设置定位蓝点精度圈的边框宽度的方法。
        // myLocationStyle.strokeWidth( float width);

        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);

        // 设置默认定位按钮是否显示，非必需设置。
        mUiSettings.setMyLocationButtonEnabled(false);

        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);

        // 从location对象中获取经纬度信息，地址描述信息，
        // 建议拿到位置之后调用逆地理编码接口获取（获取地址描述数据章节有介绍）
        aMap.setOnMyLocationChangeListener(location -> {

            myLocationTag = location;

            if (isFirstLocation) {
                moveMap(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM_COUNT);
                isFirstLocation = false;
            }
        });

        aMap.setOnMarkerClickListener(marker -> {
            int index = data.indexOf(marker.getObject());
            clickCompanyMap = data.get(index);
            showCompanyServiceDialog();

            return true;
        });
    }

    private void showCompanyServiceDialog() {

        AlertDialog dialog = new AlertDialog.Builder(baseUI.getBaseContext())
                .setView(R.layout.dialog_company_map)
                .show();

        AppCompatTextView companyName = dialog.findViewById(R.id.tv_companyName);
//            AppCompatTextView companyAddress = dialog.findViewById(R.id.tv_companyAddress);

        TextView masterName = dialog.findViewById(R.id.tv_masterName);
        TextView masterPhone = dialog.findViewById(R.id.tv_masterPhone);
        ImageView icon1 = dialog.findViewById(R.id.iv_icon1);
        View phoneLayout1 = dialog.findViewById(R.id.ll_phone1);

        TextView saferName = dialog.findViewById(R.id.tv_saferName);
        TextView saferPhone = dialog.findViewById(R.id.tv_saferPhone);
        ImageView icon2 = dialog.findViewById(R.id.iv_icon2);
        View phoneLayout2 = dialog.findViewById(R.id.ll_phone2);

        TextView contactName = dialog.findViewById(R.id.tv_contactName);
        TextView contactPhone = dialog.findViewById(R.id.tv_contactPhone);
        ImageView icon3 = dialog.findViewById(R.id.iv_icon3);
        View phoneLayout3 = dialog.findViewById(R.id.ll_phone3);

        serviceCount = dialog.findViewById(R.id.tv_serviceCount);
//            FloatingActionButton export = dialog.findViewById(R.id.fab_clearServiceCount);
        FloatingActionButton addServiceCount = dialog.findViewById(R.id.fab_addServiceCount);
        FloatingActionButton guide = dialog.findViewById(R.id.fab_guide);
        addServiceCountGroup = dialog.findViewById(R.id.group_addServiceCount);

        companyName.setText(clickCompanyMap.getCompanyName());

        masterName.setText(clickCompanyMap.getEnterpriseLead());
        masterPhone.setText(clickCompanyMap.getEnterpriseLeadPhone());

        saferName.setText(clickCompanyMap.getEnterpriseSafe());
        saferPhone.setText(clickCompanyMap.getEnterpriseSafePhone());

        contactName.setText(clickCompanyMap.getCompanyContactsName());
        contactPhone.setText(clickCompanyMap.getCompanyContactsPhone());

//            companyAddress.setText(clickCompanyMap.getCompanyAddress());
        serviceCount.setText(clickCompanyMap.getServiceNowCount() + "/" + clickCompanyMap.getServiceTotalCount());

        if (TextUtils.isEmpty(clickCompanyMap.getEnterpriseLead())
                && TextUtils.isEmpty(clickCompanyMap.getEnterpriseLeadPhone())
        ) {
            phoneLayout1.setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(clickCompanyMap.getEnterpriseLead())) {
            masterName.setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(clickCompanyMap.getEnterpriseLeadPhone())) {
            masterPhone.setVisibility(View.GONE);
            icon1.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(clickCompanyMap.getEnterpriseSafe())
                && TextUtils.isEmpty(clickCompanyMap.getEnterpriseSafePhone())
        ) {
            phoneLayout2.setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(clickCompanyMap.getEnterpriseSafe())) {
            saferName.setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(clickCompanyMap.getEnterpriseSafePhone())) {
            saferPhone.setVisibility(View.GONE);
            icon2.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(clickCompanyMap.getCompanyContactsName())
                && TextUtils.isEmpty(clickCompanyMap.getCompanyContactsPhone())
        ) {
            phoneLayout3.setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(clickCompanyMap.getCompanyContactsName())) {
            contactName.setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(clickCompanyMap.getCompanyContactsPhone())) {
            contactPhone.setVisibility(View.GONE);
            icon3.setVisibility(View.GONE);
        }

        switch (clickCompanyMap.getServiceNowCount()) {
            case 1:
                serviceCount.setTextColor(ContextCompat.getColor(baseUI.getBaseContext(), R.color.google_red));
                addServiceCountGroup.setVisibility(View.VISIBLE);
                break;
            case 2:
                serviceCount.setTextColor(Color.parseColor("#FAEE00"));
                addServiceCountGroup.setVisibility(View.VISIBLE);
                break;
            case 3:
                serviceCount.setTextColor(ContextCompat.getColor(baseUI.getBaseContext(), R.color.google_green));
                addServiceCountGroup.setVisibility(View.GONE);
                break;
            case 0:
            default:
                serviceCount.setTextColor(ContextCompat.getColor(baseUI.getBaseContext(), R.color.grey));
                addServiceCountGroup.setVisibility(View.VISIBLE);
                break;
        }

        guide.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                new AlertDialog.Builder(baseUI.getBaseContext())
                        .setItems(new String[]{"百度地图", "高德地图"}, (dialog1, which) -> {
                            switch (which) {
                                case 0:
                                    // 百度地图
                                    LatLng p = new LatLng(clickCompanyMap.getLat(), clickCompanyMap.getLng());
                                    p = LocationChangeUtils.GaoDe2BaiDu(p);
                                    if (!GuideUtils.routeByBaiDuMap(baseUI.getBaseContext(), clickCompanyMap.getCompanyName(), p.latitude, p.longitude)) {
                                        Zt.info("请先安装百度地图");
                                    }
                                    break;
                                case 1:
                                    // 高德地图
                                    if (!GuideUtils.routeByAMap(baseUI.getBaseContext(), clickCompanyMap.getCompanyName(), clickCompanyMap.getLat(), clickCompanyMap.getLng())) {
                                        Zt.info("请先安装高德地图");
                                    }
                                    break;
                            }
                        }).show();
            }
        });

        addServiceCount.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                baseUI.baseDialog.showAlertDialog(
                        new ZDialogConfig.AlertConfig(DIALOG_CODE_ASK_ADD)
                                .setTitle(R.string.z_tips_best)
                                .setContent("请确认本次服务是否完成？")
                );
            }
        });

        phoneLayout1.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                new ZProxyHelper(baseUI.getBaseContext()).requestProxy(new ZProxyConfig()
                        .setPermissions(new String[]{Manifest.permission.CALL_PHONE})
                        .setRequestCode(3344)
                        .setOnProxyRequestPermissionsResult((isAgreeAll, requestCode, permissions, isAgree) -> {
                            if (isAgreeAll) {
                                CallPhoneUtils.callPhone(baseUI.getBaseContext(), clickCompanyMap.getEnterpriseLeadPhone());
                            }
                        })
                );
            }
        });

        phoneLayout2.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                new ZProxyHelper(baseUI.getBaseContext()).requestProxy(new ZProxyConfig()
                        .setPermissions(new String[]{Manifest.permission.CALL_PHONE})
                        .setRequestCode(3344)
                        .setOnProxyRequestPermissionsResult((isAgreeAll, requestCode, permissions, isAgree) -> {
                            if (isAgreeAll) {
                                CallPhoneUtils.callPhone(baseUI.getBaseContext(), clickCompanyMap.getEnterpriseSafePhone());
                            }
                        })
                );
            }
        });

        phoneLayout3.setOnClickListener(new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                new ZProxyHelper(baseUI.getBaseContext()).requestProxy(new ZProxyConfig()
                        .setPermissions(new String[]{Manifest.permission.CALL_PHONE})
                        .setRequestCode(3344)
                        .setOnProxyRequestPermissionsResult((isAgreeAll, requestCode, permissions, isAgree) -> {
                            if (isAgreeAll) {
                                CallPhoneUtils.callPhone(baseUI.getBaseContext(), clickCompanyMap.getCompanyContactsPhone());
                            }
                        })
                );
            }
        });
    }

    private void moveMap(LatLng point, int zoomCount) {

        // 参数依次是：
        // 1.视角调整区域的中心点坐标
        // 2.希望调整到的缩放级别
        // 3.俯仰角0°~45°（垂直与地图时为0）
        // 4.偏航角 0~360° (正北方为0)
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                new CameraPosition(point, zoomCount, 0, 0)
        );
        aMap.animateCamera(mCameraUpdate);
    }

    private void mapZoom(int flag) {
        if (flag > 0) {
            aMap.animateCamera(CameraUpdateFactory.zoomIn());
        } else {
            aMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    private void checkPermission() {

        // SDK在Android 6.0下需要进行运行检测的权限如下：
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };

        if (baseUI.requestPermission(PERMISSION_CODE_LOCATION, permissions)) {
            initMyLocation();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    private void drawMarker(CompanyMap companyMap) {

        if (companyMap.getLat() == null || companyMap.getLng() == null) {
            return;
        }

        Marker m = markers.get(companyMap.getCompanyId());
        if (m != null) {
            m.remove();
        }

        BitmapDescriptor descriptor = null;
        switch (companyMap.getServiceNowCount()) {
            case 1:
                descriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.ic_location_1));
                break;
            case 2:
                descriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.ic_location_2));
                break;
            case 3:
                descriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.ic_location_3));
                break;
            case 0:
            default:
                descriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.ic_location_0));
                break;
        }

        Marker marker = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(companyMap.getLat(), companyMap.getLng()))
                .title("")
                .snippet("")
                .infoWindowEnable(false)
                .icon(descriptor)
        );
        marker.setObject(companyMap);

        markers.put(companyMap.getCompanyId(), marker);
    }

    @Override
    public void getDataSuccess(ArrayList<CompanyMap> tempData) {

        Set<Long> keySet = markers.keySet();
        Iterator<Long> keys = keySet.iterator();
        Marker m;
        while (keys.hasNext()) {
            m = markers.get(keys.next());
            if (m != null) {
                m.remove();
            }
        }

        data.clear();
        markers.clear();

        if (tempData != null) {
            data.addAll(tempData);
        }

        for (int i = 0; i < data.size(); i++) {
            drawMarker(data.get(i));
        }
    }

    @Override
    public void getDataError() {

    }

    @Override
    public void getDataFinish() {
        baseUI.baseDialog.closeDialog(DIALOG_CODE_LOAD);
    }

    @Override
    public void addServiceCountSuccess() {

        Zt.success("提交成功");

        clickCompanyMap.setServiceNowCount(clickCompanyMap.getServiceNowCount() + 1);
        serviceCount.setText(clickCompanyMap.getServiceNowCount() + "/" + clickCompanyMap.getServiceTotalCount());

        switch (clickCompanyMap.getServiceNowCount()) {
            case 1:
                serviceCount.setTextColor(ContextCompat.getColor(baseUI.getBaseContext(), R.color.google_red));
                addServiceCountGroup.setVisibility(View.VISIBLE);
                break;
            case 2:
                serviceCount.setTextColor(Color.parseColor("#FAEE00"));
                addServiceCountGroup.setVisibility(View.VISIBLE);
                break;
            case 3:
                serviceCount.setTextColor(ContextCompat.getColor(baseUI.getBaseContext(), R.color.google_green));
                addServiceCountGroup.setVisibility(View.GONE);
                break;
            case 0:
            default:
                serviceCount.setTextColor(ContextCompat.getColor(baseUI.getBaseContext(), R.color.grey));
                addServiceCountGroup.setVisibility(View.VISIBLE);
                break;
        }

        drawMarker(clickCompanyMap);
    }

    @Override
    public void addServiceCountError() {

    }

    @Override
    public void addServiceCountFinish() {
        baseUI.baseDialog.closeDialog(DIALOG_CODE_ADD);
    }

    @Override
    public void exportSuccess(File file) {
        baseUI.baseDialog.showTipDialog(
                new ZDialogConfig.TipConfig(-1)
                        .setTitle(R.string.z_tips_best)
                        .setContent("已保存至：" + file.getAbsolutePath())
        );
    }

    @Override
    public void exportError() {

    }

    @Override
    public void exportFinish() {
        baseUI.baseDialog.closeDialog(DIALOG_CODE_EXPORT);
    }

    @Override
    public void onSearchKeyChanging(String searchKey) {
        refreshSearch(searchKey, false);
    }

    @Override
    public void onSearched(String searchKey) {
        refreshSearch(searchKey, true);
    }

    private void refreshSearch(String searchKey, boolean isSearched) {

        if (TextUtils.isEmpty(searchKey)) {
            if (refreshLayout.getVisibility() != View.GONE) {
                refreshLayout.setVisibility(View.GONE);
            }
        } else {
            if (refreshLayout.getVisibility() != View.VISIBLE) {
                refreshLayout.setVisibility(View.VISIBLE);
            }
        }

        ArrayList<CompanyMap> temp = new ArrayList<>();
        for (CompanyMap t : data) {
            if (t.getCompanyName().contains(searchKey)) {
                temp.add(t);
            }
        }

        mapCompanyListData.clear();
        mapCompanyListData.addAll(temp);
        mapCompanyListAdapter.refreshData(mapCompanyListData);

        if (isSearched && ZBeanUtils.isCollectionEmpty(mapCompanyListData)) {
            Zt.info("未搜索到该企业");
        }

        refreshLayout.setVisibility(ZBeanUtils.isCollectionEmpty(mapCompanyListData) ? View.GONE : View.VISIBLE);
    }

}
