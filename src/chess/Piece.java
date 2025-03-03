package chess;

abstract class Piece extends ReturnPiece implements Cloneable {
    boolean isWhite;

    public static PieceFile mapIntToFile(int file) {
        if (file < 1 || file > 8) {
            throw new IllegalArgumentException("File must be between 1 and 8");
        }
        return PieceFile.values()[file - 1];
    }

    public Piece(boolean isWhite, int rank, int file) {
        this.isWhite = isWhite;
        if (rank < 1 || rank > 8) {
            throw new IllegalArgumentException("Rank must be between 1 and 8");
        }
        pieceRank = rank;
        pieceFile = mapIntToFile(file);
    }

    boolean locationEquals(int rank, int file) {
        return pieceRank == rank && pieceFile == mapIntToFile(file);
    }

    Chess.Player getPlayer() {
        if (isWhite) {
            return Chess.Player.white;
        }
        return Chess.Player.black;
    }

    Coord currentCoord() {
        return new Coord(pieceRank - 1, pieceFile.ordinal());
    }

    boolean checkMove(Coord dst, Board board) {
        if (dst != null) {
            Piece p = board.getPiece(dst);
            if (p == null || p.getPlayer() != getPlayer()) { // can either take piece or no piece on destination
                return true;
            }
        }
        return false;
    }

    void updatePosition(Coord coord, Board board) {
        Piece targetPiece = board.getPiece(coord);
        if (targetPiece != null) {
            // remove piece
            board.removePiece(coord);
            board.piecesOnBoard.remove(targetPiece);
        }
        board.removePiece(currentCoord());
        pieceRank = coord.r + 1;
        pieceFile = mapIntToFile(coord.f + 1);
        board.putPiece(currentCoord(), this);
    }

    public abstract void print();

    // moves the piece to the specified coordinates returning the result
    // of the move and the new board state. In case of valid captures
    // opponent pieces are removed.
    public abstract ReturnPlay.Message move(Board board, Coord dest);

    // returns true if a piece can attack or move to the specified square,
    // attackOnly is true if only attacking moves are to be considered
    // attackOnly is false when capture or non-capture moves should be
    // considered. Any pieces of the opponent with "movePending" are to
    // be ignored as if they did not exist.
    public abstract boolean canTarget(Board board, Coord coord, boolean attackOnly);

    // return true if a piece can make any legal move, (do not consider safety of
    // own king)
    public abstract boolean canMove(Board board);

    @Override
    public Piece clone() throws CloneNotSupportedException {
        return (Piece) super.clone();
    }
}
