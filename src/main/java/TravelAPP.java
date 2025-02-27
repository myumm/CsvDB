public class TravelAPP {
    public static void main(String[] args) {
        TravelDAO dao = new TravelDAO();

        // CSV 파일에서 데이터 읽어와서 DB에 삽입
        dao.importCsvToDB("csv/travel.csv");

        dao.close();
    }
}
