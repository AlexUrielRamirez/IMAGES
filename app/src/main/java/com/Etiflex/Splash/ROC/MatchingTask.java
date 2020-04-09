package com.Etiflex.Splash.ROC;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class MatchingTask extends AsyncTask<Void, Void, String> {

    private ArrayList<String> list_tags_received, list_tags_expected, list_tags_final;
    private ProgressBar pb_procesando;
    private TextView txt_estado;
    private Context context;

    public MatchingTask(Context ctx, ArrayList<String> received, ArrayList<String> expected, ProgressBar pb, TextView estado){
        this.context = ctx;
        this.list_tags_final = new ArrayList<>();
        this.list_tags_received = received;
        this.list_tags_expected = expected;
        this.pb_procesando = pb;
        this.txt_estado = estado;

    }

    @Override
    protected String doInBackground(Void... voids) {
        for(int position = 0; position < list_tags_received.size(); position++){
            for(int i = 0; i < list_tags_expected.size(); i++){
                if(list_tags_expected.get(i).equals(list_tags_received.get(position)))
                    list_tags_final.add(list_tags_expected.get(i));
            }
        }

        if(list_tags_expected.size() == list_tags_received.size())
            return "OK";
        else if(list_tags_expected.size() > list_tags_received.size())
            return "FALTAN";
        else
            return "SOBRAN";
    }

    @Override
    protected void onPostExecute(String str) {
        super.onPostExecute(str);

        switch (str){
            case "SOBRAN":
                this.pb_procesando.setVisibility(View.GONE);
                this.txt_estado.setText("ATENCION, HAY PRODUCTO SOBRANTES");
                new AlertDialog.Builder(context)
                        .setTitle("ATENCIÓN")
                        .setMessage("Se contabilizaron más productos de los esperados.\nSeleccione \"Aceptar\" para continuar o \"REINTENTAR\" para reiniciar el escáner.")
                        .setPositiveButton("ACEPTAR", (dialog, which) ->{
                            context.startActivity(new Intent(context, ReciboOrdenCompra.class));
                            ((Activity) context).finish();
                        })
                        .setNegativeButton("REINTENTAR", (dialog, which) ->{
                            context.startActivity(new Intent(context, ReciboOrdenCompra.class));
                            ((Activity) context).finish();
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case "OK":
                this.pb_procesando.setVisibility(View.GONE);
                this.txt_estado.setText("FINALIZANDO...");
                new AlertDialog.Builder(context)
                        .setTitle("FINALIZAR")
                        .setMessage("Se contabilizaron todos los productos, presione \"Aceptar\" para continuar.")
                        .setPositiveButton("ACEPTAR", (dialog, which) -> {
                            context.startActivity(new Intent(context, ReciboOrdenCompra.class));
                            ((Activity) context).finish();
                        })
                        .setIcon(android.R.drawable.ic_menu_add)
                        .show();
                break;
            case "FALTAN":
                this.pb_procesando.setVisibility(View.GONE);
                this.txt_estado.setText("ATENCION, FALTAN PRODUCTOS");
                new AlertDialog.Builder(context)
                        .setTitle("ATENCIÓN")
                        .setMessage("No se contabilizaron lo productos esperados\nPor favor, asegure su contenido y vuelva a intentar.")
                        .setPositiveButton("ACEPTAR", (dialog, which) ->{
                            context.startActivity(new Intent(context, ReciboOrdenCompra.class));
                            ((Activity) context).finish();
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
                default:

        }
    }
}
