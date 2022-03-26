public class Knight extends Piece {
    public Knight(int x, int y, Side side, Board board) {
        super(x, y, side, board);
    }

    @Override
    public boolean canMove(int destX, int destY) {

        if (Math.abs(x - destX) == 1 && Math.abs(y - destY) == 2) {
            return true;
        }
        else if (Math.abs(x - destX) == 2 && Math.abs(y - destY) == 1) {
            return true;
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return this.getSide() == Side.BLACK ? "N" : "N";
    }
}


