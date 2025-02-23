package chess;

import java.util.ArrayList;

class Knight extends Piece {
    
    public Knight(boolean isWhite, int rank, int file) {
        super(isWhite, rank, file);

        if (isWhite) {
            pieceType = PieceType.WN;
        } else {
            pieceType = PieceType.BN;
        }
    }
    @Override
    public void print() {
        if (isWhite) {
            System.out.print("wN ");
        } else {
            System.out.print("bN ");
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
