import java.util.Arrays;
import java.util.Objects;

public class Main {
    //init var
    private static int BOARD_SIZE = 3;
    private static final String[][] board = new String[BOARD_SIZE][BOARD_SIZE];

    public static void main(String[] args){
        boolean isPlaying = true;
        final Player player1 = new Player("Player 1","X");
        final Player player2 = new Player("Player 2","O");
        Player[] players = new Player[]{player1, player2};
        boolean namePlayerLoop;
        clearBoard();

        //changing settings upon initialization
        if(InputHelper.getYN("Welcome to tic-tac toe!\nBefore we begin, do you want to set any names for players?")){
            do {
                Player selectedPlayer = players[InputHelper.getRangedInt("Select a Player to change.", 1, 2)-1]; //has to be subtracted by 1 because the indexes start at 0
                selectedPlayer.name = InputHelper.getString("What do you want to change this player's name to?");
                System.out.println("Player name set to "+selectedPlayer.name+".");
                namePlayerLoop = InputHelper.getYN("Do you want to name another player?");
            }while(namePlayerLoop);
        }
        BOARD_SIZE = InputHelper.getPositiveInt("How large would you like your board to be?");

        displayBoard();

        //main gameplay loop
        do{
            setCell(players[0]); //player 1's move
            setCell(players[1]); //player 2's move
            System.out.println(verticalWin(player1));
            //check for win
            //if win, do a YN input for the isplaying bool

        }while(isPlaying);
    }

    private static void clearBoard(){for(int i = 0; i<BOARD_SIZE; i++){board[i] = new String[]{"-", "-", "-"};}}

    private static void displayBoard(){
        System.out.println();
        for(String[] i:board){System.out.println(Arrays.toString(i));}
        System.out.println();
    }

    private static void setCell(Player player){
        //tbh i could just use a regex to ensure the player inputs something along the lines of [X, Y] and then read the R/C using substrings and integer.parseInt but tbh ive already overengineered this program enough
        boolean legal;
        int row ;
        int col;
        do {
            row = InputHelper.getInt(player.name + ", enter move row");
            col = InputHelper.getInt(player.name + ", enter move column");
            legal = legalMoveCheck(row, col);
            if(!legal){System.out.println("Sorry, you entered invalid options. Try again.");}
        }while (!legal);

        board[row-1][col-1] = player.icon; //must be appended by 1 because the indexes are 0-2
        displayBoard();
        winCheck(player);
    }

    private static boolean legalMoveCheck(int row, int col){
        boolean rowCheck = false;
        boolean colCheck = false;
        boolean alreadyExists = true;
        if(row>=1&&row<=BOARD_SIZE){rowCheck=true;}
        if(col>=1&&col<=BOARD_SIZE){colCheck=true;}
        if(!Objects.equals(board[row - 1][col - 1], "-")){alreadyExists = false;}
        return rowCheck&&colCheck&&alreadyExists;
    }

    private static void winCheck(Player player){

    }

    private static boolean horizontalWin(Player player){
        for(String[] i:board){
            if (Arrays.equals(i, new String[]{player.icon, player.icon, player.icon})){
                return true;
            }
        }
        return false;
    }

    private static boolean verticalWin(Player player){
        boolean win = false;
        for(int i = 0; i<BOARD_SIZE;i++) {
            System.out.println(i);
            for (String[] j : board){
                win = Objects.equals(j[i], player.icon);
                if(!win){break;}
            }
        }
        return win;
    }

    private static class Player{
        String name;
        String icon; // x or O

        private Player(String name, String icon){
            this.name = name;
            this.icon = icon;
        }
    }
}
