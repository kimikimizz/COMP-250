public class Rook extends Piece {
    public Rook(int x, int y, Side side, Board board) {
        super(x, y, side, board);
    }

    @Override
    public boolean canMove(int destX, int destY) {
        // If the destination is on the same rank or on the same file, return true. 
        if (destX == x || destY == y) {
            return true;
        }

        // Else, return false. 
        return false;
    }

    @Override
    public String getSymbol() {
        return this.getSide() == Side.BLACK ? "R" : "R";
        
    }
}