import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;


public class TravelDAO {
    private Connection conn;

    public TravelDAO() {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8")); // UTF-8 강제 적용
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Properties props = new Properties();
            props.load(new FileReader("src/resource/db/db.properties"));
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            conn = DriverManager.getConnection(url, user, password);
            System.out.println("데이터베이스 연결 성공!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ CSV 파일에서 데이터 읽어서 DB에 삽입하는 메서드
    public void importCsvToDB(String csvFilePath) {
        String sql = "INSERT INTO travel (district, title, description, address, phone, count) VALUES (?, ?, ?, ?, ?, 0)";

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String[] row;
            reader.readNext(); // 첫 번째 줄 (헤더) 건너뛰기

            while ((row = reader.readNext()) != null) {
                pstmt.setString(1, row[1]);  // 지역
                pstmt.setString(2, row[2]);  // 제목
                pstmt.setString(3, row[3]);  // 설명
                pstmt.setString(4, row[4]);  // 주소
                pstmt.setString(5, row[5]);  // 전화번호
                pstmt.executeUpdate();
            }

            System.out.println("CSV 데이터가 DB에 성공적으로 삽입됨!");
        } catch (IOException | SQLException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
