package com.example.dragonballtapgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button BotonJugar;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el botón y la música de fondo
        BotonJugar = findViewById(R.id.btnjugar);
        if (mp == null) {
            mp = MediaPlayer.create(this, R.raw.menuprinc);
            mp.setVolume(0.5f, 0.5f);
            mp.setLooping(true); // Repetir la música
            mp.start();
        }
        // Configurar el evento de clic para el botón de jugar
        BotonJugar.setOnClickListener(view -> mostrarModoJuego());
    }

    private void mostrarModoJuego() {
        // Diálogo para elegir modo de juego
        final String[] modoJuego = {"PVP", "CPU"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona un modo de juego").setItems(modoJuego, (dialog, which) -> {
            if (which == 0) {
                // Selección de PVP
                stopMusic();
                Intent intent = new Intent(MainActivity.this, SelectorPJ.class);
                intent.putExtra("modoJuego", "multiplayer");  // Pasa "multiplayer" al SelectorPJ
                startActivity(intent);
            } else {
                // Selección de CPU
                mostarSelectorDificultad();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Asegurarse de liberar el MediaPlayer si se destruye la actividad
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    private void mostarSelectorDificultad() {
        // Diálogo para seleccionar la dificultad
        final String[] dificultad = {"Fácil", "Normal", "Difícil"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona la Dificultad").setItems(dificultad, (dialog, which) -> {
            // Pasar la dificultad y modo de juego a SelectorPJ
            Intent intent = new Intent(MainActivity.this, SelectorPJ.class);
            intent.putExtra("modoJuego", "CPU"); // Asegúrate de pasar el modo de juego
            intent.putExtra("dificultad", dificultad[which]); // Asegúrate de pasar la dificultad
            stopMusic();
            startActivity(intent);
        });
        builder.create().show();
    }

    private void stopMusic() {
        if (mp != null) {
            try {
                mp.stop();
                mp.release();
                mp = null; // Asegúrate de establecer mp a null después de liberarlo
            } catch (IllegalStateException e) {
                e.printStackTrace(); // Maneja la excepción si se produce
            }
        }
    }
}