package com.example.dragonballtapgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class KOScreen extends AppCompatActivity {
    private ImageView koGifImageView;
    private TextView winnerTextView;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koscreen);
        int winner = getIntent().getIntExtra("personajeGanador", 1);

        koGifImageView = findViewById(R.id.koGifImageView);
        winnerTextView = findViewById(R.id.winnerTextView);

        String personajeGanador = getIntent().getStringExtra("personajeGanador");
        boolean isJ1 = getIntent().getBooleanExtra("isJ1", true);

        // Modificación para mostrar el mensaje correcto
        String winnerMessage = isJ1 ? "¡Jugador 1 gana!" : "¡Jugador 2 gana!";
        winnerTextView.setText(winnerMessage);
        winnerTextView.setVisibility(View.VISIBLE);

        if (personajeGanador.equals("goku")) {
            Glide.with(this).asGif().load(R.drawable.gokuwin).into(koGifImageView); // GIF para Goku
            mediaPlayer = MediaPlayer.create(this, R.raw.audiogoku);
            mediaPlayer.setVolume(1.0f,1.0f);
            mediaPlayer.start();
        } else if (personajeGanador.equals("vegeta")) {
            Glide.with(this).asGif().load(R.drawable.vegetawin).into(koGifImageView); // GIF para Vegeta
            mediaPlayer = MediaPlayer.create(this, R.raw.audiovegeta);
            mediaPlayer.setVolume(0.8f,0.8f);
            mediaPlayer.start();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Detener y liberar el MediaPlayer
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                // Iniciar la MainActivity
                Intent intent = new Intent(KOScreen.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finalizar esta actividad para que no pueda volver atrás
            }
        }, 10000); // 10 segundos (10000 milisegundos)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Asegurarse de liberar el MediaPlayer si se destruye la actividad
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}