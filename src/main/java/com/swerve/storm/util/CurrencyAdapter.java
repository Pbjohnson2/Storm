package com.swerve.storm.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.swerve.storm.R;
import com.swerve.storm.model.StormCurrency;

public class CurrencyAdapter extends BaseAdapter {
    private static final StormCurrency [] CURRENCIES = StormCurrency.values();
    Context context;

    public CurrencyAdapter(final Context context) {
        super();
        this.context = context;

    }

    @Override
    public int getCount() {
        return CURRENCIES.length;
    }

    @Override
    public StormCurrency getItem(int position) {
        return CURRENCIES[position];
    }

    @Override
    public long getItemId(int position) {
        return CURRENCIES[position].hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.coin_item_view, parent, false);
            holder.currencyImage = (ImageView) convertView.findViewById(R.id.center_coin);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final StormCurrency stormCurrency = CURRENCIES[position];
        holder.currencyImage.setImageResource(stormCurrency.getResid());
        return convertView;
    }

    private class ViewHolder {
        ImageView currencyImage;
    }
}
