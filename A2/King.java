public class King extends Piece {
    public King(int x, int y, Side side, Board board) {
        super(x, y, side, board);
    }

    @Override
    public boolean canMove(int destX, int destY) {
        // Checking if the destination is exactly one square away.
        if (Math.abs(x - destX) <= 1 && Math.abs(y - destY) <=1) {
            return true;
        }
        // Otherwise, return false.
        return false;
    }

    @Override
    public String getSymbol() {
        return this.getSide() == Side.BLACK ? "K" : "K";
    }
}
