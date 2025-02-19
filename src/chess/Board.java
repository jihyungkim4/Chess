package chess;

import java.util.ArrayList;

class Board {	
	ArrayList<Piece> piecesOnBoard;
    Piece[][] board = new Piece[8][8];
    Chess.Player nextPlayer = Chess.Player.white;
	
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

    private void updateBoard() {
        // clear board before update
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board[x][y] = null;               
            }
        }
        // place remaining pieces on the board              
        for (Piece piece : piecesOnBoard) {
           board[piece.pieceRank - 1][piece.pieceFile.ordinal()] = piece;               
        } 
    }

    private ReturnPlay makeReturnPlay(ReturnPlay.Message message) {
        ReturnPlay returnPlay = new ReturnPlay();
        returnPlay.message = message;
        returnPlay.piecesOnBoard = new ArrayList<ReturnPiece>(piecesOnBoard);
        return returnPlay;
    }

    public ReturnPlay play(String move) {
        // 1. parse move 
        // up to 3 tokens permitted. 1 token - resign, 2 tokens - regular move (e2 e4), 3 tokens - promotion or draw (g7 g8 N (letter optional if omitted Q)) / e2 e4 draw?)
        // 2. keep track of game state
        // who's turn is it next? -> starts with white
        // is game over?
        // 3. find piece at the from location (make sure the player is moving a piece and the piece is their own color)
        // Illegal move does not consume a move.
        // If game isn't over, and piece is legal - delegate work to piece
        updateBoard();
    
        Piece pieceToMove = null;
        // if no piece is selected 
        if (pieceToMove == null) {
            return makeReturnPlay(ReturnPlay.Message.ILLEGAL_MOVE);
        }
        String moveTo = "e4"; 
        ReturnPlay.Message message = pieceToMove.move(this, moveTo);

        // normal move
        if (message == null){
            if (nextPlayer == Chess.Player.white) {
                nextPlayer = Chess.Player.black;
            } else {
                nextPlayer = Chess.Player.white;
            }
        }
        return makeReturnPlay(message);
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
