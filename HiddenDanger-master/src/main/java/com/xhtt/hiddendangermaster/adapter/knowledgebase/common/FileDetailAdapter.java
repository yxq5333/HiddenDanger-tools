package com.xhtt.hiddendangermaster.adapter.knowledgebase.common;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.hg.zero.adapter.list.common.ZCommonAdapter;
import com.hg.zero.adapter.list.mvvm.base.ZViewHolder;
import com.hg.zero.file.ZAppFile;
import com.hg.zero.file.ZFileSelectorUtils;
import com.xhtt.hiddendangermaster.R;

import java.util.List;

/**
 * 文件详情适配器
 * Created by Hollow Goods on 2019-04-03.
 */
public class FileDetailAdapter extends ZCommonAdapter<ZAppFile> {

    private ZFileSelectorUtils fileSelectorUtils;

    public FileDetailAdapter(Context context, int layoutId, List<ZAppFile> data) {
        super(context, layoutId, data);
        fileSelectorUtils = new ZFileSelectorUtils();
    }

    @Override
    protected void convert(ZViewHolder viewHolder, ZAppFile item, int position) {

        viewHolder.setImageResource(R.id.iv_icon, fileSelectorUtils.getFileIcon(item.getGenerateName()));

        TextView name = viewHolder.getView(R.id.tv_name);
        StringBuilder sb = new StringBuilder();
        sb.append("<u>");
        sb.append(item.getOriginalName());
        sb.append("</u>");
        name.setText(Html.fromHtml(sb.toString()));

        viewHolder.setVisible(R.id.line, position != mData.size() - 1);
    }
}
