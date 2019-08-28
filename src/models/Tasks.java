package models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


    @Entity
    @NamedQueries({
        @NamedQuery(
                name = "getAllTasks",
                query = "SELECT m FROM Tasks AS m ORDER BY m.id DESC"
                ),
        @NamedQuery(
                name = "getTasksCount",
                query = "SELECT COUNT(m) FROM Tasks AS m"
                )
    })
    @Table(name = "tasks")
    public class Tasks {
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "created_date", nullable = false)
        private Timestamp create_date;

        @Column(name = "updated_date", nullable = false)
        private Timestamp updated_date;

        @Column(name = "content" ,length = 225 , nullable = false)
        private String content;

        //getter/setter
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Timestamp getCreate_date() {
            return create_date;
        }

        public void setCreate_date(Timestamp create_date) {
            this.create_date = create_date;
        }

        public Timestamp getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(Timestamp updated_date) {
            this.updated_date = updated_date;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }





    }


