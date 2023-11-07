package uta.fisei.ejercicio04;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class LetterAdapter extends BaseAdapter {
    private String[] letters;
    private LayoutInflater letterInf;
    public LetterAdapter(Context context){
        letters = new String[26];
        for (int i=0;i<(letters.length);i++){
            letters[i] = ""+(char)('A'+i);
        }
        letterInf = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return letters.length;
    }

    @Override
    public Object getItem(int i) {
        return letters[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Button btnLetter;
        if (view == null){
            btnLetter = (Button) letterInf.inflate(R.layout.letter,viewGroup,false);
        }else {
            btnLetter = (Button) view;
        }
        btnLetter.setText(letters[i]);
        return btnLetter;
    }

}
