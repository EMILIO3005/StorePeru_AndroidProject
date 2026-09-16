package com.example.storeperu;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.storeperu.fragments.BuscarFragment;
import com.example.storeperu.fragments.ListarFragment;
import com.example.storeperu.fragments.RegistrarFragment;

public class MainActivity extends AppCompatActivity {

    Button btnListar, btnBuscar, btnRegistrar;

    private void loadUI(){
        btnListar = findViewById(R.id.btnListar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnRegistrar = findViewById(R.id.btnRegistrar);
    }
    private void mostrarListar(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedorFragment, new ListarFragment())
                .commit();
    }
    private void mostrarBuscar(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedorFragment, new BuscarFragment())
                .commit();
    }
    private void mostrarRegistrar(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedorFragment, new RegistrarFragment())
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        loadUI();

        btnListar.setOnClickListener(v ->{mostrarListar();});
        btnBuscar.setOnClickListener(v ->{mostrarBuscar();});
        btnRegistrar.setOnClickListener(v ->{mostrarRegistrar();});

    }
}