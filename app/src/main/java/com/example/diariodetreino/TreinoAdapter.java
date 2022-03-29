package com.example.diariodetreino;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class TreinoAdapter extends RecyclerView.Adapter<TreinoAdapter.ContactViewHolder>{

    private final List<TreinoInfo> listaExercicios;

    TreinoAdapter(List<TreinoInfo> lista){
        listaExercicios = lista;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.celula_treino, parent, false);
        return new ContactViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        int _volume;
        TreinoInfo c = listaExercicios.get(position);
        holder.nome.setText("Exercício: "+c.getNome());
        holder.Obs.setText("Obs: "+c.getObservacao());
        holder.carga.setText("Carga: "+c.getCarga()+"Kg");
        holder.repeticoes.setText("Rep: "+c.getRepeticoes());
        holder.series.setText("Séries: "+ c.getSeries());
        _volume = Integer.parseInt(c.getCarga()) * Integer.parseInt(c.getRepeticoes()) * Integer.parseInt(c.getSeries());
        holder.volume.setText("Volume total do Exercício: "+ _volume);
        File imgFile = new File(c.getFoto());
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.foto.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return listaExercicios.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        TextView nome;
        TextView Obs;
        TextView carga;
        TextView repeticoes;
        TextView series;
        TextView volume;



        ContactViewHolder(View v){
            super(v);
            foto = v.findViewById(R.id.imageExercicio);
            nome = v.findViewById(R.id.textoNome);
            Obs = v.findViewById(R.id.textoObs);
            carga = v.findViewById(R.id.textoCarga);
            repeticoes = v.findViewById(R.id.textoRepeticoes);
            series = v.findViewById(R.id.textoSeries);
            volume = v.findViewById(R.id.textoVolume);
        }

    }
}
