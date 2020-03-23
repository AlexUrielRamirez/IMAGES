package com.Etiflex.Splash.Inventario;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.Etiflex.Splash.Methods;
import com.Etiflex.Splash.ROC.ModelInventory;
import com.Etiflex.Splash.ROC.RetrofitInterfaces.askOrdenCompra;
import com.Etiflex.Splash.Splash;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;
import com.uhf.uhf.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.Etiflex.Splash.GlobalPreferences.URL;

public class Inventario extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private EditText et_ordencompra;
    private TextView txt_cargando;
    private ProgressBar pb_cargando;
    private TextView btn_continuar;

    private Toolbar toolbar;

    private RelativeLayout RedPanel;

    private TextView btn_almacen_1, btn_almacen_2, btn_almacen_3, btn_almacen_4;

    private RelativeLayout LoadingPanel;

    private ArrayList<ModelInventory> main_list;
    private ModelInventory model;

    private LinearLayout InventoryLayout;

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        setContentView(R.layout.activity_inventario);
        initViews();
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setActionBar(toolbar);
        this.setTitle("Inventario");

    }

    private void initViews() {

        toolbar = findViewById(R.id.Toolbar);
        RedPanel = findViewById(R.id.colored_panel);

        et_ordencompra = findViewById(R.id.et_orden_compra);
        txt_cargando = findViewById(R.id.mensaje_panel_mini);
        pb_cargando = findViewById(R.id.pb_panel_mini);
        btn_continuar = findViewById(R.id.btn_continuar);

        btn_continuar.setOnClickListener(v->BuscarOrdenCompra());

        btn_almacen_1 = findViewById(R.id.almacen_1);
        btn_almacen_2 = findViewById(R.id.almacen_2);
        btn_almacen_3 = findViewById(R.id.almacen_3);
        btn_almacen_4 = findViewById(R.id.almacen_4);

        btn_almacen_1.setOnClickListener(SwitchButton);
        btn_almacen_2.setOnClickListener(SwitchButton);
        btn_almacen_3.setOnClickListener(SwitchButton);
        btn_almacen_4.setOnClickListener(SwitchButton);

        LoadingPanel = findViewById(R.id.loading_panel);

        InventoryLayout = findViewById(R.id.inventory_layout);

        RelativeLayout c_view = findViewById(R.id.view_panel);

        findViewById(R.id.btn_qr).setOnClickListener(v->{
            mScannerView = new ZXingScannerView(this);
            // Register ourselves as a handler for scan results.
            mScannerView.setResultHandler(this);
            // Start camera on resume
            mScannerView.startCamera();
            c_view.addView(mScannerView);
        });

    }

    private void BuscarOrdenCompra() {


        if(et_ordencompra.getText().length() > 0){
            et_ordencompra.setEnabled(false);

            txt_cargando.setTextColor(getColor(android.R.color.black));
            txt_cargando.setText("Buscando...");
            txt_cargando.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> pb_cargando.setVisibility(View.VISIBLE), 50);

            new RestAdapter.Builder().setEndpoint(URL).build().create(askOrdenCompra.class).setData(et_ordencompra.getText().toString(), new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {

                    try {
                        String a = new BufferedReader(new InputStreamReader(response.getBody().in())).readLine();
                        if(a.equals("succes")){
                            txt_cargando.setText("Iniciando Conexión");
                            new Handler().postDelayed(() -> Succes_updateUI(), 1500);
                            et_ordencompra.setEnabled(true);
                        }else{
                            et_ordencompra.setEnabled(true);
                            txt_cargando.setTextColor(getColor(R.color.rojo_etiflex));
                            txt_cargando.setText("Orden de compra inválida");
                            txt_cargando.setVisibility(View.VISIBLE);
                            pb_cargando.setVisibility(View.GONE);
                        }
                    }catch (IOException e){
                        et_ordencompra.setEnabled(true);
                        txt_cargando.setTextColor(getColor(R.color.rojo_etiflex));
                        txt_cargando.setText("Algo salió mal en el sistema, intente nuevamente");
                        txt_cargando.setVisibility(View.VISIBLE);
                        pb_cargando.setVisibility(View.GONE);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    et_ordencompra.setEnabled(true);
                    pb_cargando.setVisibility(View.GONE);
                    txt_cargando.setVisibility(View.VISIBLE);
                    txt_cargando.setTextColor(getColor(R.color.rojo_etiflex));
                    txt_cargando.setText("Error, revise su conexión");
                }
            });
        }else{
            et_ordencompra.setEnabled(true);
            txt_cargando.setText("Por favor, introduzca una orden de compra válida");
            txt_cargando.setTextColor(getColor(R.color.rojo_etiflex));
            txt_cargando.setVisibility(View.VISIBLE);
            pb_cargando.setVisibility(View.GONE);
        }

    }

    private void Succes_updateUI(){
        new Handler().postDelayed(() -> {
            et_ordencompra.animate().translationX(1000).setDuration(1000);
            new Handler().postDelayed(() -> {
                txt_cargando.animate().translationX(1000).setDuration(1000);
                new Handler().postDelayed(() -> {
                    pb_cargando.animate().translationX(1000).setDuration(1000);
                    new Handler().postDelayed(() -> {
                        btn_continuar.animate().translationX(1000).setDuration(1000);
                        new Handler().postDelayed(() -> {
                            InventoryLayout.setVisibility(View.VISIBLE);
                            new Splash().CambiarColorStatusBar(Inventario.this, R.color.rojo_etiflex);
                            findViewById(R.id.inventario_root_layout).setVisibility(View.GONE);

                        }, 900);
                    },100);
                },100);
            },100);
        }, 3000);
    }

    private View.OnClickListener SwitchButton = v -> SetSelected((TextView)v);

    private void SetSelected(TextView txt){
        if(txt.getCurrentTextColor() == getColor(android.R.color.black)){
            txt.setTextColor(getColor(android.R.color.white));
            txt.setBackground(getDrawable(R.drawable.selected_red));
        }else{
            txt.setTextColor(getColor(android.R.color.black));
            txt.setBackground(getDrawable(R.drawable.card_white_round_corners_stroke_gray));
        }
        new Methods().PlayBeep_Button();
    }

    private void DescargarInformacion(Context ctx) {

        Volley.newRequestQueue(ctx).add(new JsonObjectRequest(Request.Method.GET, URL+"/getListaInventario.php?OC="+et_ordencompra.getText().toString(), null, response -> {
            JSONArray json= response.optJSONArray("Data");
            main_list = new ArrayList<>();
            try {
                for(int i=0; i<json.length();i++){
                    model = new ModelInventory();
                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(i);
                    model.setIdProducto(jsonObject.optString("IdEtiqueta"));
                    model.setEPC(jsonObject.optString("IdEtiqueta"));
                    model.setEAN(jsonObject.optString("IdEtiqueta"));
                    model.setNombre(jsonObject.optString("IdEtiqueta"));
                    model.setDescripcion(jsonObject.optString("IdEtiqueta"));
                    model.setPrecio(jsonObject.optString("IdEtiqueta"));
                    model.setAlmacen(jsonObject.optString("IdEtiqueta"));
                    model.setCama(jsonObject.optString("IdEtiqueta"));
                    model.setCaja(jsonObject.optString("IdEtiqueta"));
                    model.setIdProvedor(jsonObject.optString("IdEtiqueta"));

                    main_list.add(model);

                }
            } catch (JSONException | NullPointerException e) {
                Log.e("Validacion","JSON | Null Exception"+e);
            }

        },error -> {
            Log.e("Validacion","Volley error"+error);
        }));
    }

    @Override
    public void handleResult(Result result) {
        et_ordencompra.setText(result.getText());
        mScannerView.stopCamera();
    }
}
