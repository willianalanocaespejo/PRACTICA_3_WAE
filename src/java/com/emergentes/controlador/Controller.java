
package com.emergentes.controlador;

import com.emergentes.modelo.GestorProductos;
import com.emergentes.modelo.Producto;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "Controller", urlPatterns = {"/Controller"})
public class Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Producto objTarea = new Producto();
        int id;
        int pos;
        String op = request.getParameter("op");

        if (op.equals("nuevo")) {
            HttpSession ses = request.getSession();
            GestorProductos agenda = (GestorProductos) ses.getAttribute("agenda");
            objTarea.setId(agenda.obtieneId());
            request.setAttribute("op", op);
            request.setAttribute("miTarea", objTarea);
            request.getRequestDispatcher("editar.jsp").forward(request, response);

        }

        if (op.equals("modificar")) {
            id = Integer.parseInt(request.getParameter("id"));
            HttpSession ses = request.getSession();
            GestorProductos agenda = (GestorProductos) ses.getAttribute("agenda");
            pos = agenda.ubicarTarea(id);
            objTarea = agenda.getLista().get(pos);

            request.setAttribute("op", op);
            request.setAttribute("miTarea", objTarea);
            request.getRequestDispatcher("editar.jsp").forward(request, response);

        }
        if (op.equals("eliminar")) {
            id = Integer.parseInt(request.getParameter("id"));
            HttpSession ses = request.getSession();
            GestorProductos agenda = (GestorProductos) ses.getAttribute("agenda");
            pos = agenda.ubicarTarea(id);
            agenda.eliminarTarea(pos);
            ses.setAttribute("agenda", agenda);
            response.sendRedirect("index.jsp");

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Producto objProducto = new Producto();

        int pos;
        String op = request.getParameter("op");

        if (op.equals("grabar")) {

            //recuperar valores del formulario
            //verificar si es nuevo o es una modificacion
            objProducto.setId(Integer.parseInt(request.getParameter("id")));
            objProducto.setProducto(request.getParameter("producto"));
            objProducto.setPrecio(Double.parseDouble(request.getParameter("precio")));
            objProducto.setCantidad(Integer.parseInt(request.getParameter("cantidad")));

            HttpSession ses = request.getSession();
            GestorProductos agenda = (GestorProductos) ses.getAttribute("agenda");
            
            String opg = request.getParameter("opg");
            if (opg.equals("nuevo")){
                agenda.insertarTarea(objProducto);
                
            }else{
                pos = agenda.ubicarTarea(objProducto.getId());
                agenda.modificarTarea(pos, objProducto);
            }
            ses.setAttribute("agenda", agenda);
            response.sendRedirect("index.jsp");
        }

    }

}
