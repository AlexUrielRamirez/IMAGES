package com.Etiflex.Splash.Principal;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.Etiflex.Splash.ConnectorManager;
import com.Etiflex.Splash.DEMO.Inventario;
import com.Etiflex.Splash.Methods;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.bean.RXInventoryTag;
import com.uhf.uhf.R;

public class Principal extends AppCompatActivity {

    private RelativeLayout FragmentHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        new Methods().CambiarColorStatusBar(this, android.R.color.white);

        initView();

    }

    private void initView() {

        FragmentHolder = findViewById(R.id.FragmentHolder);

        findViewById(R.id.btn_inventario).setOnClickListener(v->{

        });

        findViewById(R.id.btn_buscar).setOnClickListener(v->{
            if(getSupportFragmentManager().findFragmentByTag("Inventario") == null)
                getSupportFragmentManager().beginTransaction().replace(FragmentHolder.getId(), new Inventario(),"Inventario").commit();

            Animation animation = AnimationUtils.loadAnimation(this,R.anim.right_to_left_in);
            FragmentHolder.setAnimation(animation);
            FragmentHolder.setVisibility(View.VISIBLE);
            animation.start();

        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == 134){
            if(FragmentHolder.getVisibility() == View.VISIBLE){
                if(getSupportFragmentManager().findFragmentByTag("Inventario") != null){
                    new ConnectorManager().Connect(Inventario.rx);
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(FragmentHolder.getVisibility() == View.VISIBLE){
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.left_to_right_out);
            FragmentHolder.setAnimation(animation);
            FragmentHolder.setVisibility(View.GONE);
            animation.start();
        }
    }
}
