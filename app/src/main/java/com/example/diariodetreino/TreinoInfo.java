package com.example.diariodetreino;

import android.os.Parcel;
import android.os.Parcelable;

public class TreinoInfo implements Parcelable {

    private String foto = "";
    private String nome = "";
    private String observacao = "";
    private String carga = "";
    private String repeticoes = "";
    private String series = "";

    private Long id = -1L;

    TreinoInfo() {

    }

    private TreinoInfo( Parcel in) {
        String[] data = new String[7];
        in.readStringArray(data);
        setFoto(data[0]);
        setNome(data[1]);
        setObservacao(data[2]);
        setCarga(data[3]);
        setRepeticoes(data[4]);
        setSeries(data[5]);
        setId(Long.parseLong(data[6]));
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getCarga() {
        return carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    public String getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(String repeticoes) {
        this.repeticoes = repeticoes;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                getFoto(), getNome(), getObservacao(), getCarga(), getRepeticoes(), getSeries(), String.valueOf(getId())
        });
    }

    public static final Parcelable.Creator<TreinoInfo> CREATOR= new Parcelable.Creator<TreinoInfo>(){

        @Override
        public TreinoInfo createFromParcel(Parcel parcel) {
            return new TreinoInfo(parcel);
        }

        @Override
        public TreinoInfo[] newArray(int i) {
            return new TreinoInfo[i];
        }
    };
}
