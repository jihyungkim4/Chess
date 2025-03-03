package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceType;

class Board {
    ArrayList<Piece> piecesOnBoard;
    Piece[][] board = new Piece[8][8];
    Chess.Player nextPlayer = Chess.Player.white;
    boolean gameOver = false;
    int moveNumber = 1;
    Pawn promotionPawn = null;

    public Board() {
        piecesOnBoard = new ArrayList<Piece>();

        // add all pawns
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

        // add rooks
        piecesOnBoard.add(new Rook(true, 1, 1));
        piecesOnBoard.add(new Rook(true, 1, 8));
        piecesOnBoard.add(new Rook(false, 8, 1));
        piecesOnBoard.add(new Rook(false, 8, 8));

        // add bishops
        piecesOnBoard.add(new Bishop(true, 1, 3));
        piecesOnBoard.add(new Bishop(true, 1, 6));
        piecesOnBoard.add(new Bishop(false, 8, 3));
        piecesOnBoard.add(new Bishop(false, 8, 6));

        // add knights
        piecesOnBoard.add(new Knight(true, 1, 2));
        piecesOnBoard.add(new Knight(true, 1, 7));
        piecesOnBoard.add(new Knight(false, 8, 2));
        piecesOnBoard.add(new Knight(false, 8, 7));

        // add kings
        piecesOnBoard.add(new King(true, 1, 5));
        piecesOnBoard.add(new King(false, 8, 5));

        // add queens
        piecesOnBoard.add(new Queen(true, 1, 4));
        piecesOnBoard.add(new Queen(false, 8, 4));
    }

    public void load(ArrayList<ReturnPiece> initialPieces, Chess.Player nextPlayer) {
        this.piecesOnBoard.clear();
        for (ReturnPiece p : initialPieces) {
            switch (p.pieceType) {
                case BB:
                    piecesOnBoard.add(new Bishop(false, p.pieceRank, p.pieceFile.ordinal() + 1));
                    break;
                case BK:
                    piecesOnBoard.add(new King(false, p.pieceRank, p.pieceFile.ordinal() + 1));
                    break;
                case BN:
                    piecesOnBoard.add(new Knight(false, p.pieceRank, p.pieceFile.ordinal() + 1));
                    break;
                case BP:
                    piecesOnBoard.add(new Pawn(false, p.pieceRank, p.pieceFile.ordinal() + 1));
                    break;
                case BQ:
                    piecesOnBoard.add(new Queen(false, p.pieceRank, p.pieceFile.ordinal() + 1));
                    break;
                case BR:
                    piecesOnBoard.add(new Rook(false, p.pieceRank, p.pieceFile.ordinal() + 1));
                    break;
                case WB:
                    piecesOnBoard.add(new Bishop(true, p.pieceRank, p.pieceFile.ordinal() + 1));
                    break;
                case WK:
                    piecesOnBoard.add(new King(true, p.pieceRank, p.pieceFile.ordinal() + 1));
                    break;
                case WN:
                    piecesOnBoard.add(new Knight(true, p.pieceRank, p.pieceFile.ordinal() + 1));
                    break;
                case WP:
                    piecesOnBoard.add(new Pawn(true, p.pieceRank, p.pieceFile.ordinal() + 1));
                    break;
                case WQ:
                    piecesOnBoard.add(new Queen(true, p.pieceRank, p.pieceFile.ordinal() + 1));
                    break;
                case WR:
                    piecesOnBoard.add(new Rook(true, p.pieceRank, p.pieceFile.ordinal() + 1));
                    break;
                default:
                    break;

            }
        }
        this.nextPlayer = nextPlayer;
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

    Piece getPiece(Coord coord) {
        return board[coord.r][coord.f];
    }

    void removePiece(Coord coord) {
        board[coord.r][coord.f] = null;
    }

    void putPiece(Coord coord, Piece piece) {
        board[coord.r][coord.f] = piece;
    }

    // function returns the location of the king of the next player to move
    private Coord getKing(Chess.Player player) {
        Piece.PieceType matchingKing = (player == Chess.Player.white) ? Piece.PieceType.WK : Piece.PieceType.BK;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y] != null && board[x][y].pieceType == matchingKing) {
                    // rank, file
                    return new Coord(x, y);
                }
            }
        }
        return null; // should never get here!
    }

    private boolean isKingInCheck(Coord kingCoord) {
        Piece king = getPiece(kingCoord);
        Chess.Player kingPlayer = king.getPlayer();
        Chess.Player opponentColor = otherPlayer(kingPlayer);

        for (Piece piece : piecesOnBoard) {
            if (piece.getPlayer() == opponentColor) {
                if (piece.canTarget(this, kingCoord, true)) {
                    return true;
                }
            }
        }
        return false;
    }

    // returns true if player can attack specified coord
    public boolean canAttackCoord(Chess.Player player, Coord coord) {
        for (Piece piece : piecesOnBoard) {
            if (piece.getPlayer() == player) {
                if (piece.canTarget(this, coord, true)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isKingInCheckmate(Coord kingCoord) {
        // king is in check
        if (!isKingInCheck(kingCoord)) {
            return false;
        }
        // can the king move?
        Piece king = getPiece(kingCoord);
        if (king.canMove(this)) {
            return false;
        }

        // can another piece block check
        if (canBlockCheck(king)) {
            return false;
        }

        return true;
    }

    private boolean canBlockCheck(Piece king) {
        for (Piece bp : piecesOnBoard) {
            if (bp.getPlayer() != king.getPlayer() || bp == king) {
                continue;
            }
            if (canBlockCheckWithPiece(king, bp)) {
                return true;
            }
        }
        return false;
    }

    private boolean canBlockCheckWithPiece(Piece king, Piece p) {
        for (int r = 0; r < 8; ++r) {
            for (int f = 0; f < 8; ++f) {
                Coord c = new Coord(r, f);
                if (c.equals(king.currentCoord())) {
                    continue;
                }
                
                if (c.equals(p.currentCoord())) {
                    continue;
                }
               
                Piece cp = getPiece(c);
                if (cp != null && cp.getPlayer() == p.getPlayer()) {
                    continue;
                }

                if (p.canTarget(this, c, false)) {
                    Board altBoard = cloneBoard();
                    Piece altFromPiece = altBoard.getPiece(p.currentCoord());
                    altFromPiece.move(altBoard, c);
                    altBoard.updateBoard();
                    if (!altBoard.isKingInCheck(king.currentCoord())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    Chess.Player otherPlayer(Chess.Player player) {
        return (player == Chess.Player.white) ? Chess.Player.black : Chess.Player.white;
    }

    public ReturnPlay play(String move) {
        // 1. parse move
        // up to 3 tokens permitted. 1 token - resign, 2 tokens - regular move (e2 e4),
        // 3 tokens - promotion or draw (g7 g8 N (letter optional if omitted Q)) / e2 e4
        // draw?)
        // 2. keep track of game state
        // who's turn is it next? -> starts with white
        // is game over?
        // 3. find piece at the from location (make sure the player is moving a piece
        // and the piece is their own color)
        // Illegal move does not consume a move.
        // If game isn't over, and piece is legal - delegate work to piece
        updateBoard();

        // if game is already over, no more moves
        if (gameOver) {
            return makeIllegalMove();
        }

        String[] moveTokens = move.trim().split(" ");

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
        boolean drawOffered = false;
        boolean promotionTargetSpecified = false;

        // by default promotion target if any is the queen
        PieceType promotionTarget = (nextPlayer == Chess.Player.white ? PieceType.WQ : PieceType.BQ);

        if (numTokens == 3) {
            // third token is either draw request or a promotion target
            String t = moveTokens[2];
            if (t.equals("draw?")) {
                drawOffered = true;
            } else if (t.equals("Q")) {
                promotionTarget = (nextPlayer == Chess.Player.white ? PieceType.WQ : PieceType.BQ);
                promotionTargetSpecified = true;
            } else if (t.equals("N")) {
                promotionTarget = (nextPlayer == Chess.Player.white ? PieceType.WN : PieceType.BN);
                promotionTargetSpecified = true;
            } else if (t.equals("R")) {
                promotionTarget = (nextPlayer == Chess.Player.white ? PieceType.WR : PieceType.BR);
                promotionTargetSpecified = true;
            } else if (t.equals("B")) {
                promotionTarget = (nextPlayer == Chess.Player.white ? PieceType.WB : PieceType.BB);
                promotionTargetSpecified = true;
            } else {
                return makeIllegalMove();
            }
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

        if (fromPiece.pieceType != PieceType.WP && 
            fromPiece.pieceType != PieceType.BP && 
            promotionTargetSpecified) {
            // only pawns can specify promotion targets
            return makeIllegalMove();
        }

        if (!fromPiece.canTarget(this, to, false)) {
            return makeIllegalMove();
        }

        // AT THIS POINT special rules regarding checks need to happen

        if (!doSpecialChecks(from, to)) {
            return makeIllegalMove();
        }

        ReturnPlay.Message message = fromPiece.move(this, to);

        // normal move
        if (message == null) {

            processPawnPromotion(promotionTarget);

            if (isKingInCheckmate(getKing(otherPlayer(nextPlayer)))) {
                gameOver = true;
                return (nextPlayer == Chess.Player.white) ? makeReturnPlay(ReturnPlay.Message.CHECKMATE_WHITE_WINS)
                        : makeReturnPlay(ReturnPlay.Message.CHECKMATE_BLACK_WINS);
            }
            nextPlayer = otherPlayer(nextPlayer);
            ++moveNumber;

            if (drawOffered) {
                // if a player puts another player in check and asks for a draw the draw is
                // accepted
                gameOver = true;
                return makeReturnPlay(ReturnPlay.Message.DRAW);
            }

            // is opponents king in check?
            if (isKingInCheck(getKing(nextPlayer))) {
                return makeReturnPlay(ReturnPlay.Message.CHECK);
            }
        }
        return makeReturnPlay(message);
    }

    private void processPawnPromotion(PieceType promoteType) {
        if (promotionPawn == null) {
            return; // nothing to promote
        }

        Coord pos = promotionPawn.currentCoord();

        // remove piece
        removePiece(pos);
        piecesOnBoard.remove(promotionPawn);

        // if not specified promote to queen
        if (promoteType == null) {
            promoteType = promotionPawn.isWhite ? PieceType.WQ : PieceType.BQ;
        }

        Piece newPiece = null;
        switch (promoteType) {
            case BB:
                newPiece = new Bishop(false, pos.r + 1, pos.f + 1);
                break;
            case BN:
                newPiece = new Knight(false, pos.r + 1, pos.f + 1);
                break;
            case BQ:
                newPiece = new Queen(false, pos.r + 1, pos.f + 1);
                break;
            case BR:
                newPiece = new Rook(false, pos.r + 1, pos.f + 1);
                break;
            case WB:
                newPiece = new Bishop(true, pos.r + 1, pos.f + 1);
                break;
            case WN:
                newPiece = new Knight(true, pos.r + 1, pos.f + 1);
                break;
            case WQ:
                newPiece = new Queen(true, pos.r + 1, pos.f + 1);
                break;
            case WR:
                newPiece = new Rook(true, pos.r + 1, pos.f + 1);
                break;
            default:
                assert (false); // we can not promote to any other piece
                break;
        }

        piecesOnBoard.add(newPiece);
        board[pos.r][pos.f] = null;
        promotionPawn = null;
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

    private boolean doSpecialChecks(Coord from, Coord to) {
        Board altBoard = cloneBoard();
        Piece altFromPiece = altBoard.getPiece(from);
        altFromPiece.move(altBoard, to);
        altBoard.updateBoard();
        return !altBoard.isKingInCheck(altBoard.getKing(nextPlayer));
    }

    boolean isKingInCheckAfterMove(King p, Coord newCoord) {
        Board altBoard = cloneBoard();
        Piece altFromPiece = altBoard.getPiece(p.currentCoord());
        altFromPiece.move(altBoard, newCoord);
        altBoard.updateBoard();
        return altBoard.isKingInCheck(newCoord);
    }

    private Board cloneBoard() {
        Board altBoard = new Board();
        altBoard.gameOver = gameOver;
        altBoard.moveNumber = moveNumber;
        altBoard.nextPlayer = nextPlayer;
        altBoard.piecesOnBoard.clear();

        for (Piece p : piecesOnBoard) {
            try {
                Piece altPiece = p.clone();
                altBoard.piecesOnBoard.add(altPiece);
            } catch (java.lang.CloneNotSupportedException e) {
                assert false;
            }
        }

        if (promotionPawn != null) {
            Coord promCoord = promotionPawn.currentCoord();
            altBoard.promotionPawn = (Pawn) altBoard.getPiece(promCoord);
        }

        altBoard.updateBoard();
        return altBoard;
    }
}
