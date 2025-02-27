package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        if (!canTarget(board, dest)) {
            return ReturnPlay.Message.ILLEGAL_MOVE;
        }
        updatePosition(dest, board);
        return null;
    }

    @Override
    public boolean canTarget(Board board, Coord target) {
        Coord src = currentCoord();
        
        // list of all possible moves
        List<Coord> possibleMoves = Arrays.asList(
            src.step(2, 1), src.step(2, -1),
            src.step(-2, 1), src.step(-2, -1),
            src.step(1, 2), src.step(1, -2),
            src.step(-1, 2), src.step(-1, -2)
        );

        // check if target coord matches any of the possible moves
        for (Coord move : possibleMoves) {
            if (move != null && move.equals(target)) {
                Piece pieceAtTarget = board.getPiece(target);
                if (pieceAtTarget == null || pieceAtTarget.isWhite != this.isWhite) {
                    return true; // move works if its empty or captures an opponent
                }
            }
        }
        return false;
    } 

    @Override
    public boolean canMove(Board board) {
        Coord src = currentCoord();

        // list of all possible moves
        List<Coord> possibleMoves = Arrays.asList(
            src.step(2, 1), src.step(2, -1),
            src.step(-2, 1), src.step(-2, -1),
            src.step(1, 2), src.step(1, -2),
            src.step(-1, 2), src.step(-1, -2)
        );

        for (Coord move : possibleMoves) {
            if (move != null) {
                Piece pieceAtMove = board.getPiece(move);
                if (pieceAtMove == null || pieceAtMove.isWhite != this.isWhite) {
                    return true; // move works
                }
            }
        }

        return false;
    }
}
