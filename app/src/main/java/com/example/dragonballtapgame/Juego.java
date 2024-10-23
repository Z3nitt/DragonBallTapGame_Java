package com.example.dragonballtapgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class Juego extends AppCompatActivity {
    private int ScoreJ1 = 0;
    private int ScoreJ2 = 0;
    private TextView J1Scoretv;
    private TextView J2Scoretv;
    private MediaPlayer mp;
    private String Jugador1Pj; // Personaje Jugador 1
    private String Jugador2Pj; // Personaje Jugador 2
    private int timeLeft = 10; // Tiempo de juego en segundos
    private String modoJuego;
    private String dificultad;
    private boolean juegoIniciado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        // Inicializar el reproductor de música
        mp = MediaPlayer.create(this, R.raw.cancionjuego);
        mp.setLooping(true); // Repetir música
        mp.start();

        // Obtener los personajes seleccionados
        Intent intent = getIntent();
        Jugador1Pj = intent.getStringExtra("Jugador1Pj"); // Cambiado a mayúscula
        Jugador2Pj = intent.getStringExtra("Jugador2Pj");
        modoJuego = intent.getStringExtra("modoJuego");
        dificultad = intent.getStringExtra("dificultad");

        // Logs para comprobar si los personajes fueron recibidos correctamente
        Log.d("Juego", "Personaje Jugador 1: " + Jugador1Pj);
        Log.d("Juego", "Personaje Jugador 2: " + Jugador2Pj);

        // Verificar si los personajes son nulos
        if (Jugador1Pj == null || Jugador2Pj == null) {
            Toast.makeText(this, "Error: Los personajes no han sido seleccionados.", Toast.LENGTH_SHORT).show();
            Log.e("Juego", "Los personajes no han sido seleccionados.");
            finish(); // Cierra la actividad si hay un error
            return;  // Sal del método para evitar más errores
        }

        // Configurar vistas
        ImageView J1Imagen = findViewById(R.id.player1Image);
        ImageView J2Imagen = findViewById(R.id.player2Image);
        J1Scoretv = findViewById(R.id.player1Score);
        J2Scoretv = findViewById(R.id.player2Score);
        final TextView CuentaAtras = findViewById(R.id.countdownTextView);
        final TextView TiempoRestante = findViewById(R.id.gameTimerTextView);
        CuentaAtras.setVisibility(View.VISIBLE);

        // Cargar gifs de personajes
        cargarPersonajes(J1Imagen, J2Imagen);

        // Cuenta atrás de 3 segundos
        new Handler().postDelayed(new Runnable() {
            int countdown = 3; // Empieza en 3
            @Override
            public void run() {
                CuentaAtras.setText(String.valueOf(countdown));
                countdown--;

                if (countdown >= 0) {
                    new Handler().postDelayed(this, 1000);
                } else {
                    CuentaAtras.setVisibility(View.GONE);
                    empezarJuego();  // Iniciar el juego después de la cuenta atrás
                }
            }
        }, 1000);

        // Evento de clics para el jugador 1 (siempre habilitado)
        findViewById(R.id.player1Layout).setOnClickListener(view -> {
            if (juegoIniciado) {
                ScoreJ1++;
                J1Scoretv.setText(String.valueOf(ScoreJ1));
            }
        });

        // Evento de clics para el jugador 2 (solo habilitado en multijugador)
        if (modoJuego.equals("multiplayer")) {
            findViewById(R.id.player2Layout).setOnClickListener(view -> {
                if (juegoIniciado) {
                    ScoreJ2++;
                    J2Scoretv.setText(String.valueOf(ScoreJ2));
                }
            });
        } else {
            // Si es CPU, desactivar el clic en el layout del jugador 2
            findViewById(R.id.player2Layout).setEnabled(false);
            iniciarIA(); // Iniciar el "tap" automático de la IA
        }
    }

    private void cargarPersonajes(ImageView J1Imagen, ImageView J2Imagen) {
        // Cargar imagen del jugador 1
        if (Jugador1Pj.equals("goku")) {
            Glide.with(this).asGif().load(R.drawable.gokugif).into(J1Imagen);
        } else {
            Glide.with(this).asGif().load(R.drawable.vegetagif).into(J1Imagen);
        }

        // Cargar imagen del jugador 2
        if (Jugador2Pj.equals("goku")) {
            Glide.with(this).asGif().load(R.drawable.gokugif).into(J2Imagen);
        } else {
            Glide.with(this).asGif().load(R.drawable.vegetagif).into(J2Imagen);
        }
    }

    private void empezarJuego() {
        final TextView gameTimerTextView = findViewById(R.id.gameTimerTextView);
        gameTimerTextView.setVisibility(View.VISIBLE);
        juegoIniciado = true;

        if (modoJuego.equals("CPU")) {
            iniciarIA(); // Asegúrate de llamar a iniciarIA aquí
        }

        final Handler gameHandler = new Handler();
        gameHandler.post(new Runnable() {
            @Override
            public void run() {
                gameTimerTextView.setText("Tiempo restante: " + timeLeft);
                timeLeft--;

                if (timeLeft >= 0) {
                    gameHandler.postDelayed(this, 1000);
                } else {
                    gameTimerTextView.setVisibility(View.GONE);
                    acabarJuego();
                }
            }
        });
    }

    private void iniciarIA() {
        final Handler iaHandler = new Handler();
        final int intervaloTaps;

        // Definir el intervalo de taps según la dificultad
        switch (dificultad) {
            case "Fácil":
                intervaloTaps = 200; // Taps muy rápidos en modo fácil
                break;
            case "Normal":
                intervaloTaps = 90;  // Taps rápidos en modo normal
                break;
            case "Dificil":
                intervaloTaps = 50;  // Taps casi instantáneos en modo difícil
                break;
            default:
                intervaloTaps = 100; // Valor por defecto
        }

        iaHandler.post(new Runnable() {
            @Override
            public void run() {
                if (juegoIniciado && timeLeft >= 0) {
                    // La IA hace clic
                    ScoreJ2++;
                    J2Scoretv.setText(String.valueOf(ScoreJ2));

                    // Incrementar la puntuación de la IA con más frecuencia, especialmente si está detrás
                    if (ScoreJ1 > ScoreJ2) {
                        // Si el jugador 1 está ganando, aumentar la velocidad
                        iaHandler.postDelayed(this, intervaloTaps / 2); // Reduce el tiempo de espera a la mitad
                    } else {
                        // Repetir según el intervalo de dificultad
                        iaHandler.postDelayed(this, intervaloTaps);
                    }
                }
            }
        });
    }

    private void acabarJuego() {
        String Ganador;
        String personajeGanador = "";
        boolean isJ1; // Añadido para determinar quién ganó

        // Detener y liberar el reproductor de música
        stopMusic();

        // Determinar el ganador
        if (ScoreJ1 > ScoreJ2) {
            Ganador = "¡Jugador 1 gana!";
            Toast.makeText(Juego.this, "Gana el jugador 1", Toast.LENGTH_LONG).show();
            personajeGanador = Jugador1Pj; // Personaje del jugador humano
            isJ1 = true; // Jugador 1 ganó
        } else if (ScoreJ2 > ScoreJ1) {
            if (modoJuego.equals("CPU")) {
                Ganador = "¡La IA gana!"; // Mensaje para la victoria de la IA
                Toast.makeText(Juego.this, "Gana la CPU", Toast.LENGTH_LONG).show();
            } else {
                Ganador = "¡Jugador 2 gana!"; // Mensaje para el modo multijugador
                Toast.makeText(Juego.this, "Gana el jugador 2", Toast.LENGTH_LONG).show();
            }
            personajeGanador = Jugador2Pj; // Personaje de la IA o Jugador 2
            isJ1 = false; // Jugador 1 no ganó
        } else {
            Ganador = "¡Es un empate!";
            Intent intentEmpate = new Intent(Juego.this, MainActivity.class);
            startActivity(intentEmpate);
            finish(); // Terminar la actividad actual
            return; // Salir del método
        }

        // Lanzar la pantalla de KO con el ganador
        Intent intent = new Intent(Juego.this, KOScreen.class);
        intent.putExtra("ganador", Ganador);
        intent.putExtra("personajeGanador", personajeGanador);
        intent.putExtra("isJ1", isJ1); // Añadir el estado de ganador
        startActivity(intent);
        finish(); // Terminar la actividad actual
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic(); // Asegurarse de detener la música al destruir la actividad
    }
}



