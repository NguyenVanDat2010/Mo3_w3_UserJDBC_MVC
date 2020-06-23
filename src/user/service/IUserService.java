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
    User search(String value);
}
