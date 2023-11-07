package uta.fisei.ejercicio04;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String [] words;
    private String  currWords;
    private Random random;
    TextView[] charViwes;
    private LinearLayout wordLayout;
    private LetterAdapter adapter;
    private GridView gridView;
    private int numCorr;
    private int numChars;
    private ImageView[] parts;
    private int sizeParts=6;
    private int currPart;
    private TextView textViewPlayer;
    private List<View> viewPrevious1 = new ArrayList<>();
    private List<View> viewPrevious2 = new ArrayList<>();
    private boolean turn = true;
    int players;
    String letters = "abcdefghijklmnopqrstuvwxyz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();
        words = getResources().getStringArray(R.array.words);
        wordLayout = findViewById(R.id.words);
        gridView = findViewById(R.id.letters);
        textViewPlayer = findViewById(R.id.textViewJugador);

        parts = new ImageView[sizeParts];
        parts[0] = findViewById(R.id.head);
        parts[1] = findViewById(R.id.body);
        parts[2] = findViewById(R.id.armLeft);
        parts[3] = findViewById(R.id.armRight);
        parts[4] = findViewById(R.id.legLeft);
        parts[5] = findViewById(R.id.legRight);

        Bundle bundle = this.getIntent().getExtras();
        players = bundle.getInt("jugadores");

        startGame();
    }

    private void startGame() {
        textViewPlayer.setText("Turno del 1~ Jugador");

        String newWord = words[random.nextInt(words.length)];
        wordLayout.removeAllViews();

        while (newWord.equals(currWords))
            newWord = words[random.nextInt(words.length)];
        currWords = newWord;
        charViwes = new TextView[currWords.length()];
        for (int i =0; i < currWords.length(); i++){
            charViwes[i] = new TextView(this);
            charViwes[i].setText(""+currWords.charAt(i));
            charViwes[i].setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT+600));
            charViwes[i].setGravity(Gravity.CENTER);
            charViwes[i].setBackgroundResource(R.drawable.letter_bg);
            charViwes[i].setTextColor(Color.WHITE);
            wordLayout.addView(charViwes[i]);
        }
        adapter = new LetterAdapter(this);
        gridView.setAdapter(adapter);
        numCorr=0;
        currPart=0;
        numChars= currWords.length();

        for (int i=0;i<sizeParts;i++){
            parts[i].setVisibility(View.INVISIBLE);
        }

    }
    public void letterPressed(View view){
        String letter = ((TextView)view).getText().toString();
        char letterChar = letter.charAt(0);

        letters = letters.replace( String.valueOf(letterChar),"");

        if (turn)
            viewPrevious1.add(view);
        else
            viewPrevious2.add(view);

        boolean correct = false;
        for (int i=0; i < currWords.length();i++){
            if (currWords.charAt(i)==letterChar){
                correct=true;
                numCorr++;
                charViwes[i].setTextColor(Color.BLACK);
            }
        }

        String ganador;
        if (turn)
            ganador = "Jugador 1";
        else
            ganador = "Jugador 2";

        boolean ifPass = false;

        if (correct){
            if (numCorr==numChars){
                disableButtons();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Ganaste "+ganador);
                builder.setMessage("Felicidades!\n\n La respuesta era \n\n"+currWords);
                builder.setPositiveButton("Jugar de nuevo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        startGame();
                    }
                });

                builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.show();
            }
        }else if(currPart < sizeParts){
            parts[currPart].setVisibility(View.VISIBLE);
            currPart++;

            if (turn){
                for (View button : viewPrevious1)
                    button.setEnabled(true);
                for (View button : viewPrevious2)
                    button.setEnabled(false);
                turn = false;
                textViewPlayer.setText("Turno del 2~ Jugador");
                textViewPlayer.setTextColor(Color.RED);
            }else{
                for (View button : viewPrevious2)
                    button.setEnabled(true);
                for (View button : viewPrevious1)
                    button.setEnabled(false);
                turn = true;
                textViewPlayer.setText("Turno del 1~ Jugador");
                textViewPlayer.setTextColor(Color.GREEN);
                if (players == 0)
                    textViewPlayer.setText("Computador eligio "+letter);
                else
                    textViewPlayer.setText("Turno jugador 1");

            }

            if (!turn && players == 0){
                new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        disableButtons();
                    }

                    public void onFinish() {
                        simulateComputerPlay();
                        for (View button : viewPrevious2)
                            button.setEnabled(true);
                        for (View button : viewPrevious1)
                            button.setEnabled(false);
                    }
                }.start();
                ifPass = true;
            }
        }else{
            disableButtons();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Perdiste "+ganador);
            builder.setMessage("Tu perdiste!\n\n La respuesta era \n\n"+currWords);
            builder.setPositiveButton("Jugar de nuevo", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                }
            });

            builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.show();
        }

        if (turn){
            for (View button : viewPrevious2)
                button.setEnabled(true);
            for (View button : viewPrevious1)
                button.setEnabled(false);
        }else{
            for (View button : viewPrevious1)
                button.setEnabled(true);
            for (View button : viewPrevious2)
                button.setEnabled(false);
        }

        if (!turn && players == 0 && !ifPass){
            new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
                    disableButtons();
                }

                public void onFinish() {
                    simulateComputerPlay();
                    for (View button : viewPrevious2)
                        button.setEnabled(true);
                    for (View button : viewPrevious1)
                        button.setEnabled(false);
                    textViewPlayer.setText("Computador eligio "+letter);
                }
            }.start();
        }
    }

    private void simulateComputerPlay() {
        letters= letters.toUpperCase();
        Random random = new Random();
        char computerLetter = letters.charAt(random.nextInt(letters.length()));
        String computerLetterString = String.valueOf(computerLetter);

                disableButtons();
                for (View button : viewPrevious1)
                    button.setEnabled(true);
                for (View button : viewPrevious2)
                    button.setEnabled(false);
                textViewPlayer.setText("Turno del computador");
                textViewPlayer.setTextColor(Color.RED);

                enableButtons();
                for (View button : viewPrevious2)
                    button.setEnabled(true);
                for (View button : viewPrevious1)
                    button.setEnabled(false);
                letterPressedByComputer(computerLetterString);

    }
    private void letterPressedByComputer(String computerLetterString) {
        for (int i = 0; i < gridView.getChildCount(); i++) {
            View view = gridView.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                String letter = textView.getText().toString();
                if (letter.equals(computerLetterString)) {

                            letterPressed(view);
                    break;
                }
            }
        }
    }
    public void disableButtons(){
        for (int i=0;i< gridView.getChildCount();i++){
            gridView.getChildAt(i).setEnabled(false);
        }
    }

    public void enableButtons(){
        for (int i=0;i< gridView.getChildCount();i++){
            gridView.getChildAt(i).setEnabled(true);
        }
    }
}