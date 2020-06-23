package user.service;

import user.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnect implements IUserService {
    private String jdbcUrl = "jdbc:mysql://localhost:3306/user_manager";
    private String jdbcUserName = "root";
    private String jdbcPassword = "123456";

    private static final String SELECT_USER_BY_ID = "select * from users where id = ?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES " + " (?, ?, ?);";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";

    public DatabaseConnect() {
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
            System.out.println("Kết nối thành công");

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Kết nối không thành công");
        } catch (SQLException throwables) {
            // TODO Auto-generated catch block
            throwables.printStackTrace();
            System.out.println("Kết nối không thành công");
        }
        return connection;
    }

    /** Nhập vào một bản ghi cho bảng users */
    @Override
    public void insertUser(User user) throws SQLException {
        System.out.println(INSERT_USERS_SQL);

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
        ){
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,user.getCountry());
            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** Lấy ra bản ghi của người dùng có id tương ứng */
    @Override
    public User selectUser(int id) {
        User user = null;
        /** Step 1: Thiết lập kết nối*/
        try (
                Connection connection = getConnection();
                /** Step 2: Tạo một câu lệnh bằng cách sử dụng đối tượng kết nối*/
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
        ) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            /** Step3:  Thực hiện hoặc cập nhật truy vấn*/
            ResultSet resultSet = preparedStatement.executeQuery();

            /**Xử lý đối tượng resultset.*/
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                user = new User(id, name, email, country);
            }
        } catch (SQLException throwables) {
            printSQLException(throwables);
        }
        return user;
    }

    @Override
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try (
                /** Step 1: Thiết lập kết nối*/
                Connection connection = getConnection();
                /** Step 2: Tạo một câu lệnh bằng cách sử dụng đối tượng kết nối*/
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
        ) {
            System.out.println(preparedStatement);
            /** Step3: Thực hiện hoặc cập nhật truy vấn*/
            ResultSet resultSet = preparedStatement.executeQuery();

            /**Xử lý đối tượng resultset.*/
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");

                users.add(new User(id, name, email, country));
            }
        } catch (SQLException throwables) {
            printSQLException(throwables);
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL);
        ){
            System.out.println(preparedStatement);
            preparedStatement.setInt(1,id);
            rowDeleted = preparedStatement.executeUpdate()>0;
            System.out.println("Xóa thành công: "+rowDeleted);
        }
        return rowDeleted;
    }

    /** Cập nhật bản ghi trong bảng users */
    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL);
                ){
            System.out.println(UPDATE_USERS_SQL);
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,user.getCountry());
            preparedStatement.setInt(4,user.getId());

            rowUpdated = preparedStatement.executeUpdate() > 0;
            System.out.println("Cập nhật thành công: "+rowUpdated);
        }
        return rowUpdated;
    }

    @Override
    public User search(String value) {
        DatabaseConnect databaseConnect = new DatabaseConnect();
        ArrayList<User> userArrayList = (ArrayList<User>) databaseConnect.selectAllUsers();

        for (User user : userArrayList) {
            if (
                    Integer.toString(user.getId()).equals(value) ||
                            user.getName().toLowerCase().contains(value.toLowerCase()) ||
                            user.getEmail().toLowerCase().contains(value.toLowerCase()) ||
                            user.getCountry().toLowerCase().contains(value.toLowerCase())
            ) {
                return user;
            }
        }
        return null;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }


//    public static void main(String[] args) {
//        DatabaseConnect databaseConnect = new DatabaseConnect();
//        databaseConnect.getConnection();
//    }
}
