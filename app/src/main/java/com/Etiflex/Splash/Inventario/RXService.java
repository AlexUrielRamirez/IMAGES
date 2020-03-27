package com.Etiflex.Splash.Inventario;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.Etiflex.Splash.ConnectorManager;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.bean.RXInventoryTag;

import static com.Etiflex.Splash.GlobalPreferences.main_list;
import static com.Etiflex.Splash.GlobalPreferences.tag_list;

public class RXService extends IntentService {

    public RXService() {
        super("RXService");
    }

    private int pos = 0;

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
                    int k = main_list.size() - tag_list.size();
                    Inventario.pb_contador.setProgress(k);
                    Inventario.txt_contador.setText(k+"/"+main_list.size());

                }
            }
        }
    };

}
