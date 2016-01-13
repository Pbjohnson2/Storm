package com.swerve.storm.util.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.swerve.storm.R;

import lombok.RequiredArgsConstructor;

public class WindmillProgressBar extends RelativeLayout{
    private ImageView windmillProgressDialog;
    private ImageView windmillMast;
    private Animation rotation;

    public WindmillProgressBar(final Context context) {
        super(context);
        initialize();
    }

    private void initialize(){
        final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.windmill_progress_dialog, this, true);
        windmillProgressDialog = (ImageView) view.findViewById(R.id.windmill_spinner);
        windmillMast = (ImageView) view.findViewById(R.id.windmill_mast);
        rotation = AnimationUtils.loadAnimation(
                getContext(),
                R.anim.windmill_rotation);
    }

    public void startSpinner(final ViewGroup parentView){
        final Animation bottomUp = AnimationUtils.loadAnimation(
                getContext(),
                R.anim.bottom_up);
        parentView.addView(this);
        startAnimation(bottomUp);
        rotation.setRepeatCount(Animation.INFINITE);
        windmillProgressDialog.startAnimation(rotation);
    }

    public void stopSpinner(final ViewGroup parentView){
        rotation.cancel();
        clearAnimation();
        final Animation bottomDown = AnimationUtils.loadAnimation(
                getContext(),
                R.anim.bottom_down);
        bottomDown.setAnimationListener(
                new Animation.AnimationListener(){
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //Do Nothing
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        parentView.removeView(WindmillProgressBar.this);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        //Do Nothing
                    }
                });
        startAnimation(bottomDown);
    }
}
