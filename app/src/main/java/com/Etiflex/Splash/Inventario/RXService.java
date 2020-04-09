package com.Etiflex.Splash.Inventario;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.Etiflex.Splash.ConnectorManager;
import com.Etiflex.Splash.ROC.ReciboOrdenCompra;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.bean.RXInventoryTag;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static com.Etiflex.Splash.GlobalPreferences.activity;
import static com.Etiflex.Splash.GlobalPreferences.main_list;
import static com.Etiflex.Splash.GlobalPreferences.tag_list;

public class RXService extends IntentService {

    public RXService() {
        super("RXService");
    }

    private int pos = 0, cont = 0;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        new ConnectorManager().Connect(rx);
    }

    RXObserver rx = new RXObserver(){
        @Override
        protected void onInventoryTag(RXInventoryTag tag) {
            super.onInventoryTag(tag);
            for(int i = 0; i < main_list.size(); i++){
                if(main_list.get(i).getEPC().replaceAll(" ","").equals(tag.strEPC.replaceAll(" ",""))){
                    pos = i;
                    new Thread(() -> {
                        main_list.get(pos).setEstado("1");
                        Inventario.adapter.notifyItemChanged(pos);
                    }).run();

                    tag_list.remove(i);
                    cont = main_list.size() - tag_list.size();
                    Inventario.pb_contador.setProgress(cont);
                    Inventario.txt_contador.setText(cont+"/"+main_list.size());

                }
            }

        }
    };

}
