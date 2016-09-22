package no.uio.inf5750.assignment2.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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
		//init a course
		course = new Course("INFtest","nameTest");
	}
	
	@Test
	public void addCourseTest(){
		//add a course
		int id = studentSystem.addCourse(course.getCourseCode(), course.getName());
		//assert it returns a non null id
		assertNotNull(id);
		//assert we can get it in the database
		assertEquals(course.getName(), studentSystem.getCourse(id).getName());		
	}
	
	@Test 
	public void addCourseWithoutNameExpectedNotCreationTest(){
		//assert we don't add a course in the database if we have an empty attribute
		int id = studentSystem.addCourse("INFtest2", "");
		assertEquals(id, -1);
		assertNull(studentSystem.getCourse(id));	
	}
	
	@Test 
	public void addCourseWithoutCodeExpectedNotCreationTest(){
		//assert we don't add a course in the database if we have an empty attribute
		int id = studentSystem.addCourse("", "nameTest");
		assertEquals(id, -1);
		assertNull(studentSystem.getCourse(id));	
	}
	
	@Test 
	public void updateCourseTest(){
		//assert we have update the course
		int id_new = studentSystem.addCourse("code", "name");
		studentSystem.updateCourse(id_new, "newCode", "newName");
		assertEquals("newName", studentSystem.getCourse(id_new).getName());
		assertEquals("newCode", studentSystem.getCourse(id_new).getCourseCode());
	}
	
	@Test 
	public void updateCourseWithoutNameExpectedNoModificationTest(){
		//assert we don't update a course in the database if we have an empty attribute
		int id_new = studentSystem.addCourse("code2", "name2");
		studentSystem.updateCourse(id_new, "" , "");
		assertEquals("name2", studentSystem.getCourse(id_new).getName());
		assertEquals("code2", studentSystem.getCourse(id_new).getCourseCode());
	}	
	
	@Test 
	public void getCourseTest(){
		//add a course and check we can get it
		int id = studentSystem.addCourse("code4", "name4");
		assertEquals("name4", studentSystem.getCourse(id).getName());
		assertEquals("code4", studentSystem.getCourse(id).getCourseCode());	
	}
	
	@Test 
	public void getCourseNullTest(){
		//assert we don't get something if giving non existing id
		assertNull(studentSystem.getCourse(1100));
	}
	
	@Test
	public void getCourseByCourseCodeTest(){
		//add a course and check we get it by code
		int id = studentSystem.addCourse("code5", "name5");
		assertEquals("name5", studentSystem.getCourseByCourseCode("code5").getName());
		assertEquals(id, studentSystem.getCourseByCourseCode("code5").getId());	
	}
	
	@Test
	public void getCourseByNotValidCourseCodeTest(){
		//check we don't get anything when asking for non existing code
		assertNull(studentSystem.getCourseByCourseCode("code4UIGIG"));
	}
	
	@Test
	public void getCourseByNameTest(){
		//add a course and check we get it by name
		int id = studentSystem.addCourse("code6", "name6");
		assertEquals("code6", studentSystem.getCourseByName("name6").getCourseCode());
		assertEquals(id, studentSystem.getCourseByName("name6").getId());	
	}
	
	@Test
	public void getCourseByNotValidNameTest(){
		//check if null when not valid name
		assertNull(studentSystem.getCourseByName("eoqfgzqu"));	
	}
	
	@Test
	public void getAllCoursesTest(){
		//add courses and check we all get them
		studentSystem.addCourse("code7", "name7");
		studentSystem.addCourse("code8", "name8");
		List<Course> courses = (List<Course>) studentSystem.getAllCourses();
		assertEquals(2, courses.size());
		assertEquals("code7", courses.get(0).getCourseCode());
		assertEquals("name7", courses.get(0).getName());
		assertEquals("code8", courses.get(1).getCourseCode());
		assertEquals("name8", courses.get(1).getName());
		
	}
	
	@Test
	public void getAllCoursesNullTest(){
		//check we have an empty list when no course in db
		Collection<Course> courses = studentSystem.getAllCourses();
		assertEquals(0, courses.size());
	}
	
	@Test
	public void delCourseTest(){
		//add a course and delete it and check it's gone
		int id = studentSystem.addCourse("code9", "name9");
		studentSystem.delCourse(id);
		assertNull(studentSystem.getCourse(id));
	}
	
	@Test
	@Transactional //(because of Lazy error)
	public void addAttendantToCourseTest(){
		//add course and student, link them
		int student_id = studentSystem.addStudent("nameStu");
		int course_id = studentSystem.addCourse("courseStu", "courseNameStu");
		studentSystem.addAttendantToCourse(course_id, student_id);
		
		Course my_course = studentSystem.getCourse(course_id);
		Student my_student = studentSystem.getStudent(student_id);
		
		//get the set of attendant of the course and check if the student is there
		Set<Student> attendants = my_course.getAttendants();
		Student student_c = (Student) attendants.toArray()[0];
		assertEquals(my_student,student_c);
		
		//get the set of courses of the student and check if the course is there
		Set<Course> courses = my_student.getCourses();
		Course course_c = (Course) courses.toArray()[0];
		assertEquals(my_course,course_c);
	}
	
	@Test
	@Transactional //(because of Lazy error)
	public void removeAttendantFromCourse(){
		// add a student, course and add attendant to course
		int student_id = studentSystem.addStudent("nameStu");
		int course_id = studentSystem.addCourse("courseStu", "courseNameStu");
		studentSystem.addAttendantToCourse(course_id, student_id);
		//remove
		studentSystem.removeAttendantFromCourse(course_id, student_id);
		
		Course my_course = studentSystem.getCourse(course_id);
		Student my_student = studentSystem.getStudent(student_id);
		
		//check if the student is gone
		Set<Student> attendants = my_course.getAttendants();
		assertEquals(true,attendants.isEmpty());
		
		//check if the course is gone
		Set<Course> courses = my_student.getCourses();
		assertEquals(true,courses.isEmpty());
	}
	
	@Test
	public void addStudentTest(){
		//add a student
		int id = studentSystem.addStudent("name");
		//check if the returned id is not null
		assertNotNull(id);
		//check if we have the student
		assertEquals("name", studentSystem.getStudent(id).getName());		
	}
	
	@Test 
	public void addStudentWithoutNameExpectedNotCreationTest(){
		//check if not adding with empty attribute
		int id = studentSystem.addStudent("");
		assertEquals(id, -1);
		assertNull(studentSystem.getStudent(id));	
	}
	
	@Test 
	public void updateStudentTest(){
		//check updating
		int id_new = studentSystem.addStudent("name2");
		studentSystem.updateStudent(id_new, "newName");
		assertEquals("newName", studentSystem.getStudent(id_new).getName());
	}
	
	@Test 
	public void updateStudentWithoutNameExpectedNoModificationTest(){
		//check if no modification when updating with empty attributes
		int id_new = studentSystem.addStudent("name3");
		studentSystem.updateStudent(id_new, "");
		assertEquals("name3", studentSystem.getStudent(id_new).getName());
	}	
	
	@Test 
	public void getStudentTest(){
		//add and check if we get the student
		int id = studentSystem.addStudent("name4");
		assertEquals("name4", studentSystem.getStudent(id).getName());
	}
	
	@Test 
	public void getStudentNullTest(){
		//check if null when getting a student with wrong id
		assertNull(studentSystem.getStudent(1100));
	}
	
	@Test
	public void getStudentByNameTest(){
		//add a student and check we get it by name
		int id = studentSystem.addStudent("name5");
		assertEquals(id, studentSystem.getStudentByName("name5").getId());	
	}
	
	@Test
	public void getStudentByNotValidNameTest(){
		//check we get null when get by wrong name
		assertNull(studentSystem.getStudentByName("eoqfgzqu"));	
	}
	
	@Test
	public void getAllStudentsTest(){
		//add students and check we get all of them
		studentSystem.addStudent("name7");
		studentSystem.addStudent("name8");
		List<Student> students = (List<Student>) studentSystem.getAllStudents();
		assertEquals(2, students.size());
		assertEquals("name7", students.get(0).getName());
		assertEquals("name8", students.get(1).getName());
	}
	
	@Test
	public void getAllStudentNullTest(){
		//check we get empty list when no student in db
		Collection<Student> students = studentSystem.getAllStudents();
		assertEquals(0, students.size());
	}
	
	@Test
	public void delStudentTest(){
		//add a student and delete it and check it's gone
		int id = studentSystem.addStudent("name6");
		studentSystem.delStudent(id);
		assertNull(studentSystem.getStudent(id));
	}
	
	@Test 
	public void setStudentLocationTest(){
		//add a student, set his location and check it updated the location
		int id = studentSystem.addStudent("edgar");
		studentSystem.setStudentLocation(id, "4", "3");
		assertEquals("4", studentSystem.getStudent(id).getLatitude());
		assertEquals("3", studentSystem.getStudent(id).getLongitude());
	}
	
	@Test 
	public void getStudentWithLocationTest(){
		//add a student, set its location and check we get it correctly
		int id = studentSystem.addStudent("name7");
		studentSystem.setStudentLocation(id, "4", "3");
		assertEquals("name7", studentSystem.getStudent(id).getName());
		assertEquals("4", studentSystem.getStudent(id).getLatitude());
		assertEquals("3", studentSystem.getStudent(id).getLongitude());
	}
	
	@After
	public void finish(){
		//delete courses and students so tests don't interfere 
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
