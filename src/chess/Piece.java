package chess;

abstract class Piece extends ReturnPiece {
    boolean isWhite;
    // canTarget needs to ignore pieces with this flag set to true
    boolean movePending;

    public static PieceFile mapIntToFile(int file) {
        if (file < 1 || file > 8) {
            throw new IllegalArgumentException("File must be between 1 and 8");
        }
        return PieceFile.values()[file - 1];  
    }

    public Piece(boolean isWhite, int rank, int file) {
        this.isWhite = isWhite;
        this.movePending = false;
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

    public abstract void print();

    public abstract ReturnPlay.Message move(Board board, Coord dest);

    public abstract boolean canTarget(Board board, Coord coord); 

    public abstract boolean canMove(Board board); 
}

