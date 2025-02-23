package chess;

import java.util.ArrayList;

class King extends Piece {
    
    public King(boolean isWhite, int rank, int file) {
        super(isWhite, rank, file);

        if (isWhite) {
            pieceType = PieceType.WK;
        } else {
            pieceType = PieceType.BK;
        }
    }
    @Override
    public void print() {
        if (isWhite) {
            System.out.print("wK ");
        } else {
            System.out.print("bK ");
        }
    }
    @Override
    public ReturnPlay.Message move(Board board, Coord dest) {
        return ReturnPlay.Message.ILLEGAL_MOVE;
    }

    @Override
    public boolean canTarget(Board board, Coord coord) {
        return false;
    } 

    @Override
    public boolean canMove(Board board) {
        return false;
    }
}
