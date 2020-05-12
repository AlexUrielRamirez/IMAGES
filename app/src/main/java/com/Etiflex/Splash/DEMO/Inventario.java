package com.Etiflex.Splash.DEMO;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Etiflex.Splash.BuscadorEPC.Buscador;
import com.Etiflex.Splash.ConnectorManager;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.bean.RXInventoryTag;
import com.uhf.uhf.R;

import java.util.ArrayList;

public class Inventario extends Fragment {

    public static ArrayList<String> main_list, tag_list;

    public static RecyclerView rv_tags;

    public static TextView PanelMensaje;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventario, container, false);

        PanelMensaje = view.findViewById(R.id.PanelMEnsaje);

        main_list = new ArrayList<>();
        tag_list = new ArrayList<>();

        rv_tags = view.findViewById(R.id.rv_tags);
        rv_tags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv_tags.setHasFixedSize(true);
        rv_adapter adapter = new rv_adapter(tag_list);;
        rv_tags.setAdapter(adapter);
        return view;
    }

    public static RXObserver rx = new RXObserver(){
        @Override
        protected void onInventoryTag(RXInventoryTag tag) {
            if(!tag_list.contains(tag.strEPC))
                tag_list.add(tag.strEPC);
        }

        @Override
        protected void onInventoryTagEnd(RXInventoryTag.RXInventoryTagEnd endTag) {
            ConnectorManager.mReader.realTimeInventory((byte) 0xff, (byte) 0x01);
        }
    };


    public static class rv_adapter extends RecyclerView.Adapter<rv_adapter.ViewHolder> implements View.OnClickListener{

        public rv_adapter(ArrayList<String> lista) {
            this.lista = lista;
        }



        ArrayList<String> lista;
        private View.OnClickListener listener;


        @Override
        public rv_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_single_tag,parent,false);
            view.setOnClickListener(this);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(rv_adapter.ViewHolder holder, int position) {

            holder.EPC.setText(lista.get(position));
            holder.EPC.setOnClickListener(v->{
                Intent i = new Intent(holder.EPC.getContext(), Buscador.class);
                i.putExtra("EPC", lista.get(position));
                holder.EPC.getContext().startActivity(i);
            });

        }

        @Override
        public int getItemCount() {
            return lista.size();
        }

        @Override
        public void onClick(View view) {

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView EPC;
            public ViewHolder(View itemView) {
                super(itemView);
                EPC = itemView.findViewById(R.id.txt_epc);
            }
        }
    }

}
