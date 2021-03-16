package com.xhtt.hiddendangermaster.adapter.hiddendanger.hiddendanger;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.hg.zero.adapter.list.common.ZCommonAdapter;
import com.hg.zero.adapter.list.mvvm.base.ZViewHolder;
import com.hg.zero.config.ZCommonResource;
import com.hg.zero.datetime.ZDateTimeUtils;
import com.hg.zero.file.ZAppFile;
import com.hg.zero.listener.ZOnRecyclerViewItemClickOldListener;
import com.hg.zero.listener.ZOnViewClickListener;
import com.xhtt.hiddendangermaster.R;
import com.xhtt.hiddendangermaster.bean.hiddendanger.hiddendanger.HiddenDanger;

import java.util.List;

/**
 * Created by Hollow Goods on 2019-04-08.
 */
public class HiddenDangerListAdapter extends ZCommonAdapter<HiddenDanger> {

    private ZOnRecyclerViewItemClickOldListener onButtonClickListener;

    public HiddenDangerListAdapter(Context context, int layoutId, List<HiddenDanger> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(ZViewHolder viewHolder, HiddenDanger item, int position) {

        viewHolder.setText(R.id.tv_hiddenDangerDescribe, item.getHiddenDescribe());

        StringBuilder checkDate = new StringBuilder();
        checkDate.append("检查日期");
        checkDate.append(" ");
        checkDate.append(ZDateTimeUtils.getDateTimeString(item.getCheckDateShow(), ZDateTimeUtils.DateFormatMode.LINE_YMD));
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

        List<ZAppFile> img = item.getAppHiddenPhotoList();

        StringBuilder hiddenDangerPhotoCount = new StringBuilder();
        hiddenDangerPhotoCount.append("(");
        hiddenDangerPhotoCount.append(img == null ? 0 : img.size());
        hiddenDangerPhotoCount.append("张");
        hiddenDangerPhotoCount.append(")");
        viewHolder.setText(R.id.tv_hiddenDangerPhotoCount, hiddenDangerPhotoCount.toString());

        if (img == null || img.size() == 0) {
            viewHolder.setImageResource(R.id.iv_img, ZCommonResource.getImageError());
        } else {
            viewHolder.setImageByUrl(R.id.iv_img, img.get(img.size() - 1).getUrl());
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

        viewHolder.setOnClickListener(R.id.btn_edit, new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_change, new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_detail, new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.cv_all, new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnLongClickListener(R.id.cv_all, new ZOnViewClickListener(false) {
            @Override
            public void onViewLongClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemLongClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_ledger, new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_ledger2, new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_changeFile, new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });

        viewHolder.setOnClickListener(R.id.btn_changeFile2, new ZOnViewClickListener(false) {
            @Override
            public void onViewClick(View view, int id) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRecyclerViewItemClick(view, viewHolder, position);
                }
            }
        });
    }

    public void setOnButtonClickListener(ZOnRecyclerViewItemClickOldListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

}
