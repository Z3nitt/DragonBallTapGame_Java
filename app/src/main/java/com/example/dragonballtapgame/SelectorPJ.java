package com.example.dragonballtapgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SelectorPJ extends AppCompatActivity {
    private String jugador1Pj = null; // Inicializar como null
    private String jugador2Pj = null; // Inicializar como null
    private boolean jugador1Listo = false;
    private boolean jugador2Listo = false; // Nueva variable para saber si el jugador 2 está listo
    private String modoJuego;
    private String dificultad; // Nueva variable para la dificultad
    private MediaPlayer mp; // Reproductor de música

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector_pj);

        // Obtener el modo de juego y la dificultad de la MainActivity
        modoJuego = getIntent().getStringExtra("modoJuego");
        dificultad = getIntent().getStringExtra("dificultad"); // Recibir la dificultad

        // Reproducir música para la selección de personajes
        mp = MediaPlayer.create(this, R.raw.selectorpj);
        mp.setVolume(0.5f, 0.5f);
        mp.setLooping(true);
        mp.start();

        // J1 selección
        ImageView gokuJ1 = findViewById(R.id.gokuPlayer1);
        ImageView vegetaJ1 = findViewById(R.id.vegetaPlayer1);

        gokuJ1.setOnClickListener(view -> seleccionarJugador1("goku", gokuJ1, vegetaJ1));
        vegetaJ1.setOnClickListener(view -> seleccionarJugador1("vegeta", vegetaJ1, gokuJ1));

        // J2 selección
        ImageView gokuJ2 = findViewById(R.id.gokuPlayer2);
        ImageView vegetaJ2 = findViewById(R.id.vegetaPlayer2);

        gokuJ2.setOnClickListener(view -> seleccionarJugador2("goku", gokuJ2, vegetaJ2));
        vegetaJ2.setOnClickListener(view -> seleccionarJugador2("vegeta", vegetaJ2, gokuJ2));

        // J1 LISTO
        Button j1Listo = findViewById(R.id.player1Ready);
        j1Listo.setOnClickListener(view -> {
            if (jugador1Pj == null) {
                Toast.makeText(this, "Error: Debes seleccionar un personaje para el Jugador 1.", Toast.LENGTH_SHORT).show();
                return;
            }
            jugador1Listo = true;
            Log.d("SelectorPJ", "Jugador 1 seleccionado: " + jugador1Pj);
            j1Listo.setBackgroundColor(ContextCompat.getColor(SelectorPJ.this, R.color.gris_azul_trans));
            checkJugadoresListos();
        });

        // J2 LISTO
        Button j2Listo = findViewById(R.id.player2Ready);
        j2Listo.setOnClickListener(view -> {
            if (jugador2Pj == null) {
                Toast.makeText(this, "Error: Debes seleccionar un personaje para el Jugador 2.", Toast.LENGTH_SHORT).show();
                return;
            }
            jugador2Listo = true;
            Log.d("SelectorPJ", "Jugador 2 seleccionado: " + jugador2Pj);
            j2Listo.setBackgroundColor(ContextCompat.getColor(SelectorPJ.this, R.color.gris_azul_trans));
            checkJugadoresListos();
        });
    }

    private void seleccionarJugador1(String personaje, ImageView seleccionado, ImageView noSeleccionado) {
        jugador1Pj = personaje;
        seleccionado.setBackgroundColor(ContextCompat.getColor(SelectorPJ.this, R.color.gris_azul_trans));
        noSeleccionado.setBackgroundResource(R.drawable.borde);
        Log.d("SelectorPJ", "Personaje Jugador 1 seleccionado: " + jugador1Pj);
        jugador1Listo = false; // Resetea el estado de listo si se selecciona un nuevo personaje
    }

    private void seleccionarJugador2(String personaje, ImageView seleccionado, ImageView noSeleccionado) {
        jugador2Pj = personaje;
        seleccionado.setBackgroundColor(ContextCompat.getColor(SelectorPJ.this, R.color.gris_azul_trans));
        noSeleccionado.setBackgroundResource(R.drawable.borde);
        Log.d("SelectorPJ", "Personaje Jugador 2 seleccionado: " + jugador2Pj);
        jugador2Listo = false; // Resetea el estado de listo si se selecciona un nuevo personaje
    }

    private void checkJugadoresListos() {
        if (jugador1Listo && jugador2Listo) {
            Log.d("SelectorPJ", "Jugador 1: " + jugador1Pj);
            Log.d("SelectorPJ", "Jugador 2: " + jugador2Pj);
            Log.d("SelectorPJ", "Dificultad: " + dificultad); // Mostrar dificultad

            // Iniciar la actividad de juego
            Intent intent = new Intent(SelectorPJ.this, Juego.class);
            intent.putExtra("Jugador1Pj", jugador1Pj);
            intent.putExtra("Jugador2Pj", jugador2Pj);
            intent.putExtra("modoJuego", modoJuego);
            intent.putExtra("dificultad", dificultad); // Pasar la dificultad seleccionada
            startActivity(intent);
            detenerMusica();
        } else {
            Log.d("SelectorPJ", "Jugadores no listos. Jugador 1 listo: " + jugador1Listo + ", Jugador 2 listo: " + jugador2Listo);
        }
    }

    private void detenerMusica() {
        if (mp != null) {
            try {
                if (mp.isPlaying()) {
                    mp.stop(); // Detener la música solo si está reproduciéndose
                }
                mp.release(); // Liberar recursos
            } catch (IllegalStateException e) {
                e.printStackTrace(); // Imprimir el error en caso de que ocurra una excepción
            } finally {
                mp = null; // Limpiar la referencia
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detenerMusica();
    }
}


