package com.example.diariodetreino;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diariodetreino.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TreinoDAO helper;

    private RecyclerView exerciciosRecy;
    private TreinoAdapter adapter;
    private List<TreinoInfo> listaTreinos;
    private final String order = "ASC";

    private AppBarConfiguration appBarConfiguration;

    private final int REQUEST_NEW = 1;
    private final int REQUEST_ALTER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.diariodetreino.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(view -> {

            Intent i = new Intent (MainActivity.this, EditActivity.class);
            i.putExtra("exercicio", new TreinoInfo());
            startActivityForResult(i, REQUEST_NEW);
        });

        helper = new TreinoDAO(this);
        listaTreinos = helper.getList(order);

        exerciciosRecy = findViewById(R.id.exerciciosRecy);
        exerciciosRecy.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        exerciciosRecy.setLayoutManager(llm);

        adapter = new TreinoAdapter(listaTreinos);
        exerciciosRecy.setAdapter(adapter);

        exerciciosRecy.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        abrirOpcoes(listaTreinos.get(position));
                    }
                }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int REQUEST_ALTER = 2;
        if(requestCode == REQUEST_NEW && resultCode == RESULT_OK){
            assert data != null;
            TreinoInfo treinoInfo = data.getParcelableExtra("exercicio");
            helper.inserirExercicio(treinoInfo);
            listaTreinos = helper.getList(order);
            adapter = new TreinoAdapter(listaTreinos);
            exerciciosRecy.setAdapter(adapter);
        } else if (requestCode == REQUEST_ALTER && resultCode == RESULT_OK){
            assert data != null;
            TreinoInfo treinoInfo = data.getParcelableExtra("exercicio");
            helper.alteraExercicio(treinoInfo);
            listaTreinos = helper.getList(order);
            adapter = new TreinoAdapter(listaTreinos);
            exerciciosRecy.setAdapter(adapter);
        }
    }

    private void abrirOpcoes(final TreinoInfo exercicio){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(exercicio.getNome());
        builder.setItems(new CharSequence[]{"Editar", "Excluir"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Intent intent1 = new Intent(MainActivity.this, EditActivity.class);
                                intent1.putExtra("exercicio", exercicio);
                                startActivityForResult(intent1, REQUEST_ALTER);
                                break;
                            case 1:
                                listaTreinos.remove(exercicio);
                                helper.apagarExercicio(exercicio);
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}