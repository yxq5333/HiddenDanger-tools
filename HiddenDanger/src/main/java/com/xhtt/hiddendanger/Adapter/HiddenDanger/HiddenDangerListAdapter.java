package com.xhtt.hiddendanger.Adapter.HiddenDanger;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.request.RequestOptions;
import com.hg.hollowgoods.adapter.list.mvvm.base.ViewHolder;
import com.hg.hollowgoods.Adapter.BaseRecyclerView.CommonAdapter;
import com.hg.hollowgoods.Bean.AppFile;
import com.hg.hollowgoods.Constant.HGCommonResource;
import com.hg.hollowgoods.UI.Base.Click.OnRecyclerViewItemClickListener;
import com.hg.hollowgoods.UI.Base.Click.OnViewClickListener;
import com.hg.hollowgoods.Util.Glide.GlideOptions;
import com.hg.hollowgoods.Util.StringUtils;
import com.xhtt.hiddendanger.Bean.HiddenDanger.HiddenDanger;
import com.xhtt.hiddendanger.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-08.
 */
public class HiddenDangerListAdapter extends CommonAdapter<HiddenDanger> {

    private OnRecyclerViewItemClickListener onButtonClickListener;

    public HiddenDangerListAdapter(Context context, int layoutId, List<HiddenDanger> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, HiddenDanger item, int position) {

        viewHolder.setText(R.id.tv_hiddenDangerDescribe, item.getHiddenDescribe());

        StringBuilder checkDate = new StringBuilder();
        checkDate.append("检查日期");
        checkDate.append(" ");
        checkDate.append(StringUtils.getDateTimeString(item.getCheckDateShow(), StringUtils.DateFormatMode.LINE_YMD));
        viewHolder.setText(R.id.tv_checkDate, checkDate.toString());

        StringBuilder checkPeople = new StringBuilder();
        checkPeople.append("检查人");
        checkPeople.append(" ");
        checkPeople.append(TextUtils.isEmpty(item.getCheckPeople()) ? "" : item.getCheckPeople());
        viewHolder.setText(R.id.tv_checkPeople, checkPeople.toString());

        viewHolder.setText(R.id.tv_count, "第" + item.getTimes() + "次检查");

        if (item.getStatus() == null) {
            // 没点过任何提交按钮
            viewHolder.setSlanted(R.id.status, R.color.grey2, "未整改");
        } else if (item.getStatus() == HiddenDanger.STATUS_CHANGED) {
            viewHolder.setSlanted(R.id.status, R.color.colorAccent, "已整改");
        } else {
            viewHolder.setSlanted(R.id.status, R.color.grey2, "未整改");
        }

        ArrayList<AppFile> img = item.getMedia().get(25);

        StringBuilder hiddenDangerPhotoCount = new StringBuilder();
        hiddenDangerPhotoCount.append("(");
        hiddenDangerPhotoCount.append(img == null ? 0 : img.size());
        hiddenDangerPhotoCount.append("张");
        hiddenDangerPhotoCount.append(")");
        viewHolder.setText(R.id.tv_hiddenDangerPhotoCount, hiddenDangerPhotoCount.toString());

        if (img == null || img.size() == 0) {
            viewHolder.setImageResource(R.id.iv_img, HGCommonResource.IMAGE_LOAD_ERROR);
        } else {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(HGCommonResource.IMAGE_LOADING)
                    .error(HGCommonResource.IMAGE_LOAD_ERROR)
                    .centerCrop();
            GlideOptions glideOptions = new GlideOptions(img.get(img.size() - 1).getUrl(), null, GlideOptions.NORMAL_FADE_IN, requestOptions);

            viewHolder.setImageByUrl(R.id.iv_img, glideOptions);
        }

        if (item.getStatus() == null) {
            viewHolder.setVisible(R.id.btn_edit, true);
            viewHolder.setVisible(R.id.ll_unchanged, true);
            viewHolder.setVisible(R.id.ll_changed, false);
        } else {
            viewHolder.setVisible(R.id.btn_edit, false);
            viewHolder.setVisible(R.id.ll_unchanged, item.getStatus() == HiddenDanger.STATUS_UNCHANGED);
            viewHolder.setVisible(R.id.ll_changed, item.getStatus() == HiddenDanger.STATUS_CHANGED);
        }

        if (TextUtils.isEmpty(item.getChangeDate())) {
            viewHolder.setVisible(R.id.ll_changeDate, false);
        } else {
            if (checkPeople.toString().contains(item.getChangeDate())) {
                viewHolder.setVisible(R.id.ll_changeDate, false);
            } else {
                viewHolder.setVisible(R.id.ll_changeDate, true);
                viewHolder.setText(R.id.tv_changeDate, "整改日期 " + item.getChangeDate());
            }
        }

        viewHolder.setOnClickListener(R.id.btn_edit, new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_change, new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_detail, new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.cv_all, new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnLongClickListener(R.id.cv_all, new OnViewClickListener(false) {
            @Override
            public void onViewLongClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemLongClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_ledger, new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_ledger2, new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_changeFile, new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_changeFile2, new OnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });
    }

    public void setOnButtonClickListener(OnRecyclerViewItemClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

}
