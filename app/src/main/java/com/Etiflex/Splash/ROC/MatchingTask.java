package com.Etiflex.Splash.ROC;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MatchingTask extends AsyncTask<Void, Void, String> {

    private ArrayList<String> list_tags_received, list_tags_expected, list_tags_final;
    private ProgressBar pb_procesando;
    private TextView txt_estado;

    public MatchingTask(ArrayList<String> received, ArrayList<String> expected, ProgressBar pb, TextView estado){

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

        Log.e("mlists",list_tags_expected.size()+"-"+list_tags_received.size()+"-"+list_tags_final.size());

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
                break;
            case "OK":
                this.pb_procesando.setVisibility(View.GONE);
                this.txt_estado.setText("FINALIZANDO...");
                break;
            case "FALTAN":
                this.pb_procesando.setVisibility(View.GONE);
                this.txt_estado.setText("ATENCION, FALTAN PRODUCTOS");
                break;
                default:

        }
    }
}
