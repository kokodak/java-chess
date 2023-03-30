package chess.repository.dao;

import chess.domain.game.ChessGame;
import chess.dto.ChessGameDto;
import chess.repository.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class JdbcChessGameDao implements ChessGameDao {

    private static final String ID_COLUMN = "id";
    private static final String TURN_COLUMN = "turn";

    @Override
    public List<ChessGameDto> findAll() {
        final String sql = "SELECT id, turn FROM chess_game";
        final List<ChessGameDto> chessGameDtos = new ArrayList<>();

        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int id = resultSet.getInt(ID_COLUMN);
                final String turn = resultSet.getString(TURN_COLUMN);
                chessGameDtos.add(ChessGameDto.of(id, turn));
            }
            return chessGameDtos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(final ChessGame chessGame) {
        final String sql = "INSERT INTO chess_game(turn) values(?)";

        final String turn = chessGame.getTurn().name();

        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, turn);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int findLastInsertId() {
        final String sql = "SELECT MAX(id) FROM chess_game";

        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nullable ChessGameDto findById(final int id) {
        final String sql = "SELECT id, turn FROM chess_game WHERE id = ?";

        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ChessGameDto.of(resultSet.getInt(ID_COLUMN), resultSet.getString(TURN_COLUMN));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(final ChessGame chessGame) {
        final String sql = "UPDATE chess_game SET turn = ? WHERE id = ?";

        final int id = chessGame.getId();
        final String turn = chessGame.getTurn().name();

        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, turn);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
