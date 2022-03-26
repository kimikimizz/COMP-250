public class Bishop extends Piece {
    public Bishop(int x, int y, Side side, Board board) {
        super(x, y, side, board);
    }

    @Override
    public boolean canMove(int destX, int destY) {
        // Check if the destination square is on the diagonal
        if (Math.abs(x - destX) == Math.abs(y - destY)) {
            return true;
        }
        // Otherwise, return false.
        return false;
    }

    @Override
    public String getSymbol() {
        return this.getSide() == Side.BLACK ? "B" : "B"; 
    }
}
