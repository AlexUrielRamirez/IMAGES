package com.Etiflex.Splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.Etiflex.Splash.Principal.Principal;
import com.Etiflex.Splash.ROC.ReciboOrdenCompra;
import com.uhf.uhf.R;

public class Splash extends AppCompatActivity {

    @SuppressLint("NewApi")
    public void CambiarColorStatusBar(Activity activity, int color){
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity, color));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        CambiarColorStatusBar(this, android.R.color.white);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("DEVICE", "HORCA");
        editor.apply();

        new Handler().postDelayed(() -> {
            GlobalPreferences.DEVICE = Splash.this.getPreferences(Context.MODE_PRIVATE).getString("DEVICE", "NA");
            switch (GlobalPreferences.DEVICE){
                case "HORCA":
                    startActivity(new Intent(Splash.this, Principal.class));
                    this.finish();
                    break;
                case "SPIDER":
                    startActivity(new Intent(Splash.this, ReciboOrdenCompra.class));
                    break;
                    default:
                        startActivity(new Intent(Splash.this, Principal.class));
            }

        }, 3000);

    }

}
