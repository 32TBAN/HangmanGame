package uta.fisei.ejercicio04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ModoJuego extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_juego);
    }

    public void onCLickOnePlayer(View view){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("jugadores",0);
        startActivity(intent);
    }
    public void onCLickTwoPlayer(View view){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("jugadores",1);
        startActivity(intent);
    }
}