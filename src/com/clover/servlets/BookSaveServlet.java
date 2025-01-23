package com.clover.servlets;

import com.clover.dto.Book;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

@WebServlet("/BookSaveServlet")
@MultipartConfig
public class BookSaveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1、设置 utf-8 编码
        // 设置 utf-8 编码，解决中文乱码问题
        request.setCharacterEncoding("UTF-8");

        // 2、接收普通文本
        // 当网页提交数据的表单设置了 enctype="multipart/form-data" 之后，不能直接使用 getParameter 接收文本数据
        // 需要在当前 Servlet 类添加 @MultipartConfig 用于处理非压缩提交的数据
        String id = request.getParameter("bookId");
        String name = request.getParameter("bookName");

        System.out.println("id:" + id);
        System.out.println("name:" + name);

        // 3、接收表单提交的图片
        // getPart("输入框的 name 属性")：接收表单中的文件
        Part bookImg = request.getPart("bookImg");
        System.out.println("bookImg:" + bookImg);
        // getParts(): 接收表单中所有文件
        //Collection<Part> parts = request.getParts();

        // 4、保存图片
        // a.获取 files 目录在 web 服务器上的路径（不是工作空间的路径）
        ServletContext servletContext = getServletContext();
        String dir = servletContext.getRealPath("/files");

        // b.给上传的文件重命名（不同用户有可能上传相同的图片，如果不重命名将导致文件覆盖）
        // 文件重命名后缀不能改变
        String header = bookImg.getHeader("Content-Disposition"); // 获取文件头
        System.out.println(header); // form-data; name="bookImg"; filename="千峰武汉（横板）.jpg"
        // 截取上传的文件的后缀名
        int begin = header.lastIndexOf(".");
        int end = header.lastIndexOf("\"");
        String ext = header.substring(begin, end);

        // 取名（时间毫秒数、UUID、雪花算法）
        // String fileName = System.currentTimeMillis() + ext; // 这个用的是时间毫秒
        String fileName = UUID.randomUUID().toString() + ext;  // 这个用的是 UUID

        // c.存储文件到目录
        bookImg.write(dir + "\\" + fileName);

        // 5.将图书信息保存到数据库
        Book book = new Book(id, name, "files/" + fileName);
        // 调用 BookDAO 将 book 对象保存到数据库
    }
}
