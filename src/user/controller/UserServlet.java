package user.controller;

import user.model.User;
import user.service.DatabaseConnect;
import user.service.IUserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "UserServlet",urlPatterns = "/users")
public class UserServlet extends HttpServlet {
//    IUserService userService = new DatabaseConnect();

    private IUserService userService;
    public void init() {
        userService = new DatabaseConnect();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "create":
                    createUser(request, response);
                    break;
                case "edit":
                    editUser(request, response);
                    break;
//                case "delete":
//                    deleteUser(request, response);
//                    break;
                case "search":
                    searchUser(request,response);
                default:
                    break;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    /** Tìm kiếm và đưa ra các bản ghi bao gồm giá trị cần tìm*/
    private void searchUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String value = request.getParameter("searchValue");

//        User userSearch=this.userService.search(value);

        List<User> userSearch = this.userService.search(value);

        RequestDispatcher dispatcher;

        if (userSearch == null){
            dispatcher = request.getRequestDispatcher("error-404.jsp");
        }else {
            request.setAttribute("users",userSearch);
            dispatcher = request.getRequestDispatcher("user/search.jsp");
        }
        dispatcher.forward(request, response);
    }

    /** Cập nhật (update) một bản ghi vào bảng users */
    private void editUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id= Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");

        User userUpdate = new User(id,name,email,country);

        this.userService.updateUser(userUpdate);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
//        request.setAttribute("message", "Users was update");
//        dispatcher.forward(request, response);
        response.sendRedirect("/users");
    }

    /** Insert một bản ghi vào bảng users */
    private void createUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
//        int id = Integer.parseInt(request.getParameter("id"));

        User newUser = new User(name,email,country);

//        this.userService.insertUser(newUser);
        /** Gọi MySql Stored Procedures từ JDBCAssignment */
        this.userService.insertUserStore(newUser); //sử dụng PROCEDURE

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        request.setAttribute("message", "New user was created");

        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


//-------------------------------------------------------------------------------------------------------


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    showCreateForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                case "sort":
                    sortUserBy(request,response);
                    break;
                    //MySql JDBC TransactionAssignment
                case "permission":
                    addUserPermission(request, response);
                    break;
                    //Thực thi SQL không sử dụng Transaction
                case "test-without-tran":
                    testWithoutTran(request, response);
                    break;
                    //Thực thi SQL có sử dụng Transaction
                case "test-use-tran":
                    testUseTran(request, response);
                    break;
                default:
                    listUser(request, response);
                    break;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void testUseTran(HttpServletRequest request, HttpServletResponse response) {
        this.userService.insertUpdateUseTransaction();
    }

    private void testWithoutTran(HttpServletRequest request, HttpServletResponse response) {
        this.userService.insertUpdateWithoutTransaction();
    }

    private void addUserPermission(HttpServletRequest request, HttpServletResponse response) {
        User user = new User("Kien", "kienhoang@gmail.com", "vn");

        int[] permissions = {1, 2, 4};

        this.userService.addUserTransaction(user, permissions);
    }

    /** Hiển thị ra danh sách users của database */
    private void listUser(HttpServletRequest request, HttpServletResponse response) {
        List<User> userList = this.userService.selectAllUsers();

        request.setAttribute("users", userList);

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sortUserBy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String operator = request.getParameter("sortBy");

        System.out.println(operator);

        List<User> userList = this.userService.sortUsers(operator);

        request.setAttribute("users",userList);

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
//        response.sendRedirect("/users");
    }

    /** Xóa một bản ghi trong bảng Users bằng id*/
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        this.userService.deleteUser(id);

        List<User> userList = this.userService.selectAllUsers();
        request.setAttribute("users",userList);

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }

    /** Hiển thị ra form users và trong form hiển thị ra dữ liệu của bản ghi cần xóa */
//    private void showDeleteForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
//        int id = Integer.parseInt(request.getParameter("id"));
//        User userDelete = this.userService.selectUser(id);
//
//        RequestDispatcher dispatcher = request.getRequestDispatcher("user/delete.jsp");
//        request.setAttribute("user",userDelete);
//
//        dispatcher.forward(request, response);
//    }

    /** Hiển thị form edit users */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

//        User userExisting = this.userService.selectUser(id);
        /**Gọi MySql Stored Procedures từ JDBCAssignment*/
        User userExisting = this.userService.getUserById(id);//sử dụng PROCEDURE

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        request.setAttribute("user",userExisting);

        dispatcher.forward(request, response);
    }

    /** Hiển thị form insert bản ghi vào bảng users*/
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");

        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
