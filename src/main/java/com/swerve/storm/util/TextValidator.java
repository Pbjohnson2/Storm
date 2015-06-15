package com.swerve.storm.util;

import android.widget.EditText;
import android.widget.TextView;

public class TextValidator {
    public boolean isValidEditText(final TextView TextView) {
        if(TextView == null){
            return false;
        }
        final String text = TextView.getText().toString();
        if (text == null) {
            return false;
        }
        if(text.isEmpty()){
            return false;
        }
        return true;
    }

    public boolean isValidInput(final String text) {
        if (text == null) {
            return false;
        }
        if (text.isEmpty()) {
            return false;
        }
        return true;
    }
}
