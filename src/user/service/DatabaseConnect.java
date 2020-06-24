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
    private static final String SORT_USERS_SQL = "select * from users order by";

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

    /**
     * Nhập vào một bản ghi cho bảng users
     */
    @Override
    public void insertUser(User user) throws SQLException {
        System.out.println(INSERT_USERS_SQL);

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
        ) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy ra bản ghi của người dùng có id tương ứng
     */
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
        ) {
            System.out.println(preparedStatement);
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
            System.out.println("Xóa thành công: " + rowDeleted);
        }
        return rowDeleted;
    }

    /**
     * Cập nhật bản ghi trong bảng users
     */
    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL);
        ) {
            System.out.println(UPDATE_USERS_SQL);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            preparedStatement.setInt(4, user.getId());

            rowUpdated = preparedStatement.executeUpdate() > 0;
            System.out.println("Cập nhật thành công: " + rowUpdated);
        }
        return rowUpdated;
    }

    /**
     * Tim kiem ban ghi theo cac truong cua bang
     */
//    @Override
//    public User search(String value) {
//        DatabaseConnect databaseConnect = new DatabaseConnect();
//        ArrayList<User> userArrayList = (ArrayList<User>) databaseConnect.selectAllUsers();
//
//        for (User user : userArrayList) {
//            if (
//                    Integer.toString(user.getId()).equals(value) ||
//                            user.getName().toLowerCase().contains(value.toLowerCase()) ||
//                            user.getEmail().toLowerCase().contains(value.toLowerCase()) ||
//                            user.getCountry().toLowerCase().contains(value.toLowerCase())
//            ) {
//                return user;
//            }
//        }
//        return null;
//    }
    @Override
    public List<User> search(String value) {
        DatabaseConnect databaseConnect = new DatabaseConnect();
        ArrayList<User> userArrayList = (ArrayList<User>) databaseConnect.selectAllUsers();

        List<User> userList = new ArrayList<>();

        /** Gọi tới hàm xóa bỏ khoảng trắng*/
        value = getValue(value);

        //tìm kiếm
        for (User user : userArrayList) {
            if (Integer.toString(user.getId()).equals(value) ||
                    user.getName().toLowerCase().contains(value.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(value.toLowerCase()) ||
                    user.getCountry().toLowerCase().contains(value.toLowerCase())
            ) {
                userList.add(user);
            }
        }
        return userList;
    }

    public List<User> sortUsers(String sortBy){
        if (!sortBy.equals("id") && !sortBy.equals("name") && !sortBy.equals("email") && !sortBy.equals("country")){
            sortBy = "id";
        }
//        String SORT_ALL_USERS = "SELECT * FROM users ORDER BY"+sortBy+"ASC";

        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection()){

            Statement statement = connection.createStatement();

            System.out.println(statement);
            ResultSet rs = statement.executeQuery(SORT_USERS_SQL+sortBy+"ASC");
            System.out.println(rs);
            System.out.println(rs.getFetchSize());

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    /**
     * Xóa bỏ tất cả các khoảng trắng đầu và cuối khi tìm kiếm một giá trị bất kỳ
     */
    private String getValue(String value) {
//        int firstIndex = 0;
//        int lastIndex = value.length() - 1;
        if (value.charAt(0) == ' ' || value.charAt(value.length() - 1) == ' ') {
            value = value.trim(); //xóa khoảng trắng ở đầu và cuối thôi

//            for (int i = 0; i < value.length(); i++) {
//                if (value.charAt(i) != ' ') {
//                    firstIndex = i;
//                    for (int j = value.length() - 1; j >= 0; j--) {
//                        if (value.charAt(j) != ' ') {
//                            lastIndex = j;
//                            break;
//                        }
//                    }
//                    break;
//                }
//            }
//            value = value.substring(firstIndex, lastIndex);
        }
        return value;
    }

    /**
     * In ra các ngoại lệ bắt gặp
     */
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


    @Override
    public User getUserById(int id) {
        User user = null;
        String query = "{CALL get_user_by_id(?)}";

        // Step 1: Tạo kết nối
        try (
                Connection connection = getConnection();
                // Step 2:Create a statement using connection object
                CallableStatement callableStatement = connection.prepareCall(query)
                ) {
            callableStatement.setInt(1, id);

            // Step 3: Thực hiện câu truy vấn lưu vào ResultSet
            ResultSet rs = callableStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user = new User(id, name, email, country);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    @Override
    public void insertUserStore(User user) throws SQLException {
        String query = "{CALL insert_user(?,?,?)}";

        // try-with-resource statement will auto close the connection.
        try (
                Connection connection = getConnection();
                CallableStatement callableStatement = connection.prepareCall(query)
             ) {
            callableStatement.setString(1, user.getName());
            callableStatement.setString(2, user.getEmail());
            callableStatement.setString(3, user.getCountry());

            System.out.println(callableStatement);

            callableStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public void addUserTransaction(User user, int[] permissions) {
        Connection conn = null;

        // để chèn người dùng mới
        PreparedStatement pstmt = null;

        // để cấp quyền cho người dùng
        PreparedStatement pstmtAssignment  = null;

        // để lấy id người dùng
        ResultSet rs = null;
        try {
            conn = getConnection();
            // set auto commit to false
            conn.setAutoCommit(false);

            // Insert user
            pstmt = conn.prepareStatement(INSERT_USERS_SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getCountry());

            int rowAffected = pstmt.executeUpdate();

            // get user id
            rs = pstmt.getGeneratedKeys(); //Tạo khóa tự động tăng
            int userId = 0;
            if (rs.next())
                //trả về dữ liệu của chỉ mục cột được chỉ định của hàng hiện tại như int.
                userId = rs.getInt(1);

            // trong trường hợp thao tác chèn thành công, gán quyền cho người dùng
            if (rowAffected == 1) {

                // gán quyền truy cập cho người dùng
                String sqlPivot = "INSERT INTO user_permission(user_id,permission_id) "
                        + "VALUES(?,?)";

                pstmtAssignment  = conn.prepareStatement(sqlPivot);
                for (int permissionId : permissions) {
                    pstmtAssignment.setInt(1, userId); //cố định
                    pstmtAssignment.setInt(2, permissionId); //thay đổi theo mảng
                    pstmtAssignment.executeUpdate();
                }
                conn.commit();
            } else {
                conn.rollback();
            }

        } catch (SQLException ex) {
            // roll back the transaction
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (pstmtAssignment != null) pstmtAssignment.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
