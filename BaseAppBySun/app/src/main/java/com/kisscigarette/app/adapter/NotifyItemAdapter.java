package com.kisscigarette.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kisscigarette.app.R;
import com.kisscigarette.app.httpFrame.entity.result.NotifyListResult;
import com.kisscigarette.app.ui.person.NotifySetActivity;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/9/25.
 */
public class NotifyItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<NotifyListResult.ItemsBean> mNotifyList;

    public NotifyItemAdapter(Context ctx, List<NotifyListResult.ItemsBean> list) {
        this.mContext = ctx;
        this.mNotifyList = list;

    }


    @Override
    public int getCount() {
        return mNotifyList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_notify_set, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NotifyListResult.ItemsBean item = mNotifyList.get(position);
        holder.tvTitle.setText(item.getInfo());
        holder.switchLowerPower.setCheckedImmediatelyNoEvent(item.isOpen());
        holder.switchLowerPower.setOnClickListener(new NotifySetListener(item, position));

        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.switch_lower_power)
        SwitchButton switchLowerPower;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class NotifySetListener implements View.OnClickListener {

        NotifyListResult.ItemsBean opItem;
        int position;

        public NotifySetListener(NotifyListResult.ItemsBean item, int position) {
            this.opItem = item;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ((NotifySetActivity) mContext).notifySet(opItem, this.position, ((SwitchButton) v).isChecked());
        }
    }

}
