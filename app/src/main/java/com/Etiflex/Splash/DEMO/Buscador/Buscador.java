package com.Etiflex.Splash.DEMO.Buscador;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.uhf.uhf.R;

public class Buscador extends AppCompatActivity {

    private final int CODE_BAR_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador2);

        this.setTitle("IDEMO Buscador");

        Intent myIntent = new Intent();
        myIntent.setClassName("com.etiflex.sdl", "com.zebra.sdl.SDLguiActivity");
        startActivityForResult(myIntent, CODE_BAR_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case CODE_BAR_REQUEST_CODE:
                Log.e("Buscdr", data.getData()+"");
                break;
            default:
                Toast.makeText(this, "Algo salió mal", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
