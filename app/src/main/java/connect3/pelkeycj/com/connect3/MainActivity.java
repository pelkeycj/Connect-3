package connect3.pelkeycj.com.connect3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    boolean gameOver;
    Player player1 = new Player("Blue", R.drawable.blue);
    Player player2 = new Player("Red", R.drawable.red);
    Player currentPlayer = player1;
    GameBoard board = new GameBoard();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.currentPlayer = player1;
        this.gameOver = false;
    }

    public void playAgain(View view) {
        TextView banner = (TextView) findViewById(R.id.winner_banner);
        banner.setText(null);

        this.board.reset(this);
        this.gameOver = false;
    }

    // increment current player score and update display
    void updatePlayerScore() {
        this.currentPlayer.score++;
        TextView banner = (TextView) findViewById(R.id.winner_banner);

        if (this.currentPlayer.name.equals("Blue")) {
            TextView scoreView = (TextView) findViewById(R.id.score_blue);
            scoreView.setText("Blue: " + this.currentPlayer.score);

            // show winner banner
            banner.setText("Blue won!");

        }
        else {
            TextView scoreView = (TextView) findViewById(R.id.score_red);
            scoreView.setText("Red:  " + this.currentPlayer.score);
            banner.setText("Red won!");
        }
    }


    // we need to add an onClick method for each of the columns?
    public void dropIn(View view) {
        ImageView imgView = (ImageView) view;

        // if game won, cannot make move
        if (this.gameOver) {
            return;
        }

        // ensure player made a valid choice
        // drop chip if valid
        if (!board.dropIfValidMove(imgView, this.currentPlayer)) {
            return;
        }

        if (board.isGameWon()) {
            this.gameOver = true;
            this.updatePlayerScore();
            this.message(R.string.win_text);
        }

        if (board.isGameDraw()) {
            this.gameOver = true;
            this.message(R.string.draw_text);
        }

        if (this.currentPlayer == this.player1) {
            this.currentPlayer = this.player2;
        }
        else {
            this.currentPlayer = this.player1;
        }
    }

    public void message(int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

class Player {
    int chip;
    int score;
    String name;

    Player(String name, int chip, int score) {
        this.name = name;
        this.chip = chip;
        this.score = score;
    }

    Player(String name, int chip) {
        this(name, chip, 0);
    }
}


class Tile {
    private int row;
    private int col;
    private int id;

    Tile(int row, int col, int id) {
        this.row = row;
        this.col = col;
        this.id = id;
    }

    // is this Tile in the given row, col position of the grid?
    boolean isInPosition(int row, int col) {
        return this.row == row
                && this.col == col;
    }

    int getRow() {
        return this.row;
    }

    int getCol() {
        return this.col;
    }

    int getID() {
        return this.id;
    }
}


class TileList {
    Tile[] tileList = new Tile[9];

    TileList() {
        this.tileList[0] = new Tile(0, 0, R.id.topLeft);
        this.tileList[1] = new Tile(0, 1, R.id.topCenter);
        this.tileList[2] = new Tile(0, 2, R.id.topRight);

        this.tileList[3] = new Tile(1, 0, R.id.centerLeft);
        this.tileList[4] = new Tile(1, 1, R.id.centerCenter);
        this.tileList[5] = new Tile(1, 2, R.id.centerRight);

        this.tileList[6] = new Tile(2, 0, R.id.bottomLeft);
        this.tileList[7] = new Tile(2, 1, R.id.bottomCenter);
        this.tileList[8] = new Tile(2, 2, R.id.bottomRight);
    }

    // get tile that matches position
    Tile getTile(int row, int col) {
        for (Tile tile : tileList) {
            if (tile.isInPosition(row, col)) {
                return tile;
            }
        }

        throw new IllegalArgumentException("Illegal position: (" + row + ", " + col + ")" );
    }

    // get tile that matches ID
    Tile getTile(int id) {
        for (Tile tile : tileList) {
            if (tile.getID() == id) {
                return tile;
            }
        }

        throw new IllegalArgumentException("Illegal ID: " + id);
    }

    void resetImg(MainActivity activity) {
        for (Tile tile : tileList) {
            ImageView img = (ImageView) activity.findViewById(tile.getID());
            img.setImageDrawable(null);
        }
    }


}

class GameBoard {
    TileList tileList;
    int[][] board;

    GameBoard () {
        this.board = new int[3][3];
        this.tileList = new TileList();
    }

    // drops the players chip in the given tile if open
    // true if successful
    boolean dropIfValidMove(ImageView view, Player player) {
        Tile tile = this.tileList.getTile(view.getId());
        int row = tile.getRow();
        int col = tile.getCol();

        Log.d("D", "boardval=" + this.board[row][col]);

        if (this.board[row][col] == 0) { // open position
            this.board[row][col] = player.chip;
            this.dropPlayer(view, player.chip);
            return true;
        }
        return false;
    }

    // drop the player at the given imageView
    void dropPlayer(ImageView view, int player) {
        view.setTranslationY(-1000f);
        view.setImageResource(player);
        view.animate().translationY(1f).setDuration(300).setStartDelay(50);
    }


    // has the game been won?
    boolean isGameWon() {
        for (int i = 0; i < 3; i++) {
            // horizontal
            if (this.board[i][0] == this.board[i][1]
                    && this.board[i][1] == this.board[i][2]
                    && this.board[i][2] != 0) {
                return true;
            }

            // vertical
            if (this.board[0][i] == this.board[1][i]
                    && this.board[1][i] == board[2][i]
                    && this.board[2][i] != 0) {
                return true;
            }
        }


        // top-left -> bottom-right diagonal
        if (this.board[0][0] == this.board[1][1]
                && this.board[1][1] == this.board[2][2]
                && this.board[2][2] != 0) {
            return true;
        }

        // bottom-left -> top-right diagonal
        if (this.board[2][0] == this.board[1][1]
                && this.board[1][1] == this.board[0][2]
                && this.board[0][2] != 0) {
            return true;
        }

        return false;
    }

    // is the game a draw?
    boolean isGameDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // reset board tile images and board
    void reset(MainActivity activity) {
        // reset tile images
        this.tileList.resetImg(activity);
        // reset board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.board[i][j] = 0;
            }
        }

    }


}
