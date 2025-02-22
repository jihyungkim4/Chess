package chess;

import java.util.ArrayList;

class Pawn extends Piece {
    
    public Pawn(boolean isWhite, int rank, int file) {
        super(isWhite, rank, file);

        if (isWhite) {
            pieceType = PieceType.WP;
        } else {
            pieceType = PieceType.BP;
        }
    }
    @Override
    public void print() {
        if (isWhite) {
            System.out.print("wP ");
        } else {
            System.out.print("bP ");
        }
    }
    @Override
    public ReturnPlay.Message move(Board board, Coord dest) {
        return ReturnPlay.Message.ILLEGAL_MOVE;
    }
}
