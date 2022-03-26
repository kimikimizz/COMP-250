import java.util.ArrayList;
import java.util.List;

public class Board {
    private Piece[][] pieces;

    public Board(){
        fillBoard();
    }

    /**
     * Resets the board to the starting position, and puts all the pieces in the right places.
     */
    public void fillBoard(){
        pieces = new Piece[8][8];        // Empty the board
        fillBoard(true);          // Fill black pieces
        fillBoard(false);         // Fill white pieces
    }

    protected void fillBoard(boolean isBlack){
        int backRank = isBlack ? 0 : 7;
        int pawnRank = isBlack ? 1 : 6;
        Side side = isBlack ? Side.BLACK : Side.WHITE;

        pieces[0][backRank] = new Rook(0, backRank, side, this);
        pieces[7][backRank] = new Rook(7,backRank, side, this);

        pieces[1][backRank] = new Knight(1, backRank, side, this);
        pieces[6][backRank] = new Knight(6, backRank, side, this);

        pieces[2][backRank] = new Bishop(2, backRank, side, this);
        pieces[5][backRank] = new Bishop(5, backRank, side, this);

        pieces[4][backRank] = new King(4,backRank,side, this);
        pieces[3][backRank] = new Queen(3,backRank,side, this);

        for(int i = 0; i < pieces[pawnRank].length; i++){
            pieces[i][pawnRank] = new Pawn(i, pawnRank, side, this);
        }
    }

    public King getKing(Side side){
        for(Piece[] array : pieces){
            for(Piece p : array){
                if(p instanceof King && p.getSide() == side){
                    return (King) p;
                }
            }
        }

        return null;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < pieces.length; i++){
            for(int j = 0; j < pieces[i].length; j++){
                if(pieces[i][j] == null){
                    sb.append(" ");
                }else{
                    sb.append(pieces[i][j].getSymbol());
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public List<Piece> getPieces(Side s){
        ArrayList<Piece> out = new ArrayList<Piece>();
        for(Piece[] array : pieces){
            for(Piece p : array){
                if(p != null && !p.isCaptured() && p.getSide() == s){
                    out.add(p);
                }
            }
        }
        return out;
    }

    public List<Piece> getPieces(){
        ArrayList<Piece> out = new ArrayList<Piece>();
        for(Piece[] array : pieces){
            for(Piece p : array){
                if(p != null && !p.isCaptured()){
                    out.add(p);
                }
            }
        }
        return out;
    }

    public Piece get(int x, int y) {
        return pieces[x][y];
    }

    public void set(int x, int y, Piece p){
        pieces[x][y] = p;
    }
}
