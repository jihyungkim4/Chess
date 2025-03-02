package chess;

import java.util.ArrayList;

class Pawn extends Piece {
    // pawn can only moved long from initial position and only once
    Coord longMoveDst = null;
    int longMoveNumber = -1;

    // empassantCapture can only be done once
    Pawn empassantCapturedPiece = null;

    public Pawn(boolean isWhite, int rank, int file) {
        super(isWhite, rank, file);

        if (isWhite) {
            pieceType = PieceType.WP;
        } else {
            pieceType = PieceType.BP;
        }
    }

    @Override
    public void print() {
        if (isWhite) {
            System.out.print("wP ");
        } else {
            System.out.print("bP ");
        }
    }

    @Override
    public ReturnPlay.Message move(Board board, Coord dest) {
        if (!canTarget(board, dest, false)) {
            return ReturnPlay.Message.ILLEGAL_MOVE;
        }
        Coord src = currentCoord();
        updatePosition(dest, board);
        if (empassantCapturedPiece != null) {
            board.removePiece(empassantCapturedPiece.currentCoord());
            board.piecesOnBoard.remove(empassantCapturedPiece);
            empassantCapturedPiece = null;
        }
        if (Math.abs(dest.r - src.r) > 1) {
            longMoveDst = dest;
            longMoveNumber = board.moveNumber;
        } else {
            if (isWhite && dest.r == 7) {
                // this pawn reached promotion rank, make it into a queen
                board.promotionPawn = this;
            }
            if (!isWhite && dest.r == 0) {
                // this pawn reached promotion rank, make it into a queen
                board.promotionPawn = this;
            }
        }

        return null;
    }

    @Override
    public boolean canTarget(Board board, Coord coord, boolean attackOnly) {
        Coord src = currentCoord();
        Coord dst;

        if (isWhite) {
            if (!attackOnly) {
                // check north 1-step unobstructed
                dst = src.stepNorth();
                if (coord.equals(dst) && board.getPiece(dst) == null) {
                    return true;
                }

                // check north 2-step unobstructed
                Coord dst2 = src.step(2, 0);
                if (src.r == 1 && coord.equals(dst2) && board.getPiece(dst) == null && board.getPiece(dst2) == null) {
                    return true;
                }
            }

            // check north west
            dst = src.stepNorthWest();
            if (dst != null && coord.equals(dst)) {
                Piece p = board.getPiece(dst);
                if (p != null && !p.isWhite) {
                    return true;
                }

                // check en passant
                p = board.getPiece(src.stepWest());
                if (p != null && p.pieceType == PieceType.BP) {
                    Pawn bp = (Pawn) p;
                    if (bp.longMoveNumber == board.moveNumber - 1) {
                        empassantCapturedPiece = bp;
                        return true;
                    }
                }
            }

            // check north east
            dst = src.stepNorthEast();
            if (dst != null && coord.equals(dst)) {
                Piece p = board.getPiece(dst);
                if (p != null && !p.isWhite) {
                    return true;
                }

                // check en passant
                p = board.getPiece(src.stepEast());
                if (p != null && p.pieceType == PieceType.BP) {
                    Pawn bp = (Pawn) p;
                    if (bp.longMoveNumber == board.moveNumber - 1) {
                        empassantCapturedPiece = bp;
                        return true;
                    }
                }
            }

            return false;
        }

        if (!attackOnly) {
            // check south 1-step unobstructed
            dst = src.stepSouth();
            if (coord.equals(dst) && board.getPiece(dst) == null) {
                return true;
            }

            // check south 2-step unobstructed
            Coord dst2 = src.step(-2, 0);
            if (src.r == 6 && coord.equals(dst2) && board.getPiece(dst) == null && board.getPiece(dst2) == null) {
                return true;
            }
        }

        // check south west
        dst = src.stepSouthWest();
        if (dst != null && coord.equals(dst)) {
            Piece p = board.getPiece(dst);
            if (p != null && p.isWhite) {
                return true;
            }

            // check en passant
            dst = src.stepWest();
            p = board.getPiece(dst);
            if (p != null && p.pieceType == PieceType.WP) {
                Pawn wp = (Pawn) p;
                if (wp.longMoveNumber == board.moveNumber - 1) {
                    empassantCapturedPiece = wp;
                    return true;
                }
            }
        }

        // check south east
        dst = src.stepSouthEast();
        if (dst != null && coord.equals(dst)) {
            Piece p = board.getPiece(dst);
            if (p != null && p.isWhite) {
                return true;
            }

            // check en passant
            p = board.getPiece(src.stepEast());
            if (p != null && p.pieceType == PieceType.WP) {
                Pawn wp = (Pawn) p;
                if (wp.longMoveNumber == board.moveNumber - 1) {
                    empassantCapturedPiece = wp;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canMove(Board board) {
        // the pawn can move if
        // 1. there is no piece in front of it and it is not on edge of board (move)
        // 2. there is opponent's piece on a valid forward diagonals left or right
        // (capture)
        // 3. there is opponent's piece next for en passant capture and opponent made
        // a long pawn move with that pawn in the move immediately preceding this one.
        Coord src = currentCoord();

        if (isWhite) {
            // white pawns move up

            // can pawn move 1 step up
            Coord dst = src.stepNorth();
            if (dst != null && board.getPiece(dst) == null) {
                return true;
            }

            // can pawn capture north-west
            dst = src.stepNorthWest();
            if (dst != null) {
                Piece p = board.getPiece(dst);
                if (p != null) {
                    if (p.isWhite) {
                        return true;
                    }
                } else {
                    if (this.pieceRank == 5) {
                        // check en passant west
                        Piece wp = board.getPiece(src.stepWest());
                        if (wp.pieceType == PieceType.BP) {
                            Pawn wPawn = (Pawn) wp;
                            if (wPawn.longMoveDst == wp.currentCoord()
                                    && wPawn.longMoveNumber == board.moveNumber - 1) {
                                return true;
                            }
                        }
                    }
                }
            }

            // can pawn capture north-east
            dst = src.stepNorthEast();
            if (dst != null) {
                Piece p = board.getPiece(dst);
                if (p != null) {
                    if (!p.isWhite) {
                        return true;
                    }
                } else {
                    if (pieceRank == 5) {
                        // check en passant east
                        Piece wp = board.getPiece(src.stepEast());
                        if (wp.pieceType == PieceType.BP) {
                            Pawn wPawn = (Pawn) wp;
                            if (wPawn.longMoveDst == wp.currentCoord()
                                    && wPawn.longMoveNumber == board.moveNumber - 1) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

        // black pawns move down

        // can pawn move 1 step down
        Coord dst = src.stepSouth();
        if (dst != null && board.getPiece(dst) == null)
            return true;

        // can pawn capture south-west
        dst = src.stepSouthWest();
        if (dst != null) {
            Piece p = board.getPiece(dst);
            if (p != null) {
                if (p.isWhite) {
                    return true;
                }
            } else {
                if (pieceRank == 4) {
                    // check en passant east
                    Piece bp = board.getPiece(src.stepEast());
                    if (bp.pieceType == PieceType.BP) {
                        Pawn bPawn = (Pawn) bp;
                        if (bPawn.longMoveDst == bp.currentCoord() && bPawn.longMoveNumber == board.moveNumber - 1) {
                            return true;
                        }
                    }
                }
            }
        }

        // can pawn capture south-east
        dst = src.stepSouthEast();
        if (dst != null) {
            Piece p = board.getPiece(dst);
            if (p != null) {
                if (p.isWhite) {
                    return true;
                }
            } else {
                if (pieceRank == 4) {
                    // check en passant west
                    Piece bp = board.getPiece(src.stepWest());
                    if (bp.pieceType == PieceType.BP) {
                        Pawn bPawn = (Pawn) bp;
                        if (bPawn.longMoveDst == bp.currentCoord() && bPawn.longMoveNumber == board.moveNumber - 1) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
