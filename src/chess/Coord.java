package chess;

class Coord {
    int r;
    int f;

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
}

