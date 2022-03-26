public class Queen extends Piece {
    public Queen(int x, int y, Side side, Board board) {
        super(x, y, side, board);
    }

    @Override
    public boolean canMove(int destX, int destY) {

        // If the destination is on the same rank or on the same file, return true. 
        if (destX == x || destY == y) {
            return true;
        }

        // Check if the destination square is on the diagonal
        else if (Math.abs(x - destX) == Math.abs(y - destY)) {
            return true;
        }
        // Otherwise, return false.
        return false;
    }

    @Override
    public String getSymbol() {
        return this.getSide() == Side.BLACK ? "Q" : "Q";
    }
}