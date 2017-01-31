package connect3.pelkeycj.com.connect3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Board board = new Board();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    // we need to add an onClick method for each of the columns?
    public void dropIn(View view) {
        ImageView topLeft = (ImageView) findViewById(R.id.topLeft);
        ImageView counter = (ImageView) view;

        counter.setTranslationY(-1000f);
        counter.setImageResource(R.drawable.blue);
        counter.animate().translationY(1f).setDuration(500).setStartDelay(50);

    }
}

class Board {
    int[][] board = new int[3][3];

    Board() {}


    // determine if there is space in the given column
    public boolean isColAvailable(int col) {
        if (board[0][col] == 0) {
            return true;
        }
        return false;
    }
}


