public class Pawn extends Piece {
    public Pawn(int x, int y, Side side, Board board) {
        super(x, y, side, board);
    }

    @Override
    public boolean canMove(int destX, int destY) {
        // Checking if the pawn is black
        if (this.getSide() == Side.BLACK) {
            // Checking if advancing on the same file
            if (destX == x) {
                // Checking if advancing by one square
                if (destY - y == 1) {
                    if (board.get(destX, destY) == null){
                        return true;
                    }
                }
                // Checking if advancing by two squares for a second rank pawn
                if (destY - y == 2 && y == 1) {
                    if (board.get(destX, destY) == null && board.get(destX, y + 1) == null){
                        return true;
                    }
                }
            }
            // Checking if capturing a piece on the diagonal
            else if (Math.abs(destX - x) == 1 && destY - y == 1 && board.get(destX, destY) != null) {
                // Checking if the piece is an ennemy piece
                if (board.get(destX, destY).getSide() == Side.WHITE) {
                    return true;
                }
            }
        }
        else if (this.getSide() == Side.WHITE) {
            // Checking if advancing on the same file
            if (destX == x) {

                // Checking if advancing by one square
                if (destY - y == -1) {
                    if (board.get(destX, destY) == null){
                        return true;
                    }
                }
                // Checking if advancing by two squares for a second rank pawn
                if (destY - y == -2 && y == 6) {
                    if (board.get(destX, destY) == null && board.get(destX, y - 1) == null){
                        return true;
                    }
                }
            }
            // Checking if capturing a piece on the diagonal
            else if (Math.abs(destX - x) == 1 && destY - y == -1 && board.get(destX, destY) != null) {
                // Checking if the piece is an ennemy piece
                if (board.get(destX, destY).getSide() == Side.BLACK) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return this.getSide() == Side.BLACK ? "P" : "P";
    }
}
