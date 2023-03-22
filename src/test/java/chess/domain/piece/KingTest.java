package chess.domain.piece;

import static chess.domain.piece.Side.*;
import static chess.domain.position.File.*;
import static chess.domain.position.Rank.*;
import static org.assertj.core.api.Assertions.*;

import chess.domain.board.Board;
import chess.domain.piece.immediate.King;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class KingTest {

    private Map<Position, Piece> boardMap;
    private King king;

    @BeforeEach
    void init() {
        king = new King(WHITE);
        boardMap = new HashMap<>();
        for (final Rank rank : Rank.values()) {
            for (final File file : File.values()) {
                boardMap.put(Position.of(file, rank), new Empty());
            }
        }
    }

    @Test
    void 킹은_자신이_갈_수_있는_위치들을_반환한다() {
        /*

        ........
        ........
        ........
        ........
        ........
        ........
        pP......
        k.......

        */

        boardMap.put(Position.of(A, ONE), king);
        boardMap.put(Position.of(A, TWO), new Pawn(WHITE));
        boardMap.put(Position.of(B, TWO), new Pawn(BLACK));
        final Board board = new Board(boardMap);

        final List<Position> movablePosition = king.findMovablePosition(Position.of(A, ONE), board);

        assertThat(movablePosition)
                .containsOnly(
                        Position.of(B, ONE),
                        Position.of(B, TWO)
                );
    }
}
