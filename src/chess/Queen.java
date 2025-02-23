package chess;

import java.util.ArrayList;

class Queen extends Piece {
    
    public Queen(boolean isWhite, int rank, int file) {
        super(isWhite, rank, file);

        if (isWhite) {
            pieceType = PieceType.WQ;
        } else {
            pieceType = PieceType.BQ;
        }
    }
    @Override
    public void print() {
        if (isWhite) {
            System.out.print("wQ ");
        } else {
            System.out.print("bQ ");
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
