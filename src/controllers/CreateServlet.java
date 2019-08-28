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
 * Servlet implementation class CreateServlet
 */
@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())){

            EntityManager em = DBUtil.createEntityManager();

            Tasks t = new Tasks();

            //フォームの内容を各フィールドに上書き(新規登録)
           String content = request.getParameter("content");
            t.setContent(content);

            //Timestampクラスのインスタンス生成
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            t.setCreate_date(currentTime);
            t.setUpdated_date(currentTime);


            //バリデーションを実行してエラーがあったら新規登録のフォームに戻る
            //（エラー変数にバリデータから取り出したエラー内容を格納）

            List<String> errors = TaskValidator.validate(t);
            if(errors.size() > 0){
                em.close();
                //フォームに初期値を設定、さらにエラーメッセージを送る
                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("tasks", t);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/new.jsp");
                rd.forward(request, response);

            }else{
                //データベースに保存
                em.getTransaction().begin();
                em.persist(t);
                em.getTransaction().commit();
                request.getSession().setAttribute("flush", "登録が完了しました。");
                em.close();

                //indexのページにリダイレクト
                response.sendRedirect(request.getContextPath() + "/index");
                 }//エラーがあるかないかのif-else終了


            }

    }

}
