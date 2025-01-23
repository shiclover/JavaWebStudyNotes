package com.clover.servlets;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet("/FileDownloadServlet")
public class FileDownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1、接收客户端传递过来的文件名（即为要下载的文件）
        String fileName = request.getParameter("fileName");

        // 2、获取存储文件的 files 目录路径
        ServletContext servletContext = getServletContext();
        String dir = servletContext.getRealPath("/files");

        // 拼接要下载的文件路径
        String filePath = dir + "\\" + fileName;

        // 3、设置响应头
        response.setContentType("application/image"); // 设置响应的类型如果浏览器无法识别则会提示另存为
        response.addHeader("Content-Disposition", "attachment; filename=" + fileName);

        // 4、通过 IO 流将文件数据响应给浏览器（文件是二进制数据，所以获取字节流）
        ServletOutputStream outputStream = response.getOutputStream();
        // 读取服务文件，写给客户端
        FileInputStream fis = new FileInputStream(filePath);
        byte[] bs = new byte[1024];
        int len = -1;
        while ((len = fis.read(bs)) != -1) {
            // 将读取到的文件数据通过 outputStream 写给客户端
            outputStream.write(bs, 0, len);
        }
        outputStream.close();
        fis.close();
    }
}
