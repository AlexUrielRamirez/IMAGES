package com.Etiflex.Splash.Principal;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.Etiflex.Splash.BuscadorEPC.Buscador;
import com.Etiflex.Splash.ConnectorManager;
import com.Etiflex.Splash.Inventario.Inventario;
import com.uhf.uhf.R;

public class Principal extends AppCompatActivity {

    private RelativeLayout Buscador;
    private LinearLayout OptionsPanel;
    private TextView number_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Buscador = findViewById(R.id.purple_layout);
        OptionsPanel = findViewById(R.id.ll_options);
        number_3 = findViewById(R.id.number_3);

        findViewById(R.id.buscar_epc).setOnClickListener(v->{

            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,findViewById(R.id.title_3),"number");
            Intent in = new Intent(this,Buscador.class);
            startActivity(in,activityOptionsCompat.toBundle());

        });

        findViewById(R.id.btn_inventario).setOnClickListener(v-> startActivity(new Intent(this, Inventario.class)));

    }

}
