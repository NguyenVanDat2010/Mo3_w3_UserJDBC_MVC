package user.service;

import user.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserService {
    /**Thêm mới 1 bản ghi trong bảng*/
    void insertUser(User user) throws SQLException;

    /**Tìm kiếm 1 bản ghi trong bảng user theo id*/
    User selectUser(int id);

    /**Hiển thị toàn bộ bản ghi trong bảng user */
    List<User> selectAllUsers();

    /**Xóa 1 bản ghi trong bảng user */
    boolean deleteUser(int id) throws SQLException;

    /**Sủa 1 bản ghi trong bảng user */
    boolean updateUser(User user) throws SQLException;

    /** Tìm kiếm một bản ghi thông qua trường bất kỳ*/
//    User search(String value);

    /** Tìm kiếm tất cả các bản ghi có chứa giá trị cần tìm kiếm thông qua trường bất kỳ*/
    List<User> search(String value);

    List<User> sortUsers(String sortBy);

    //Procedure
    User getUserById(int id);
    //procedure
    void insertUserStore(User user) throws SQLException;

    //Transaction
    void addUserTransaction(User user, int[] permission);
}
