package no.uio.inf5750.assignment2.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import no.uio.inf5750.assignment2.model.Course;
import no.uio.inf5750.assignment2.model.Student;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/META-INF/assignment2/beans.xml"})
public class StudentSystemTest {
	
	@Autowired 
	StudentSystem studentSystem;
	
	Course course;
	int course_id;
	
	@Before
	public void init(){
		course = new Course("INFtest","nameTest");
	}
	
	@Test
	public void addCourseTest(){
		int id = studentSystem.addCourse(course.getCourseCode(), course.getName());
		assertNotNull(id);
		assertEquals(course.getName(), studentSystem.getCourse(id).getName());		
	}
	
	@Test 
	public void addCourseWithoutNameExpectedNotCreationTest(){
		int id = studentSystem.addCourse("INFtest2", "");
		assertEquals(id, -1);
		assertNull(studentSystem.getCourse(id));	
	}
	
	@Test 
	public void addCourseWithoutCodeExpectedNotCreationTest(){
		int id = studentSystem.addCourse("", "nameTest");
		assertEquals(id, -1);
		assertNull(studentSystem.getCourse(id));	
	}
	
	@Test 
	public void updateCourseTest(){
		int id_new = studentSystem.addCourse("code", "name");
		studentSystem.updateCourse(id_new, "newCode", "newName");
		assertEquals("newName", studentSystem.getCourse(id_new).getName());
		assertEquals("newCode", studentSystem.getCourse(id_new).getCourseCode());
	}
	
	@Test 
	public void updateCourseWithoutNameExpectedNoModificationTest(){
		int id_new = studentSystem.addCourse("code2", "name2");
		studentSystem.updateCourse(id_new, "" , "");
		assertEquals("name2", studentSystem.getCourse(id_new).getName());
		assertEquals("code2", studentSystem.getCourse(id_new).getCourseCode());
	}	
	
	@Test 
	public void getCourseTest(){
		int id = studentSystem.addCourse("code4", "name4");
		assertEquals("name4", studentSystem.getCourse(id).getName());
		assertEquals("code4", studentSystem.getCourse(id).getCourseCode());	
	}
	
	@Test 
	public void getCourseNullTest(){
		assertNull(studentSystem.getCourse(1100));
	}
	
	@Test
	public void getCourseByCourseCodeTest(){
		int id = studentSystem.addCourse("code5", "name5");
		assertEquals("name5", studentSystem.getCourseByCourseCode("code5").getName());
		assertEquals(id, studentSystem.getCourseByCourseCode("code5").getId());	
	}
	
	@Test
	public void getCourseByNotValidCourseCodeTest(){
		assertNull(studentSystem.getCourseByCourseCode("code4UIGIG"));
	}
	
	@Test
	public void getCourseByNameTest(){
		int id = studentSystem.addCourse("code6", "name6");
		assertEquals("code6", studentSystem.getCourseByName("name6").getCourseCode());
		assertEquals(id, studentSystem.getCourseByName("name6").getId());	
	}
	
	@Test
	public void getCourseByNotValidNameTest(){
		assertNull(studentSystem.getCourseByName("eoqfgzqu"));	
	}
	
	@Test
	public void getAllCoursesTest(){
		int id1 = studentSystem.addCourse("code7", "name7");
		int id2 = studentSystem.addCourse("code8", "name8");
		List<Course> courses = (List<Course>) studentSystem.getAllCourses();
		assertEquals(2, courses.size());
		assertEquals("code7", courses.get(0).getCourseCode());
		assertEquals("name7", courses.get(0).getName());
		assertEquals("code8", courses.get(1).getCourseCode());
		assertEquals("name8", courses.get(1).getName());
		
	}
	
	@Test
	public void getAllCoursesNullTest(){
		Collection<Course> courses = studentSystem.getAllCourses();
		assertEquals(0, courses.size());
	}
	
	@Test
	public void delCourseTest(){
		int id = studentSystem.addCourse("code9", "name9");
		studentSystem.delCourse(id);
		assertNull(studentSystem.getCourse(id));
	}
	
	@Test
	@Transactional //(because of Lazy error)
	public void addAttendantToCourseTest(){
		int student_id = studentSystem.addStudent("nameStu");
		int course_id = studentSystem.addCourse("courseStu", "courseNameStu");
		studentSystem.addAttendantToCourse(course_id, student_id);
		
		Course my_course = studentSystem.getCourse(course_id);
		Student my_student = studentSystem.getStudent(student_id);
		
		Set<Student> attendants = my_course.getAttendants();
		Student student_c = (Student) attendants.toArray()[0];
		assertEquals(my_student,student_c);
		
		Set<Course> courses = my_student.getCourses();
		Course course_c = (Course) courses.toArray()[0];
		assertEquals(my_course,course_c);
	}
	
	@Test
	@Transactional //(because of Lazy error)
	public void removeAttendantFromCourse(){
		int student_id = studentSystem.addStudent("nameStu");
		int course_id = studentSystem.addCourse("courseStu", "courseNameStu");
		studentSystem.addAttendantToCourse(course_id, student_id);
		studentSystem.removeAttendantFromCourse(course_id, student_id);
		
		Course my_course = studentSystem.getCourse(course_id);
		Student my_student = studentSystem.getStudent(student_id);
		
		Set<Student> attendants = my_course.getAttendants();
		assertEquals(true,attendants.isEmpty());
		
		Set<Course> courses = my_student.getCourses();
		assertEquals(true,courses.isEmpty());
	}
	
	@Test
	public void addStudentTest(){
		int id = studentSystem.addStudent("name");
		assertNotNull(id);
		assertEquals("name", studentSystem.getStudent(id).getName());		
	}
	
	@Test 
	public void addStudentWithoutNameExpectedNotCreationTest(){
		int id = studentSystem.addStudent("");
		assertEquals(id, -1);
		assertNull(studentSystem.getStudent(id));	
	}
	
	@Test 
	public void updateStudentTest(){
		int id_new = studentSystem.addStudent("name2");
		studentSystem.updateStudent(id_new, "newName");
		assertEquals("newName", studentSystem.getStudent(id_new).getName());
	}
	
	@Test 
	public void updateStudentWithoutNameExpectedNoModificationTest(){
		int id_new = studentSystem.addStudent("name3");
		studentSystem.updateStudent(id_new, "");
		assertEquals("name3", studentSystem.getStudent(id_new).getName());
	}	
	
	@Test 
	public void getStudentTest(){
		int id = studentSystem.addStudent("name4");
		assertEquals("name4", studentSystem.getStudent(id).getName());
	}
	
	@Test 
	public void getStudentNullTest(){
		assertNull(studentSystem.getStudent(1100));
	}
	
	@Test
	public void getStudentByNameTest(){
		int id = studentSystem.addStudent("name5");
		assertEquals(id, studentSystem.getStudentByName("name5").getId());	
	}
	
	@Test
	public void getStudentByNotValidNameTest(){
		assertNull(studentSystem.getStudentByName("eoqfgzqu"));	
	}
	
	@Test
	public void getAllStudentsTest(){
		int id1 = studentSystem.addStudent("name7");
		int id2 = studentSystem.addStudent("name8");
		List<Student> students = (List<Student>) studentSystem.getAllStudents();
		assertEquals(2, students.size());
		assertEquals("name7", students.get(0).getName());
		assertEquals("name8", students.get(1).getName());
	}
	
	@Test
	public void getAllStudentNullTest(){
		Collection<Student> students = studentSystem.getAllStudents();
		assertEquals(0, students.size());
	}
	
	@Test
	public void delStudentTest(){
		int id = studentSystem.addStudent("name6");
		studentSystem.delStudent(id);
		assertNull(studentSystem.getStudent(id));
	}
	
	@Test 
	public void setStudentLocationTest(){
		int id = studentSystem.addStudent("edgar");
		studentSystem.setStudentLocation(id, "4", "3");
		assertEquals("4", studentSystem.getStudent(id).getLatitude());
		assertEquals("3", studentSystem.getStudent(id).getLongitude());
	}
	
	@Test 
	public void getStudentWithLocationTest(){
		int id = studentSystem.addStudent("name7");
		studentSystem.setStudentLocation(id, "4", "3");
		assertEquals("name7", studentSystem.getStudent(id).getName());
		assertEquals("4", studentSystem.getStudent(id).getLatitude());
		assertEquals("3", studentSystem.getStudent(id).getLongitude());
	}
	
	@After
	public void finish(){
		Collection<Course> courses = studentSystem.getAllCourses();
		Collection<Student> students = studentSystem.getAllStudents();
		for(Course c : courses){
			studentSystem.delCourse(c.getId());
		}
		for(Student s : students){
			studentSystem.delStudent(s.getId());
		}
	}
}
