import java.util.Arrays;
import java.util.Objects;

public class Main {
    //init var
    private static final int BOARD_SIZE = 3;
    private static final String EMPTY_BOX = "-";
    private static boolean isWon = false;

    public static void main(String[] args){
        boolean isPlaying = true;
        final Player player1 = new Player("Player 1","X");
        final Player player2 = new Player("Player 2","O");
        final String[][] board = new String[BOARD_SIZE][BOARD_SIZE];

        clearBoard(board);
        displayBoard(board);

        //so there was a loop to add names to the players but i deleted it for some reason and idc enough to put it back

        //main gameplay loop
        do{
            isWon = setCell(player1, board); //player 1's move
            isWon = setCell(player2, board); //player 2's move

            //if win, do a YN input for the isplaying bool
            if(isWon){
                isWon = false; //this is set to false immediately because the setcell method relies on the wincon being false
                clearBoard(board);
                isPlaying = InputHelper.getYN("Would you like to play again?");
                if(isPlaying){displayBoard(board);} //this needs to be here to print the board on turn 1 when playing again, since it's usually only called after setting a cell
            }
        }while(isPlaying);
    }

    private static void clearBoard(String[][] board){for(int i = 0; i<BOARD_SIZE; i++){board[i] = new String[]{EMPTY_BOX, EMPTY_BOX, EMPTY_BOX};}}

    private static void displayBoard(String[][] board){
        System.out.println(); //u cant do /n because it's printed using a loop so u gotta do empty println statements which feels weird but whatever
        for(String[] i:board){System.out.println(Arrays.toString(i));}
        System.out.println();
    }

    //this is a boolean bc u need a way to tell the main method the game is over so they can prompt to play again
    private static boolean setCell(Player player, String[][] board){
        //tbh i could just use a regex to ensure the player inputs something along the lines of [X, Y] and then read the R/C using substrings and integer.parseInt but tbh ive already overengineered this program enough
        boolean legal;
        int row ;
        int col;


        //prevents game from still playing if the player already won
        if(!isWon){
            do{
                final String playerIconDisplay = " ("+player.icon+"),"; //this doesnt have to be a var but i dont care it makes it more readable
                row = InputHelper.getRangedInt(player.name + playerIconDisplay +" enter move row",1,3);
                col = InputHelper.getRangedInt(player.name + playerIconDisplay+" enter move column",1,3);
                legal = legalMoveCheck(row, col, board);
                if (!legal) {System.out.println("Sorry, you entered invalid options. Try again.");}
            }while(!legal);

            board[row-1][col-1] = player.icon; //must be appended by 1 because the indexes are 0-2
            displayBoard(board);
            return winCheck(player, board);
        }
        return true; //this should never happen because it will already be returned if isWon is false which it should be
    }

    private static boolean legalMoveCheck(int row, int col, String[][] board){
        boolean rowCheck = false;
        boolean colCheck = false;
        boolean alreadyExists = true;
        if(row>=1&&row<=BOARD_SIZE){rowCheck=true;}
        if(col>=1&&col<=BOARD_SIZE){colCheck=true;}
        if(!Objects.equals(board[row - 1][col - 1], EMPTY_BOX)){alreadyExists = false;}
        return rowCheck&&colCheck&&alreadyExists;
    }

    private static boolean winCheck(Player player, String[][] board){
        boolean horizontal = horizontalWinCheck(player, board);
        boolean vertical = verticalWinCheck(player, board);
        boolean diagonal = diagonalWinCheck(player, board);
        boolean playerWon = horizontal||vertical||diagonal;
        boolean tie = tieCheck(board);

        if(horizontal||vertical||diagonal){System.out.println(player.name+" won!");
        }else if(tie){System.out.println("Tie!");}

        return tie||playerWon;
    }

    private static boolean horizontalWinCheck(Player player, String[][] board){
        for(String[] i:board){if(Arrays.equals(i, new String[]{player.icon, player.icon, player.icon})) {return true;}}
        return false;
    }

    private static boolean verticalWinCheck(Player player, String[][] board){
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

    private static boolean diagonalWinCheck(Player player, String[][] board){
        boolean leftToRight = true;
        boolean rightToLeft = true;

        //diagonal from left to right
        for(int i = 0; i<BOARD_SIZE;i++){
            if(!board[i][i].equals(player.icon)){
                leftToRight = false;
                break;
            }
        }
        //diagonal from right to left
        for(int i = BOARD_SIZE-1; i>0;i--){
            if(!board[i][(BOARD_SIZE-1)-i].equals(player.icon)){
                rightToLeft = false;
                break;
            }
        }
        return rightToLeft||leftToRight;
    }

    private static boolean tieCheck(String[][] board){
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
