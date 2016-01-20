package com.swerve.storm.ui.retrieve;

import android.content.Context;
import android.widget.ImageView;
import com.swerve.storm.R;
import com.swerve.storm.model.StormCurrency;

public class Coin extends ImageView {
    private StormCurrency stormCurrency;

    public Coin(Context context) {
        super(context);
        inflate(getContext(), R.layout.coin, null);
    }

    public void setStormCurrency(final StormCurrency stormCurrency) {
        this.stormCurrency = stormCurrency;
        setBackgroundResource(stormCurrency.getResid());
    }
}
