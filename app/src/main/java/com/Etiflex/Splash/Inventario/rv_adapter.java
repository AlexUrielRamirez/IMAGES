package com.Etiflex.Splash.Inventario;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.Etiflex.Splash.ROC.ModelInventory;
import com.uhf.uhf.R;

import java.util.ArrayList;

public class rv_adapter extends RecyclerView.Adapter<rv_adapter.ViewHolder> implements View.OnClickListener{

    public rv_adapter(ArrayList<ModelInventory> lista) {
        this.lista = lista;
    }

    ArrayList<ModelInventory> lista;
    private View.OnClickListener listener;


    @Override
    public rv_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_inventario,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(rv_adapter.ViewHolder holder, int position) {
        holder.Nombre.setText(lista.get(position).getNombre());
        holder.Nombre.setOnClickListener(view -> {

        });

        if(lista.get(position).getEstado().equals("1")){
            holder.ic.setImageResource(R.drawable.done_verde);
        }else{
            holder.ic.setImageResource(R.drawable.interrogacion_negro);
        }

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Nombre;
        ImageView ic;
        public ViewHolder(View itemView) {
            super(itemView);
            Nombre = itemView.findViewById(R.id.Nombre);
            ic = itemView.findViewById(R.id.ic);
        }
    }
}

