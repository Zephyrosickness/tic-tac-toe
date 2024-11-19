import java.util.Arrays;
import java.util.Objects;

public class Main {
    //init var
    private static final int BOARD_SIZE = 3;
    private static final String[][] board = new String[BOARD_SIZE][BOARD_SIZE];
    private static final String EMPTY_BOX = "-";
    private static boolean isWon = false;

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

        displayBoard();

        //main gameplay loop
        do{
            setCell(players[0]); //player 1's move
            setCell(players[1]); //player 2's move

            //if win, do a YN input for the isplaying bool
            if(isWon){
                isPlaying = InputHelper.getYN("Would you like to play again?");
            }

        }while(isPlaying);
    }

    private static void clearBoard(){for(int i = 0; i<BOARD_SIZE; i++){board[i] = new String[]{EMPTY_BOX, EMPTY_BOX, EMPTY_BOX};}}

    private static void displayBoard(){
        System.out.println();
        for(String[] i:board){System.out.println(Arrays.toString(i));}
        System.out.println();
    }

    //this is a boolean bc u need a way to tell the main method the game is over so they can prompt to play again
    private static void setCell(Player player){
        //tbh i could just use a regex to ensure the player inputs something along the lines of [X, Y] and then read the R/C using substrings and integer.parseInt but tbh ive already overengineered this program enough
        boolean legal;
        int row ;
        int col;
        
        //prevents game from still playing if the player already won
        if(!isWon){
            do {
                row = InputHelper.getRangedInt(player.name + ", enter move row",1,3);
                col = InputHelper.getRangedInt(player.name + ", enter move column",1,3);
                legal = legalMoveCheck(row, col);
                if (!legal) {System.out.println("Sorry, you entered invalid options. Try again.");}
            }while(!legal);

            board[row-1][col-1] = player.icon; //must be appended by 1 because the indexes are 0-2
            displayBoard();
            isWon = winCheck(player);
        }
    }

    private static boolean legalMoveCheck(int row, int col){
        boolean rowCheck = false;
        boolean colCheck = false;
        boolean alreadyExists = true;
        if(row>=1&&row<=BOARD_SIZE){rowCheck=true;}
        if(col>=1&&col<=BOARD_SIZE){colCheck=true;}
        if(!Objects.equals(board[row - 1][col - 1], EMPTY_BOX)){alreadyExists = false;}
        return rowCheck&&colCheck&&alreadyExists;
    }

    private static boolean winCheck(Player player){
        boolean horizontal = horizontalWinCheck(player);
        boolean vertical = verticalWinCheck(player);
        boolean diagonal = diagonalWinCheck(player);
        boolean playerWon = horizontal||vertical||diagonal;
        boolean tie = tieCheck();

        if(horizontal||vertical||diagonal){System.out.println(player.name+" won!");
        }else if(tie){System.out.println("Tie!");}

        return tie||playerWon;
    }

    //checks for horizontal win
    private static boolean horizontalWinCheck(Player player){
        for(String[] i:board){if (Arrays.equals(i, new String[]{player.icon, player.icon, player.icon})){return true;}}
        return false;
    }

    //checks for vertical win
    private static boolean verticalWinCheck(Player player){
        for(int col = 0; col<BOARD_SIZE; col++){
            boolean win = true;
            for(String[] row : board){
                if(!row[col].equals(player.icon)){
                    win = false;
                    break;
                }
            }
            if(win){return true;}
        }
        return false;
    }

    private static boolean diagonalWinCheck(Player player){
        boolean leftToRight = true;
        boolean rightToLeft = true;
        //checks if diagonal from left to right
        for(int i = 0; i<BOARD_SIZE;i++){
            if(!board[i][i].equals(player.icon)){
                leftToRight = false;
                break;
            }
        }
        //checks if diagonal from right to left
        for(int i = BOARD_SIZE-1; i>0;i--){
            if(!board[i][(BOARD_SIZE-1)-i].equals(player.icon)){
                rightToLeft = false;
                break;
            }
        }
        return rightToLeft||leftToRight;
    }

    private static boolean tieCheck(){
        boolean isTied = true;

        for(String[] row:board){
            for(String col:row){
                if(col.equals(EMPTY_BOX)){
                    isTied = false;
                    break;
                }
            }
        }
        return isTied;
    }

    private static class Player{
        String name;
        String icon; // X or O

        private Player(String name, String icon){
            this.name = name;
            this.icon = icon;
        }
    }
}
