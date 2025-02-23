package chess;

import java.util.ArrayList;

class Rook extends Piece {
    
    public Rook(boolean isWhite, int rank, int file) {
        super(isWhite, rank, file);

        if (isWhite) {
            pieceType = PieceType.WR;
        } else {
            pieceType = PieceType.BR;
        }
    }
    @Override
    public void print() {
        if (isWhite) {
            System.out.print("wR ");
        } else {
            System.out.print("bR ");
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
