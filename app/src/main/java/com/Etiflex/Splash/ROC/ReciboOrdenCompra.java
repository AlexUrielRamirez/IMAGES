package com.Etiflex.Splash.ROC;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Etiflex.Splash.ConnectorManager;
import com.Etiflex.Splash.Methods;
import com.Etiflex.Splash.Splash;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.bean.RXInventoryTag;
import com.uhf.uhf.R;

import java.util.ArrayList;

import static android.view.View.GONE;

public class ReciboOrdenCompra extends AppCompatActivity {

    private RelativeLayout Panel_1, Panel_2, Panel_2_1;

    private EditText et_OrdenCompra;
    private TextView btn_siguiente;

    private TextView txt_Estado, txt_OrdenCompra;
    private ProgressBar pb_conteo, pb_processo;

    private String OrdenCompra;

    private ArrayList<String> main_list, receiver_list;

    private Handler ha, ha_check;
    private Runnable r, r_check;
    private boolean IDLE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibo_orden_compra);

        new Splash().CambiarColorStatusBar(this,R.color.rojo_etiflex);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Panel_1 = findViewById(R.id.panel_1);
        Panel_2 = findViewById(R.id.panel_2);
        Panel_2_1 = findViewById(R.id.panel_2_1);

        et_OrdenCompra = findViewById(R.id.et_orden_compra);
        btn_siguiente = findViewById(R.id.btn_siguiente);

        txt_Estado = findViewById(R.id.txt_estado);
        txt_OrdenCompra = findViewById(R.id.txt_orden_compra);
        pb_conteo = findViewById(R.id.pb_portal);

        pb_processo = findViewById(R.id.pb_portal_process);

        btn_siguiente.setOnClickListener(v->{
            OrdenCompra = et_OrdenCompra.getText().toString();
            Panel_2.setVisibility(View.VISIBLE);
            LoadListContent();
        });

        setSupportActionBar(toolbar);

        this.setTitle("Orden Compra");

        et_OrdenCompra.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0){
                    if(btn_siguiente.getVisibility() != View.VISIBLE){
                        Animation animation_in = AnimationUtils.loadAnimation(ReciboOrdenCompra.this, R.anim.bottom_up_in);
                        btn_siguiente.setAnimation(animation_in);
                        btn_siguiente.setVisibility(View.VISIBLE);
                        animation_in.start();
                    }
                }else{
                    if(btn_siguiente.getVisibility() != GONE){
                        Animation animation_out = AnimationUtils.loadAnimation(ReciboOrdenCompra.this, R.anim.center_bottom_out);
                        btn_siguiente.setAnimation(animation_out);
                        animation_out.start();
                        btn_siguiente.setVisibility(GONE);
                    }
                }
            }
        });

    }

    private void LoadListContent() {
        new Handler().postDelayed(() -> {
            main_list = new ArrayList<>();
            receiver_list = new ArrayList<>();

            main_list.add("75 01 23 45 12 34 50 00 00 00 01 17");
            main_list.add("75 01 23 45 12 34 50 00 00 00 01 18");
            main_list.add("75 01 23 45 12 34 50 00 00 00 01 19");
            main_list.add("75 01 23 45 12 34 50 00 00 00 01 20");
            main_list.add("75 01 23 45 12 34 50 00 00 00 01 21");
            main_list.add("75 01 23 45 12 34 50 00 00 00 01 22");

            pb_conteo.setMax(main_list.size());
            pb_conteo.setProgress(0);

            txt_OrdenCompra.setText(OrdenCompra);

            Panel_2_1.setVisibility(View.VISIBLE);

            initReader();

        }, 3000);
    }

    private void initReader() {

        ha = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                new Thread(() -> new ConnectorManager().Connect(rx)).run();
                ha.postDelayed(this, 1);
            }
        };
        ha_check = new Handler();
        r_check = () -> {
            new Thread(() -> {
                ConnectorManager.connector.disConnect();
                ha_check.removeCallbacks(r_check);
                ha.removeCallbacks(r);
                txt_Estado.setText("Estado: Procesando");
                pb_processo.setVisibility(View.VISIBLE);
                new MatchingTask(receiver_list, main_list, pb_processo, txt_Estado).execute();
                IDLE = true;
            }).run();

        };
        ha.postDelayed(r, 50);

    }

    private RXObserver rx = new RXObserver(){
        @Override
        protected void onInventoryTag(RXInventoryTag tag) {
            new Thread(() -> {
                if(!IDLE){
                    Log.e("mtags", tag.strEPC);
                    ha_check.removeCallbacks(r_check);
                    ha_check.postDelayed(r_check, 3000);
                    if(!receiver_list.contains(tag.strEPC)) {
                        receiver_list.add(tag.strEPC);
                        pb_conteo.setProgress(receiver_list.size());
                        new Methods().PlayBeep_Short();
                    }
                }
            }).run();
        }
    };



}
