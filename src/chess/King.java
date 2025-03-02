package chess;

class King extends Piece {
    boolean hasMoved = false; // used to deny castling

    public King(boolean isWhite, int rank, int file) {
        super(isWhite, rank, file);

        if (isWhite) {
            pieceType = PieceType.WK;
        } else {
            pieceType = PieceType.BK;
        }
    }

    boolean checkDir(Coord.Dir dir, Coord target, Board board, boolean attackOnly) {
        Coord dst = currentCoord();

        dst = dst.step(dir);
        if (dst != null && dst.equals(target)) {
            if (isKingInCheck(dst, board)) {
                return false;
            }

            if (!attackOnly) {
                // for move-only verification we are done
                return true;
            }

            // for attack check we should make sure this move is an attack
            Piece p = board.getPiece(target);
            if (p != null && p.isWhite != isWhite) {
                return true;
            }
        }
        return false;
    }

    private boolean isKingInCheck(Coord kingCoord, Board board) {
        Chess.Player opponentColor = board.otherPlayer(getPlayer());
        for (Piece piece : board.piecesOnBoard) {
            if (piece.getPlayer() == opponentColor) {
                if (piece.canTarget(board, kingCoord, true)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void print() {
        if (isWhite) {
            System.out.print("wK ");
        } else {
            System.out.print("bK ");
        }
    }

    @Override
    public ReturnPlay.Message move(Board board, Coord dest) {
        // if we can move there we should move there : otherwise illegal
        // if we move there, and an opponent piece is there, we take it
        if (!canTarget(board, dest, false)) {
            return ReturnPlay.Message.ILLEGAL_MOVE;
        }
        updatePosition(dest, board);
        hasMoved = true;
        return null;
    }

    @Override
    void updatePosition(Coord coord, Board board) {
        // recognize if move is castling move
        if (isCastling(board, coord)) {
            doCastles(board, coord);
        } else {
            super.updatePosition(coord, board);
        }
    }

    @Override
    public boolean canTarget(Board board, Coord target, boolean attackOnly) {
        // can you get to the destination with valid moves
        // no jumps over pieces
        // can make it in one move
        Coord src = currentCoord();
        if (src.f == target.f) {
            // pieces on the same file (north / south)
            // walk until reach piece or end of board
            if (checkDir(Coord.Dir.N, target, board, attackOnly))
                return true;
            if (checkDir(Coord.Dir.S, target, board, attackOnly))
                return true;
            return false;
        } else if (src.r == target.r) {
            // pieces on the same rank (east / west)
            if (checkDir(Coord.Dir.E, target, board, attackOnly))
                return true;
            if (checkDir(Coord.Dir.W, target, board, attackOnly))
                return true;

            // check for castling
            if (attackOnly || isKingInCheck(src, board)) {
                // king under check currently can not castle
                return false;
            }

            if (isCastling(board, target)) {
                if (isWhite) {
                    if (target.equals(Coord.parse("b1")) && canLongCastle(board)) {
                        return true;
                    }

                    if (target.equals(Coord.parse("g1")) && canShortCastle(board)) {
                        return true;
                    }
                } else {
                    if (target.equals(Coord.parse("b8")) && canLongCastle(board)) {
                        return true;
                    }

                    if (target.equals(Coord.parse("g8")) && canShortCastle(board)) {
                        return true;
                    }
                }
            }
            return false;

        } else {
            // maybe diagonal?
            if (checkDir(Coord.Dir.NE, target, board, attackOnly))
                return true;
            if (checkDir(Coord.Dir.NW, target, board, attackOnly))
                return true;
            if (checkDir(Coord.Dir.SE, target, board, attackOnly))
                return true;
            if (checkDir(Coord.Dir.SW, target, board, attackOnly))
                return true;
            return false;
        }
    }

    @Override
    public boolean canMove(Board board) {
        // is there any direction the king can move 1 step or not?
        // use getPiece to check to see if there is a piece in the way. -> Once you find
        // a piece check if its yours or the opponents
        Coord src = currentCoord();
        if (checkMove(src.stepNorth(), board)) {
            if (!isKingInCheck(src.stepNorth(), board)) {
                return true;
            }
        }

        if (checkMove(src.stepNorthEast(), board)) {
            if (!isKingInCheck(src.stepNorthEast(), board)) {
                return true;
            }
        }

        if (checkMove(src.stepEast(), board)) {
            if (!isKingInCheck(src.stepEast(), board)) {
                return true;
            }
        }

        if (checkMove(src.stepSouthEast(), board)) {
            if (!isKingInCheck(src.stepSouthEast(), board)) {
                return true;
            }
        }

        if (checkMove(src.stepSouth(), board)) {
            if (!isKingInCheck(src.stepSouth(), board)) {
                return true;
            }
        }

        if (checkMove(src.stepSouthWest(), board)) {
            if (!isKingInCheck(src.stepSouthWest(), board)) {
                return true;
            }
        }

        if (checkMove(src.stepWest(), board)) {
            if (!isKingInCheck(src.stepWest(), board)) {
                return true;
            }
        }

        if (checkMove(src.stepNorthWest(), board)) {
            if (!isKingInCheck(src.stepNorthWest(), board)) {
                return true;
            }
        }

        if (hasMoved) {
            // once the king has moved it can not castle
            return false;
        }

        if (isKingInCheck(src, board)) {
            // king under check currently can not castle
            return false;
        }

        if (canLongCastle(board)) {
            return true;
        }

        if (canShortCastle(board)) {
            return true;
        }
        return false;
    }

    private boolean canLongCastle(Board board) {
        if (hasMoved) {
            return false;
        }

        Coord[] emptyAndNotAttacked = new Coord[3];
        Piece rook;

        // long castling is done by moving previously unmoved white king to h1 or black
        // king to a8
        if (isWhite) {
            rook = board.getPiece(Coord.parse("a1"));
            if (rook == null || rook.pieceType != PieceType.WR) {
                return false;
            }
            // squares b1, c1, d1 should be empty and not attacked by any black piece
            emptyAndNotAttacked[0] = Coord.parse("b1");
            emptyAndNotAttacked[1] = Coord.parse("c1");
            emptyAndNotAttacked[2] = Coord.parse("d1");
        } else {
            rook = board.getPiece(Coord.parse("a8"));
            if (rook == null || rook.pieceType != PieceType.BR) {
                return false;
            }
            // squares b8, c8, d8 should be empty and not attacked by any white piece
            emptyAndNotAttacked[0] = Coord.parse("b8");
            emptyAndNotAttacked[1] = Coord.parse("c8");
            emptyAndNotAttacked[2] = Coord.parse("d8");
        }

        if (!checkKingCastlingPath(board, emptyAndNotAttacked)) {
            return false;
        }

        return true;
    }

    private boolean canShortCastle(Board board) {
        if (hasMoved) {
            return false;
        }

        Coord[] emptyAndNotAttacked = new Coord[2];
        Piece rook;

        // long castling is done by moving previously unmoved white king to h1 or black
        // king to a8
        if (isWhite) {
            rook = board.getPiece(Coord.parse("h1"));
            if (rook == null || rook.pieceType != PieceType.WR || ((Rook) rook).hasMoved) {
                return false;
            }
            // squares f1, g1 should be empty and not attacked by any black piece
            emptyAndNotAttacked[0] = Coord.parse("f1");
            emptyAndNotAttacked[1] = Coord.parse("g1");
        } else {
            rook = board.getPiece(Coord.parse("h8"));
            if (rook == null || rook.pieceType != PieceType.BR || ((Rook) rook).hasMoved) {
                return false;
            }
            // squares f8, g should be empty and not attacked by any white piece
            emptyAndNotAttacked[0] = Coord.parse("f8");
            emptyAndNotAttacked[1] = Coord.parse("g8");
        }

        if (!checkKingCastlingPath(board, emptyAndNotAttacked)) {
            return false;
        }

        return true;
    }

    private boolean checkKingCastlingPath(Board board, Coord coords[]) {
        Chess.Player otherPlayer = board.otherPlayer(getPlayer());
        for (Coord c : coords) {
            Piece p = board.getPiece(c);
            if (p != null) {
                return false;
            }

            if (board.canAttackCoord(otherPlayer, c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isCastling(Board board, Coord coord) {
        Coord src = currentCoord();
        if (isWhite) {
            if (!src.equals(Coord.parse("e1"))) {
                return false;
            }
            if (coord.equals(Coord.parse("g1")) || coord.equals(Coord.parse("b1"))) {
                return true;
            }
            return false;
        } else {
            if (!src.equals(Coord.parse("e8"))) {
                return false;
            }
            if (coord.equals(Coord.parse("g8")) || coord.equals(Coord.parse("b8"))) {
                return true;
            }
            return false;
        }
    }

    private void doCastles(Board board, Coord coord) {
        Coord kingFrom = null, kingTo = null;
        Coord rookFrom = null, rookTo = null;

        if (isWhite) {
            kingFrom = Coord.parse("e1");
            if (coord.equals(Coord.parse("b1"))) {
                // long white castles
                kingTo = Coord.parse("b1");
                rookFrom = Coord.parse("a1");
                rookTo = Coord.parse("c1");
            } else if (coord.equals(Coord.parse("g1"))) {
                // short white castles
                kingTo = Coord.parse("g1");
                rookFrom = Coord.parse("h1");
                rookTo = Coord.parse("f1");
            } else {
                assert false;
            }
        } else {
            kingFrom = Coord.parse("e8");
            if (coord.equals(Coord.parse("b8"))) {
                kingTo = Coord.parse("b8");
                rookFrom = Coord.parse("a8");
                rookTo = Coord.parse("c8");
            } else if (coord.equals(Coord.parse("g8"))) {
                // short black castles
                kingTo = Coord.parse("g8");
                rookFrom = Coord.parse("h8");
                rookTo = Coord.parse("f8");
            } else {
                assert false;
            }
        }

        movePiece(board, kingFrom, kingTo);
        movePiece(board, rookFrom, rookTo);
    }

    private void movePiece(Board board, Coord from, Coord to) {
        assert from != null;
        assert to != null;

        Piece p = board.getPiece(from);
        assert p != null;

        p.pieceRank = to.r + 1;
        p.pieceFile = mapIntToFile(to.f + 1);
    }
}
