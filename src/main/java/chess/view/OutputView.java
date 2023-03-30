package chess.view;

import chess.domain.board.Board;
import chess.domain.game.Turn;
import chess.domain.piece.Piece;
import chess.domain.piece.Side;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.dto.ChessGameDto;
import chess.dto.GameResultDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutputView {

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String ADVANTAGE_SIDE_MESSAGE = " 진영이 더 유리한 상황입니다.";

    public void printExistChessGameId(final List<ChessGameDto> chessGameDtos) {
        System.out.println("[현재 남아있는 체스 게임 방 목록]" + LINE_SEPARATOR);
        for (final ChessGameDto chessGameDto : chessGameDtos) {
            final int id = chessGameDto.getId();
            final Turn turn = chessGameDto.getTurn();
            System.out.println("방 번호 : " + id);
            System.out.println("턴 정보 : " + turn.name() + " 차례" + LINE_SEPARATOR);
        }
    }

    public void printErrorMessage(final Exception e) {
        System.out.println(e.getMessage() + LINE_SEPARATOR);
    }

    public void printStartMessage() {
        System.out.println("체스 게임을 시작합니다.");
    }

    public void printBoard(final Board board) {
        final List<String> boardFormats = getBoardFormats(board);
        System.out.println();
        boardFormats.forEach(System.out::println);
    }

    private List<String> getBoardFormats(final Board board) {
        final List<String> boardFormats = new ArrayList<>();

        initBoardFormats(boardFormats, board);
        addRankFormat(boardFormats);
        boardFormats.add("---------");
        boardFormats.add(getFileFormat());

        return boardFormats;
    }

    private static void initBoardFormats(final List<String> boardFormats, final Board board) {
        final Map<Position, Piece> boardMap = board.getBoard();
        for (Rank rank : Rank.values()) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (File file : File.values()) {
                final Piece piece = boardMap.get(Position.of(file, rank));
                stringBuilder.append(piece.name());
            }
            boardFormats.add(stringBuilder.toString());
        }
    }

    private void addRankFormat(final List<String> boardFormats) {
        for (final Rank rank : Rank.values()) {
            String boardFormat = boardFormats.get(Board.UPPER_BOUNDARY - rank.index());
            boardFormat += " | " + rank.index();
            boardFormats.set(Board.UPPER_BOUNDARY - rank.index(), boardFormat);
        }
    }

    private String getFileFormat() {
        StringBuilder stringBuilder = new StringBuilder();
        for (final File file : File.values()) {
            stringBuilder.append(file.symbol());
        }
        stringBuilder.append(LINE_SEPARATOR);
        return stringBuilder.toString();
    }

    public void printStatus(final GameResultDto gameResult) {
        System.out.println("WHITE 진영의 기물 점수: " + gameResult.getWhiteSidePrice());
        System.out.println("BLACK 진영의 기물 점수: " + gameResult.getBlackSidePrice());
        System.out.println("현재 " + getAdvantageSideFormat(gameResult.getAdvantageSide()));
        System.out.println();
    }

    private String getAdvantageSideFormat(final Side side) {
        if (side.isWhite()) {
            return "WHITE" + ADVANTAGE_SIDE_MESSAGE;
        }
        if (side.isBlack()) {
            return "BLACK" + ADVANTAGE_SIDE_MESSAGE;
        }
        return "두 진영 모두 비등비등한 상황입니다.";
    }
}
