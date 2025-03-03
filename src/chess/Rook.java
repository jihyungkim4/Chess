package chess;

import java.util.ArrayList;

class Rook extends Piece {
    boolean hasMoved = false;

    public Rook(boolean isWhite, int rank, int file) {
        super(isWhite, rank, file);

        if (isWhite) {
            pieceType = PieceType.WR;
        } else {
            pieceType = PieceType.BR;
        }
    }

    boolean checkDir(Coord.Dir dir, Coord target, Board board) {
        Coord dst = currentCoord();
        while (true) {
            dst = dst.step(dir);
            if (dst != null && dst.equals(target)) {
                return true;
            }
            if (dst == null || board.getPiece(dst) != null) {
                break;
            }
        }
        return false;
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
        updatePosition(dest, board);
        hasMoved = true;
        return null;
    }

    @Override
    public boolean canTarget(Board board, Coord target, boolean attackOnly) {
        if (checkDir(Coord.Dir.N, target, board))
            return true;
        if (checkDir(Coord.Dir.E, target, board))
            return true;
        if (checkDir(Coord.Dir.S, target, board))
            return true;
        if (checkDir(Coord.Dir.W, target, board))
            return true;
        return false;
    }

    @Override
    public boolean canMove(Board board) {
        Coord src = currentCoord();
        if (checkMove(src.stepNorth(), board))
            return true;
        if (checkMove(src.stepEast(), board))
            return true;
        if (checkMove(src.stepSouth(), board))
            return true;
        if (checkMove(src.stepWest(), board))
            return true;
        return false;
    }
}
