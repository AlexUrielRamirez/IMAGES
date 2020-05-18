package com.Etiflex.Splash.Principal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.Etiflex.Splash.ConnectorManager;
import com.Etiflex.Splash.DEMO.Buscador.Buscador;
import com.Etiflex.Splash.DEMO.Inventario;
import com.Etiflex.Splash.GlobalPreferences;
import com.Etiflex.Splash.Methods;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.bean.RXInventoryTag;
import com.uhf.uhf.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

import static com.Etiflex.Splash.DEMO.Inventario.PanelMensaje;

public class Principal extends AppCompatActivity {

    private String IdTienda, NombreTienda, DireccionTienda;

    private RelativeLayout FragmentHolder;

    private LinearLayout Panel_1, Panel_1_Carga;
    private EditText et_IdTienda;
    private Button btn_continuar_panel;
    private TextView txt_error_panel_1;

    private JSONObject Data_Store = null;

    private interface AN_get_Store{
        @FormUrlEncoded
        @POST("/getStore.php")
        void setData(
                @Field("IdTienda") String IdTienda,
                Callback<Response> callback
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        new Methods().CambiarColorStatusBar(this, android.R.color.white);

        initView();

    }

    private void initView() {

        FragmentHolder = findViewById(R.id.FragmentHolder);

        Panel_1 = findViewById(R.id.Panel_1);
        Panel_1_Carga = findViewById(R.id.Panel_1_panel_carga);
        et_IdTienda = findViewById(R.id.et_id_tienda);
        btn_continuar_panel = findViewById(R.id.btn_continuar_panel_1);
        txt_error_panel_1 = findViewById(R.id.txt_error_IDTienda);
        btn_continuar_panel.setOnClickListener(v->{
            if(et_IdTienda.getText().length() > 0){
                btn_continuar_panel.setEnabled(false);
                et_IdTienda.setEnabled(false);
                txt_error_panel_1.setVisibility(View.GONE);
                Panel_1_Carga.setVisibility(View.VISIBLE);

                new RestAdapter.Builder().setEndpoint(GlobalPreferences.URL).build().create(AN_get_Store.class).setData(et_IdTienda.getText().toString(), new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        try{
                            JSONObject json = new JSONObject(new BufferedReader(new InputStreamReader(response.getBody().in())).readLine());
                            switch (json.getString("Result")){
                                case "OK":
                                    IdTienda = et_IdTienda.getText().toString();
                                    NombreTienda = json.getString("Nombre");
                                    DireccionTienda = json.getString("Dirección");
                                    Data_Store = json;
                                    Data_Store.put("IdTienda", IdTienda);
                                    Panel_1.setVisibility(View.GONE);
                                    RestorePanel_1();
                                    break;
                                case "ERROR_404":
                                    GetTienda_Error("No se encontraron tiendas con el ID ingresado");
                                    break;
                            }
                        }catch (IOException | NullPointerException | JSONException e){
                            Log.e("Pr", e.getMessage());
                            GetTienda_Error("¡Vaya! Algo salió mal, no eres tu, somos nosotros.");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        GetTienda_Error("Sin conexión a la red");
                    }
                });

            }else{
                txt_error_panel_1.setText("Por favor, introduzca un ID válido...");
                txt_error_panel_1.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.btn_inventario).setOnClickListener(v->{
            if(getSupportFragmentManager().findFragmentByTag("Inventario") == null)
                getSupportFragmentManager().beginTransaction().replace(FragmentHolder.getId(), new Inventario(),"Inventario").commit();

            Animation animation = AnimationUtils.loadAnimation(this,R.anim.right_to_left_in);
            FragmentHolder.setAnimation(animation);
            FragmentHolder.setVisibility(View.VISIBLE);
            animation.start();
        });

        findViewById(R.id.btn_buscar).setOnClickListener(v->{
            Intent i = new Intent(this, Buscador.class);
            i.putExtra("DataStore", Data_Store.toString());
            startActivity(i);
        });

    }

    private void RestorePanel_1() {
        et_IdTienda.setText("");
        btn_continuar_panel.setEnabled(true);
        et_IdTienda.setEnabled(true);
        Panel_1_Carga.setVisibility(View.GONE);
        txt_error_panel_1.setVisibility(View.GONE);
    }

    private void GetTienda_Error(String Message){
        Panel_1_Carga.setVisibility(View.GONE);
        txt_error_panel_1.setText(Message);
        txt_error_panel_1.setVisibility(View.VISIBLE);
        btn_continuar_panel.setEnabled(true);
        et_IdTienda.setEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == 134){
            if(FragmentHolder.getVisibility() == View.VISIBLE){
                    new ConnectorManager().Connect(Inventario.rx);
                    Inventario.rv_tags.getAdapter().notifyDataSetChanged();
                    if(Inventario.tag_list.size() > 0){
                        if(PanelMensaje.getVisibility() == View.VISIBLE)
                            PanelMensaje.setVisibility(View.GONE);
                    }
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if(FragmentHolder.getVisibility() == View.VISIBLE){
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.left_to_right_out);
            FragmentHolder.setAnimation(animation);
            FragmentHolder.setVisibility(View.GONE);
            animation.start();
        }else{
            Panel_1.setVisibility(View.VISIBLE);
        }
    }
}
