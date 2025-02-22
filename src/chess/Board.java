package chess;

import java.util.ArrayList;

class Board {	
	ArrayList<Piece> piecesOnBoard;
    Piece[][] board = new Piece[8][8];
    Chess.Player nextPlayer = Chess.Player.white;
    boolean gameOver = false;
	
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

    private ReturnPlay makeIllegalMove() {
        return makeReturnPlay(ReturnPlay.Message.ILLEGAL_MOVE);
    }

    private Piece getPiece(Coord coord) {
        return board[coord.r][coord.f];
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

        // if game is already over, no more moves
        if (gameOver) {
            return makeIllegalMove();
        }

        String[] moveTokens = move.split(" ");

        // if number of tokens is more than 3 or less than 1, move automatically illegal
        int numTokens = moveTokens.length;
        if (numTokens > 3 || numTokens < 1) {
            return makeIllegalMove();
        }
 
        if (numTokens == 1) {
            if (moveTokens[0].equals("resign")) {
                if (nextPlayer == Chess.Player.white) {
                    gameOver = true;
                    return makeReturnPlay(ReturnPlay.Message.RESIGN_BLACK_WINS);
                }
                gameOver = true; 
                return makeReturnPlay(ReturnPlay.Message.RESIGN_WHITE_WINS);
            }
            return makeIllegalMove(); 
        }

        Coord from = Coord.parse(moveTokens[0]);
        Coord to = Coord.parse(moveTokens[1]);

        if (from == null || to == null) {
            return makeIllegalMove();
        }

        Piece fromPiece = getPiece(from);
        Piece toPiece = getPiece(to);

        // first check some common rules that apply to all pieces

        if (fromPiece == null) {
            // must have a piece to move
            return makeIllegalMove();
        }

        if (fromPiece.getPlayer() != nextPlayer) {
            // can not move opponent's piece
            return makeIllegalMove();
        }

        if (toPiece != null && toPiece.getPlayer() == nextPlayer) {
            // can not attack own piece
            return makeIllegalMove();
        }

        ReturnPlay.Message message = fromPiece.move(this, to);

        // normal move
        if (message == null) {
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
