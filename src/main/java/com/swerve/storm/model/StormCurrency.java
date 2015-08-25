package com.swerve.storm.model;

import com.swerve.storm.R;
import lombok.Getter;

public enum StormCurrency {
    ONE(R.drawable.coin_one, 0.01),
    FIVE(R.drawable.coin_five, 0.05),
    TEN(R.drawable.coin_ten, 0.10),
    TWENTY_FIVE(R.drawable.coin_twenty_five, 0.25);

    @Getter private final int resid;
    @Getter private final double amount;

    StormCurrency(final int resid, final double amount){
        this.resid = resid;
        this.amount = amount;
    }
}
