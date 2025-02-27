package chess;

class Coord {
    int r;
    int f;

    enum Dir {

        N, NE, E, SE, S, SW, W, NW
    }

    public Coord(int r, int f) {
        this.r = r;
        this.f = f;
    }

    public static Coord parse(String position) {
        // if anything illegal return null
        // must be two letters
        if (position.length() != 2) {
            return null;
        }
        char cf = position.charAt(0);  
        char cr = position.charAt(1);

        try { 
            // convert 
            int file = ReturnPiece.PieceFile.valueOf(String.valueOf(cf)).ordinal();          
            int rank = Integer.parseInt(String.valueOf(cr)) - 1;
            
            if (rank < 0 || rank > 7) {
                return null;
            }

            if (file < 0 || file > 7) {
                return null;
            }

            return new Coord(rank, file);

        } catch (Exception e) {
            return null;          
        }
    }

    static private Coord makeCoordIfValid(int r, int f) {
        if (f >= 8 || f < 0 || r >= 8 || r < 0) {
            return null;
        } else {
            return new Coord(r, f);
        }
    }

    Coord stepNorth() {
        return makeCoordIfValid(r + 1, f);
    }

    Coord stepEast() {
        return makeCoordIfValid(r, f + 1);
    }

    Coord stepWest() {
        return makeCoordIfValid(r, f - 1);
    }

    Coord stepSouth() {
        return makeCoordIfValid(r - 1, f);
    }

    Coord stepNorthEast() {
        return makeCoordIfValid(r + 1, f + 1);
    }

    Coord stepNorthWest() {
        return makeCoordIfValid(r + 1, f - 1);
    }

    Coord stepSouthEast() {
        return makeCoordIfValid(r - 1, f + 1);
    }

    Coord stepSouthWest() {
        return makeCoordIfValid(r - 1, f - 1);
    }

    Coord step(int rankOffset, int fileOffset) {
        return makeCoordIfValid(r + rankOffset, f + fileOffset);
    }

    boolean equals(Coord other) {
        if (other == null) {
            return false;
        }
        return (r == other.r && f == other.f);
    }

    Coord step(Dir dir) {
        switch (dir) {
            case N: return stepNorth();
            case NE: return stepNorthEast();
            case E: return stepEast();
            case SE: return stepSouthEast();
            case S: return stepSouth();
            case SW: return stepSouthWest();
            case W: return stepWest();
            case NW: return stepNorthWest();
        }
        return null;
    }
}

