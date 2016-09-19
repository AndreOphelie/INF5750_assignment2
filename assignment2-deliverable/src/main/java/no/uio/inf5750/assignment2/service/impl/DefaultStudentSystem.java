package no.uio.inf5750.assignment2.service.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import no.uio.inf5750.assignment2.dao.CourseDAO;
import no.uio.inf5750.assignment2.dao.StudentDAO;
import no.uio.inf5750.assignment2.model.Course;
import no.uio.inf5750.assignment2.model.Student;
import no.uio.inf5750.assignment2.service.StudentSystem;

@Component
@Transactional
public class DefaultStudentSystem implements StudentSystem {
	
	@Autowired
	private StudentDAO studentDao;
	@Autowired
	private CourseDAO courseDao;
	
	/**
     * Adds a course.
     *
     * @param courseCode the course code of the course to add.
     * @param name the name of the course to add.
     * @return the generated id of the added course.
     */
	@Override
    public int addCourse(String courseCode, String name){
		if(name != null && !name.equals("") && courseCode != null && !courseCode.equals("")){
			Course course = new Course(courseCode,name);
			return courseDao.saveCourse(course);
		}
		return -1;
		
	}

    /**
     * Updates a course.
     *
     * @param courseId the id of the course to update.
     * @param courseCode the course code to update.
     * @param name the name to update.
     */
	@Override
    public void updateCourse(int courseId, String courseCode, String name){
		if (name != null && !name.equals("") && courseCode != null && !courseCode.equals("")) {
			Course course = courseDao.getCourse(courseId);
			course.setCourseCode(courseCode);
			course.setName(name);
			courseDao.saveCourse(course);
		}
	}

    /**
     * Returns a course.
     *
     * @param courseId the id of the course to return.
     * @return the course or null if it doesn't exist.
     */
	@Override
    public Course getCourse(int courseId){
		return courseDao.getCourse(courseId);
	}

    /**
     * Returns a course with a specific course code.
     *
     * @param courseCode the course code of the course to return.
     * @return the course code or null if it doesn't exist.
     */
	@Override
    public Course getCourseByCourseCode(String courseCode){
		return courseDao.getCourseByCourseCode(courseCode);
	}

    /**
     * Returns a course with a specific name.
     *
     * @param name the name of the course that needs to be found
     * @return the course code or null if it doesn't exist.
     */
	@Override
    public Course getCourseByName(String name){
		return courseDao.getCourseByName(name);
	}

    /**
     * Returns all courses.
     *
     * @return all courses or an empty Collection if no course exists.
     */
	@Override
    public Collection<Course> getAllCourses(){
		return courseDao.getAllCourses();
	}

    /**
     * Removes all references to the course from degrees and students and
     * deletes the course.
     *
     * @param courseId the id of the course to delete.
     */
	@Override
    public void delCourse(int courseId){
		//delete for the course for attendants
		Course course = getCourse(courseId);
		Set<Student> attendants = course.getAttendants();
		Object[] obj = attendants.toArray();
	    for(Object o : obj){
	    	Student student = new Student();
	    	student = (Student) o;
	    	removeAttendantFromCourse(courseId, student.getId());
	    }
	       	
		courseDao.delCourse(getCourse(courseId));
	}

    /**
     * Adds an attendant to a course and a course to a student.
     *
     * @param courseId the id of the course.
     * @param studentId the id of the student.
     */
	@Override
    public void addAttendantToCourse(int courseId, int studentId){
		Course course = getCourse(courseId);
		Student student = getStudent(studentId);
		///add the student to the attendants of the course
		Set<Student> attendants = course.getAttendants(); // get the actual list
		attendants.add(student); // add the student
		course.setAttendants(attendants); // set 
		///add the course to the courses of the student
		Set<Course> courses = student.getCourses(); // get the current list
		courses.add(course); // add the course
		student.setCourses(courses); // set the list
	}

    /**
     * Removes an attendant from a course and a course from a student.
     *
     * @param courseId the id of the course.
     * @param studentId the id of the student.
     */
	@Override
    public void removeAttendantFromCourse(int courseId, int studentId){
		Course course = getCourse(courseId);
		Student student = getStudent(studentId);
		///remove the student to the attendants of the course
		Set<Student> attendants = course.getAttendants(); // get the actual list
		attendants.remove(student); // remove the student
		course.setAttendants(attendants); // set 
		///remove the course to the courses of the student
		Set<Course> courses = student.getCourses(); // get the current list
		courses.remove(course); // remove the course
		student.setCourses(courses); // set the list
	}



    /**
     * Adds a student.
     *
     * @param name the name of the student to add.
     * @return the generated id of the added student.
     */
	@Override
    public int addStudent(String name){
		if (name != null && !name.equals("")){
			Student student = new Student(name);
			return studentDao.saveStudent(student);
		}
		return -1;
		
	}

    /**
     * Updates a student.
     *
     * @param studentId the id of the student to update.
     * @param name the name to update.
     */
	@Override
    public void updateStudent(int studentId, String name){
		if(name != null && !name.equals("")){
			Student student = studentDao.getStudent(studentId);
			student.setName(name);
			studentDao.saveStudent(student);
		}
	}

    /**
     * Returns a student.
     *
     * @param studentId the id of the student to return.
     * @return the student or null if it doesn't exist.
     */
	@Override
    public Student getStudent(int studentId){
		return studentDao.getStudent(studentId);
	}

    /**
     * Returns a student with a specific name.
     *
     * @param name the name of the student to return.
     * @return the student or null if it doesn't exist.
     */
	@Override
    public Student getStudentByName(String name){
		return studentDao.getStudentByName(name);
	}

    /**
     * Returns all students.
     *
     * @return all students or an empty Collection if no student exists.
     */
	@Override
    public Collection<Student> getAllStudents(){
		return studentDao.getAllStudents();
	}

    /**
     * Removes all references to the student from courses and deletes the
     * student.
     *
     * @param studentId the id of the student to delete.
     */
	@Override
    public void delStudent(int studentId){
		Student student = getStudent(studentId);
		Set<Course> courses = student.getCourses();
		Object[] obj = courses.toArray();
	    for(Object o : obj){
	    	Course course = new Course();
	    	course = (Course) o;
	    	removeAttendantFromCourse(course.getId(), studentId);
	    }
		
		studentDao.delStudent(student);
		
	}

	@Override
	public void setStudentLocation(int studentId, String latitude, String longitude) {
		Student student = studentDao.getStudent(studentId);
		student.setLatitude(latitude);
		student.setLongitude(longitude);
		studentDao.saveStudent(student);
	}
	
}
