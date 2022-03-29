package com.example.diariodetreino;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TreinoDAO extends SQLiteOpenHelper {

    private static final int VERSAO = 1;
    private final String TABELA = "Treinos";
    private static final String DATABASE = "DadosTreino";

    public TreinoDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABELA +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                " nome TEXT NOT NULL, " +
                " observacao TEXT, " +
                " carga TEXT, " +
                " repeticoes TEXT, " +
                " series TEXT, " +
                " foto TEXT);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<TreinoInfo> getList(String order){
        List<TreinoInfo> exercicios = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABELA + " ORDER BY nome " +
                order + ";", null);

        while(cursor.moveToNext()){
            TreinoInfo t = new TreinoInfo();

            t.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            t.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            t.setObservacao(cursor.getString(cursor.getColumnIndexOrThrow("observacao")));
            t.setCarga(cursor.getString(cursor.getColumnIndexOrThrow("carga")));
            t.setRepeticoes(cursor.getString(cursor.getColumnIndexOrThrow("repeticoes")));
            t.setSeries(cursor.getString(cursor.getColumnIndexOrThrow("series")));
            t.setFoto(cursor.getString(cursor.getColumnIndexOrThrow("foto")));

            exercicios.add(t);
        }

        cursor.close();

        return exercicios;
    }

    public void inserirExercicio(TreinoInfo t){
        ContentValues values = new ContentValues();

        values.put("nome", t.getNome());
        values.put("observacao", t.getObservacao());
        values.put("carga", t.getCarga());
        values.put("repeticoes", t.getRepeticoes());
        values.put("series", t.getSeries());
        values.put("foto", t.getFoto());

        getWritableDatabase().insert(TABELA, null, values);
    }

    public void alteraExercicio(TreinoInfo t){
        ContentValues values = new ContentValues();

        values.put("id", t.getId());
        values.put("nome", t.getNome());
        values.put("observacao", t.getObservacao());
        values.put("carga", t.getCarga());
        values.put("repeticoes", t.getRepeticoes());
        values.put("series", t.getSeries());
        values.put("foto", t.getFoto());

        String[] idParaSerAlterado = {t.getId().toString()};
        getWritableDatabase().update(TABELA, values, "id=?", idParaSerAlterado);
    }

    public void apagarExercicio(TreinoInfo t){
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {t.getId().toString()};
        db.delete(TABELA, "id=?", args);
    }

}
