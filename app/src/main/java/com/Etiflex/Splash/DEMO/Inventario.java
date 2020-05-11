package com.Etiflex.Splash.DEMO;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.Etiflex.Splash.ConnectorManager;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.bean.RXInventoryTag;
import com.uhf.uhf.R;

import java.util.ArrayList;

public class Inventario extends Fragment {

    private ArrayList<String> main_list, tag_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventario, container, false);

        main_list = new ArrayList<>();
        tag_list = new ArrayList<>();

        return view;
    }

    public static RXObserver rx = new RXObserver(){
        @Override
        protected void onInventoryTag(RXInventoryTag tag) {

            if()

        }

        @Override
        protected void onInventoryTagEnd(RXInventoryTag.RXInventoryTagEnd endTag) {
            ConnectorManager.mReader.realTimeInventory((byte) 0xff, (byte) 0x01);
        }
    };

}
