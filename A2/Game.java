import java.util.Stack;

public class Game {
    Board b;
    Stack<String> moveHistory = new Stack<String>();
    Side currentTurn;

    public Game(){
        b = new Board();
        currentTurn = Side.WHITE;
    }

    public static String getName() {
        return "Queen's Game-bit";
    }
    public boolean canMove(int x, int y, int destX, int destY, Side s){
       /* TODO write a method that checks if a piece at coordinates x,y can move to coordinates destX,destY
       Conditions for false:
       - Origin or destination coordinates are outside the board
       - Piece at origin is null or captured
       - Piece at origin is not of the same side as s
            - You can check this using piece.getSide()
       - Piece cannot move to the destination by piece movement rules
            - You should check this using piece.canMove(destX, destY, b)
       - Destination has a piece of the same Side as the player
       - piece must move "through" a piece to get from (x,y) to (destX,destY) (use isVisible())
            - The knight is the except to this rule. The knight can hop "over" pieces, so be sure to check for this.
          */

        // If outside of the board
        if (destX >= 8 || destY >= 8 || destX < 0 || destY < 0) {
            return false;
        }
        if (x >= 8 || y >= 8 || x < 0 || y < 0) {
            return false;
        }

        // If the piece at origin does not exist 
        if (b.get(x, y) == null) {
            return false;
        }

        // If the origin has an ennemy piece
        if (b.get(x, y).getSide() != s) {
            return false;
        }

        // For the piece at (x, y) cannot move to the destination
        if (b.get(x, y).canMove(destX, destY) == false) {
            return false;
        } else {
            // If the piece at destination is of their own colour
            if (b.get(destX, destY) != null) {
                if (b.get(destX, destY).getSide() == s) {
                    return false;
                }
            }
            // If the piece is a knight
            if (b.get(x, y) instanceof Knight) {
                return true;
            }
            if (b.get(x, y) instanceof Pawn) {
                return true;
            }
            if (b.get(x, y) instanceof King || b.get(x, y) instanceof Queen || b.get(x, y) instanceof Bishop || b.get(x, y) instanceof Rook) {
                if (this.isVisible(x, y, destX, destY) == true) {
                    return true;
                }
                return false;
            }
            else {
                return false;
            }
            
        }
    }

    /**
     * This method is provided to you in order to help with canMove().
     *
     * In chess, no piece except the knight can "move through" or "hop over" a piece that's in its way
     *
     * This method checks that there are no pieces along the path from (x,y) to (destX,destY).
     * Note that a "path" is only defined if (x,y) and (destX,destY) are on the same row, column, or diagonal.
     * If the requested path is undefined, the method throws an exception.
     *
     * If the path is defined and no piece is found along the path, the method returns true.
     *
     * Don't worry about how this method works or tests and edge cases for it, we will
     * grade you assuming you keep it exactly as provided and use it as a part of your
     * canMove() method.
     */
    private boolean isVisible(int x, int y, int destX, int destY) {
        int diffX = destX - x;
        int diffY = destY - y;

        boolean validCheck = (diffX == 0 || diffY == 0) || (Math.abs(diffX) == Math.abs(diffY));
        if (!validCheck) {
            try {
                throw new Exception("The 'path' between the squares (" + x + ", " + y + ") and (" + destX + ", " + destY +") is undefined");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Diagonal
        if (Math.abs(diffX) == Math.abs(diffY) && Math.abs(diffX) > 1) {
            //Determine direction of move
            int dirX = diffX > 0 ? 1 : -1;
            int dirY = diffY > 0 ? 1 : -1;
            for (int i = x + dirX, j = y + dirY; i != destX && j != destY; i += dirX, j += dirY) {
                if (b.get(i, j) != null) {
                    return false;
                }
            }
        }

        //Linear
        if ((diffX == 0 && Math.abs(diffY) > 1) || (diffY == 0 && Math.abs(diffX) > 1)) {
            if (diffX == 0) {
                int dirY = diffY > 0 ? 1 : -1;
                for (int j = y + dirY; j != destY; j += dirY) {
                    if (b.get(x, j) != null) {
                        return false;
                    }
                }
            } else {
                int dirX = diffX > 0 ? 1 : -1;
                for (int i = x + dirX; i != destX; i += dirX) {
                    if (b.get(i, y) != null) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void appendCheckToHistory(Side side){
        moveHistory.push(side.toString()+ " is in check");
    }

    private void appendWinToHistory(Side side){
        moveHistory.push(side.toString()+ " has won");
    }

    private void appendMoveToHistory(int x, int y, int destX, int destY, Side side){
        Piece toMove = b.get(x,y);
        Piece toCapture = b.get(destX, destY);
        if(toCapture == null){
            moveHistory.push(side.toString() + toMove.getSymbol() + " at "+ x + ", " + y + " to " + destX + ", " + destY);
        }else{
            moveHistory.push(side.toString() + toMove.getSymbol() + " at " + x + ", " + y + " captures " + toCapture.getSymbol() + " at " + destX + ", " + destY);
        }
    }


    /**
     * This method takes as input the coordinates of the piece to move and of the destination to move to.
     *
     * It returns true if the move is valid and false otherwise.
     *
     * Conditions for a valid move are determined in canMove()
     *
     * Upon a successful move, this method also:
     * - updates the board to reflect the move
     * - tracks the move in game history
     *   - also tracks in history if the move was:
     *     - a capture
     *     - result in check
     * - updates the current player's turn
     *
     *  x The x coordinate of the piece to move
     *  y The y coordinate of the piece to move
     *  destX The x coordinate of the destination to move to
     *  destY The y coordinate of the destination to move to
     */
    public boolean move(int x, int y, int destX, int destY){
        if (x == destX && y == destY) {
            return false;
        }
        if (this.canMove(x, y, destX, destY, currentTurn) == true) {
            // Track game history
            this.appendMoveToHistory(x, y, destX, destY, currentTurn);
            // Move the piece
            b.get(x, y).move(destX, destY);
            // If either or both are in check
            if (this.isInCheck(currentTurn) == true) {
                this.appendCheckToHistory(currentTurn);
            }
            else if (this.isInCheck(Side.negate(currentTurn)) == true) {
                this.appendCheckToHistory(Side.negate(currentTurn));
            }
            // If the king has been captured
            if (b.getKing(Side.BLACK) == null) {
                this.appendWinToHistory(Side.WHITE);
                currentTurn = null;
            }
            if (b.getKing(Side.WHITE) == null) {
                this.appendWinToHistory(Side.BLACK);
                currentTurn = null;
            }
            // Update the turn
            currentTurn = Side.negate(currentTurn);
            return true;
        }
        return false;
    }

    /**
     * Return true if the King of Side side can be captured by any of
     * the opponent's pieces.
     *
     */
    public boolean isInCheck(Side side) {
        if (b.getKing(side) != null) {
            int x = b.getKing(side).x;
            int y = b.getKing(side).y;

            for (Piece i : b.getPieces(Side.negate(side))) {
                if (this.canMove(i.x, i.y, x, y, Side.negate(side))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Ensures that the game is in the exact same state as a new game
     */
    public void reset(){
        while(!moveHistory.empty()){
            System.out.println(moveHistory.pop());
        }
        b.fillBoard();
        currentTurn = Side.WHITE;
    }


    public static void main(String[] args){
        Board b = new Board();
        System.out.println(b);
    }

    /**
     * Return an array of Strings containing every successful move made during the game,
     * and every time a move resulted in check.
     */
    public String[] moveHistory(){
        String[] out = new String[moveHistory.size()];
        for(int i = 0; i < moveHistory.size(); i++){
            out[i] = moveHistory.get(i);
        }
        return out;
    }

    public Side getCurrentTurn(){
        return currentTurn;
    }
}
