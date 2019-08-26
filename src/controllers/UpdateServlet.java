package controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Tasks;
import models.validator.TaskValidator;
import utils.DBUtil;

/**
 * Servlet implementation class UpdateServlet
 */
@WebServlet("/update")
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String _token = (String)request.getParameter("_token");
        if(_token !=null && _token.equals(request.getSession().getId())){
            EntityManager em = DBUtil.createEntityManager();

            //セッションスコープからメッセージのIDを取得して、該当のIDのメッセージ1件のみをデータベースから取得
            Tasks t = em.find(Tasks.class , (Integer)(request.getSession().getAttribute("tasks_id")));

            //フォームの内容を各フィールドに上書き
            String content = request.getParameter("content");
            t.setContent(content);

            //更新日時のみ上書き
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            t.setUpdated_date(currentTime);

            //バリデーションを実行してエラーがあったら編集画面のフォームに戻る（
            //エラー変数にバリデータから取り出したエラー内容を格納）
            List<String> errors = TaskValidator.validate(t);
            if(errors.size() > 0){

                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("tasks", t);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/edit.jsp");
                rd.forward(request, response);

            }else{

                em.getTransaction().begin();
                em.getTransaction().commit();
                request.getSession().setAttribute("flush","更新が完了しました。");
                em.close();

                request.getSession().removeAttribute("tasks_id");

                response.sendRedirect(request.getContextPath() + "/index");
            }
        }



        }



}
