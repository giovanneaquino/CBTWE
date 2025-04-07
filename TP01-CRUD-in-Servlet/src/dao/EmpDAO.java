package dao;
import java.sql.*;
import java.util.*;
import model.Emp;



/*
	Ricardo Queiroz
	Giovanne Aquino
*/



public class EmpDAO {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/tp01WE1?useTimezone=true&serverTimezone=America/Sao_Paulo";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "@Mysqlt6y777";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    public static int save(Emp e) {
        int status = 0;
        String sql = "INSERT INTO user905(name, password, email, country) VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getPassword());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getCountry());
            status = ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return status;
    }

    public static int update(Emp e) {
        int status = 0;
        String sql = "UPDATE user905 SET name=?, password=?, email=?, country=? WHERE id=?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getPassword());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getCountry());
            ps.setInt(5, e.getId());
            status = ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return status;
    }

    public static int delete(int id) {
        int status = 0;
        String sql = "DELETE FROM user905 WHERE id=?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            status = ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return status;
    }

    public static Emp getEmployeeById(int id) {
        Emp e = new Emp();
        String sql = "SELECT * FROM user905 WHERE id=?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                e.setId(rs.getInt("id"));
                e.setName(rs.getString("name"));
                e.setPassword(rs.getString("password"));
                e.setEmail(rs.getString("email"));
                e.setCountry(rs.getString("country"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return e;
    }

    public static List<Emp> getAllEmployees() {
        List<Emp> list = new ArrayList<>();
        String sql = "SELECT * FROM user905";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Emp e = new Emp();
                e.setId(rs.getInt("id"));
                e.setName(rs.getString("name"));
                e.setPassword(rs.getString("password"));
                e.setEmail(rs.getString("email"));
                e.setCountry(rs.getString("country"));
                list.add(e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }
}
