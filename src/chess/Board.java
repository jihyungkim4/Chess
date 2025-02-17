package chess;

import java.util.ArrayList;

class Board {	
	ArrayList<Piece> piecesOnBoard;
	
    public Board() {
        piecesOnBoard = new ArrayList<Piece>();

        piecesOnBoard.add(new Pawn(true, 2, 1));
        piecesOnBoard.add(new Pawn(true, 2, 2));
        piecesOnBoard.add(new Pawn(true, 2, 3));
        piecesOnBoard.add(new Pawn(true, 2, 4));
        piecesOnBoard.add(new Pawn(true, 2, 5));
        piecesOnBoard.add(new Pawn(true, 2, 6));
        piecesOnBoard.add(new Pawn(true, 2, 7));
        piecesOnBoard.add(new Pawn(true, 2, 8));
       
        piecesOnBoard.add(new Pawn(false, 7, 1));
        piecesOnBoard.add(new Pawn(false, 7, 2));
        piecesOnBoard.add(new Pawn(false, 7, 3));
        piecesOnBoard.add(new Pawn(false, 7, 4));
        piecesOnBoard.add(new Pawn(false, 7, 5));
        piecesOnBoard.add(new Pawn(false, 7, 6));
        piecesOnBoard.add(new Pawn(false, 7, 7));
        piecesOnBoard.add(new Pawn(false, 7, 8));
                
    }

    public Piece getPiece(int rank, int file) {   
        for (Piece piece : piecesOnBoard) {
            if (piece.locationEquals(rank, file)) {
                return piece;
            }                
        } 
        return null;
    }

    public ReturnPlay play(String move) {
        return null;
    }
    
    public void print() {
        boolean isWhite = true;
        for (int r = 8; r > 0; r--) {
            for (int f = 1; f < 9; f++) {
                Piece piece = getPiece(r, f);
                if (piece == null) {
                    if (isWhite) {
                       System.out.print("   ");
                    } else {
                       System.out.print("## ");
                    }
                } else {
                    piece.print(); 
                }
                isWhite = !isWhite;
            }
            isWhite = !isWhite;
            System.out.print(" " + Integer.toString(r) + "\n");
        }
        for (int f = 1; f < 9; f++) {
            System.out.print(" " + Piece.mapIntToFile(f) + " ");
        }
        System.out.println();
    }
    
}
