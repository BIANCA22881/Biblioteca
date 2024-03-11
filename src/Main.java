import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            App app = new App();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}