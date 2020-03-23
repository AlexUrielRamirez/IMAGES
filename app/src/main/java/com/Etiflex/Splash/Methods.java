package com.Etiflex.Splash;

import android.animation.Animator;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TableRow;

public class Methods {

    public void PlayBeep_Short(){
        new ToneGenerator(AudioManager.STREAM_MUSIC, 100).startTone(ToneGenerator.TONE_CDMA_PIP,100);
    }

    public void PlayBeep_Button(){
        try {
            new ToneGenerator(AudioManager.STREAM_MUSIC, 100).startTone(ToneGenerator.TONE_CDMA_PIP,100);
        }catch (RuntimeException e){}
    }

    public void circularRevealActivity(View ROOT, RelativeLayout ColoredPanel) {

        int cx = ColoredPanel.getWidth() / 2;
        int cy = ColoredPanel.getHeight() / 2;

        float finalRadius = Math.max(ColoredPanel.getWidth(), ColoredPanel.getHeight());
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(ColoredPanel, cx, cy, 0, finalRadius);
        circularReveal.setDuration(1000);
        circularReveal.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) { }
            @Override
            public void onAnimationEnd(Animator animation) {
                ROOT.setVisibility(View.VISIBLE);
                ColoredPanel.setVisibility(View.GONE);
            }
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        ColoredPanel.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

}
