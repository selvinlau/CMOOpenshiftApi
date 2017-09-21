package org.openshift.quickstarts.undertow.servlet;
import java.util.List;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class Student extends Model {

	private int student_id;
	private String f_name;
	private String l_name;

	final static String insertQuery = "INSERT INTO CMO.STUDENT (f_name,l_name) VALUES (:f_name,:l_name)";
	final static String getAllQuery = "SELECT * FROM CMO.STUDENT";

	public Student(int student_id, String f_name, String l_name) {
		super();
		this.student_id = student_id;
		this.f_name = f_name;
		this.l_name = l_name;
	}

	public Student(String f_name, String l_name) {
		super();
		this.f_name = f_name;
		this.l_name = l_name;
	}

	public Student() {
		super();
	}

	public int getStudent_id() {
		return student_id;
	}

	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}

	public String getF_name() {
		return f_name;
	}

	public void setF_name(String f_name) {
		this.f_name = f_name;
	}

	public String getL_name() {
		return l_name;
	}

	public void setL_name(String l_name) {
		this.l_name = l_name;
	}

	public static int createStudent(Student student) {
		int createdId;
		try (Connection conn = Student.getSql2o().beginTransaction()) {
			createdId = conn.createQuery(insertQuery).addParameter("f_name", student.getF_name()).addParameter("l_name", student.getL_name())
					.executeUpdate().getKey(Integer.class);
			conn.commit();
			return createdId;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 1;
	}
	
	public static List<Student> getAllStudent(){
		try (Connection conn = Student.getSql2o().open()) {
			List<Student> studentList= conn.createQuery(getAllQuery).executeAndFetch(Student.class);
			
			return studentList;
		}
	}
}
