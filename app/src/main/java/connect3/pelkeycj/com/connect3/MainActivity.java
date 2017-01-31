package connect3.pelkeycj.com.connect3;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import static connect3.pelkeycj.com.connect3.R.id.bottomLeft;

public class MainActivity extends AppCompatActivity {
    Board board = new Board();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    // we need to add an onClick method for each of the columns?
    public void dropIn(View view) {
        ImageView tile = (ImageView) view;

        // ensure player made a valid choice
        // drop chip if valid
        if (!board.dropIfValidMove(tile)) {
            return;
        }

        /*
        board.checkWin();
        board.checkDraw();
        // make AI move (currently random)
        board.setAI();
        board.checkLose();
        board.checkDraw();
        */
    }
}

class Coord {
    private int row;
    private int col;

    Coord(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // is this Coord equal to the given row, col position?
    public boolean isEqual(int row, int col) {
        return this.row == row
                && this.col == col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }
}

class Board {
    int chipPlayer = R.drawable.blue;
    int chipAI = R.drawable.red;
    // 0 represents unplayed position
    int[][] board;

    Board() {
        this.board = new int[3][3];
    }


    // get the coordinates of the clicked tile
    public Coord getTileCoord(ImageView tile) {
        // get coordinates of tile in grid
        switch(tile.getId()) {
            case R.id.topLeft:
                return new Coord(0, 0);
            case R.id.topCenter:
                return new Coord(0, 1);
            case R.id.topRight:
                return new Coord(0, 2);
            case R.id.centerLeft:
                return new Coord(1,0);
            case R.id.centerCenter:
                return new Coord(1,1);
            case R.id.centerRight:
                return new Coord(1,2);
            case R.id.bottomLeft:
                return new Coord(2,0);
            case R.id.bottomCenter:
                return new Coord(2,1);
            default:
                return new Coord(2,2);
        }
    }

    // if this tile is open, drop chip and
    public boolean dropIfValidMove(ImageView tile) {
        Coord tileCoord = this.getTileCoord(tile);
        int row = tileCoord.getRow();
        int col = tileCoord.getCol();

        if (this.board[row][col] == 0) {
            this.board[row][col] = 1; // occupy space
            dropPlayer(tile, chipPlayer);
            return true;
        }

        return false;
    }

    // drop the chip at the given tile
    public void dropPlayer(ImageView tile, int chip) {
        tile.setTranslationY(-1000f);
        tile.setImageResource(chip);
        tile.animate().translationY(1f).setDuration(200).setStartDelay(50);
    }


}


