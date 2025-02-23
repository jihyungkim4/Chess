package chess;

import java.util.ArrayList;

class Bishop extends Piece {
    
    public Bishop(boolean isWhite, int rank, int file) {
        super(isWhite, rank, file);

        if (isWhite) {
            pieceType = PieceType.WB;
        } else {
            pieceType = PieceType.BB;
        }
    }
    @Override
    public void print() {
        if (isWhite) {
            System.out.print("wB ");
        } else {
            System.out.print("bB ");
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
