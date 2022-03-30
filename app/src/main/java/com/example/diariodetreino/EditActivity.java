package com.example.diariodetreino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    private TreinoInfo exercicio;
    private View layout;


    private ImageButton foto;
    private EditText nome;
    private EditText obs;
    private EditText carga;
    private EditText repeticoes;
    private EditText series;

    private Integer Volume;
    private TextView ValorCargaTotal;

    private final int CAMERA = 1;
    private final int GALERIA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        exercicio = getIntent().getParcelableExtra("exercicio");
        layout = findViewById(R.id.main_layout);
        Button salvar = findViewById(R.id.buttonSalvar);
        Button calcular = findViewById(R.id.buttonCalcular);

        foto = findViewById(R.id.imageExercicio);
        nome = findViewById(R.id.nomeExercicio);
        obs = findViewById(R.id.obsExercicio);
        carga = findViewById(R.id.cargaExercicio);
        repeticoes = findViewById(R.id.repExercicio);
        series = findViewById(R.id.seriesExercicio);

        nome.setText(exercicio.getNome());
        obs.setText(exercicio.getObservacao());
        carga.setText(exercicio.getCarga());
        repeticoes.setText(exercicio.getRepeticoes());
        series.setText(exercicio.getSeries());

        File imgFile = new File(exercicio.getFoto());
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            foto.setImageBitmap(bitmap);
        }

        foto.setOnClickListener((view -> alertaImagem()));

        salvar.setOnClickListener(view -> {

            exercicio.setNome(nome.getText().toString());
            exercicio.setObservacao(obs.getText().toString());
            exercicio.setCarga(carga.getText().toString());
            exercicio.setRepeticoes(repeticoes.getText().toString());
            exercicio.setSeries(series.getText().toString());

            if (exercicio.getNome().equals("") ||
                    exercicio.getObservacao().equals("") ||
                    exercicio.getCarga().equals("") ||
                    exercicio.getRepeticoes().equals("") ||
                    exercicio.getSeries().equals("")
            ){
                Toast.makeText(EditActivity.this, "Necessario passar todos os dados!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent i = new Intent();
            i.putExtra("exercicio", exercicio);
            setResult(RESULT_OK, i);
            finish();
        });

        ValorCargaTotal = findViewById(R.id.textVolumeTotal);

        calcular.setOnClickListener(view -> {
            if (carga.getText() != null && series.getText() != null && repeticoes.getText() != null){
                Volume = Integer.parseInt(carga.getText().toString()) *
                        Integer.parseInt(series.getText().toString()) *
                        Integer.parseInt(repeticoes.getText().toString());
                ValorCargaTotal.setText(new StringBuilder().append("Volume do exercício: ").append(Volume).append("kg").toString());
            } else {
                Toast.makeText(EditActivity.this, "Erro no cálculo de Volume, verifique os valores!", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void alertaImagem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione a Fonte da Imagem");
        builder.setPositiveButton("Camera", (dialogInterface, i) -> clicaTirarFoto());

        builder.setNegativeButton("Galeria", (dialogInterface, i) -> clicaCarregaImagem());

        builder.create().show();
    }

    private void clicaTirarFoto() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){
            requestCameraPermission();
        } else {
            showCamera();
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)){
            Snackbar.make(layout, "É necessário permitir para utilizar a camera",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", view -> ActivityCompat.requestPermissions(EditActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            CAMERA)).show();

        } else {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                CAMERA);
        }
    }

    private void showCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAMERA);

    }

    private void clicaCarregaImagem() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            requestGaleriaPermission();
        } else {
            showGaleria();
        }
    }

    private void requestGaleriaPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)){

            Snackbar.make(layout, "É necessário permitir para utilizar a galeria!",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", view -> ActivityCompat.requestPermissions(EditActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            GALERIA)).show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    GALERIA);
        }
    }

    private void showGaleria(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALERIA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    clicaTirarFoto();
                }
                break;
            case GALERIA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    clicaCarregaImagem();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED || data == null) {
            return;
        }
        if (requestCode == GALERIA) {
            Uri contentURI = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                exercicio.setFoto(saveImage(bitmap));
                foto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            try {
                exercicio.setFoto(saveImage(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
            foto.setImageBitmap(bitmap);
        }

    }

    private String saveImage(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String IMAGE_DIR = "/FotosContatos";
        File directory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIR);

        boolean isDirectoryCreated= directory.mkdir();

        if (!isDirectoryCreated) {
            throw new IOException("Erro ao criar diretório");
        }

        try {
            File f = new File(directory, Calendar.getInstance().getTimeInMillis() + ".jpg");

            boolean fileCreated = f.createNewFile();

            if (!fileCreated) {
                throw new IOException("Erro ao salvar Imagem");
            }
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());

            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            return f.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}