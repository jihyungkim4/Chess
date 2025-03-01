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

    boolean checkDir(Coord.Dir dir, Coord target, Board board) {
        Coord dst = currentCoord();
        while (true) {
            dst = dst.step(dir);
            if (dst != null && dst.equals(target)) {
                return true;
            }
            if (dst == null || (board.getPiece(dst) != null && !board.getPiece(dst).movePending)) {
                break;
            }
        }
        return false;
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
        if (!canTarget(board, dest)) {
            return ReturnPlay.Message.ILLEGAL_MOVE;
        }
        updatePosition(dest, board);
        return null;
    }

    @Override
    public boolean canTarget(Board board, Coord target) {
        if (checkDir(Coord.Dir.NE, target, board)) return true;
        if (checkDir(Coord.Dir.NW, target, board)) return true;
        if (checkDir(Coord.Dir.SE, target, board)) return true;
        if (checkDir(Coord.Dir.SW, target, board)) return true;
        return false;
    } 

    @Override
    public boolean canMove(Board board) {
        Coord src = currentCoord();
        if (checkMove(src.stepNorthEast(), board)) return true;
        if (checkMove(src.stepNorthWest(), board)) return true;
        if (checkMove(src.stepSouthEast(), board)) return true;
        if (checkMove(src.stepSouthWest(), board)) return true;
        return false;
    }
}
