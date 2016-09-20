package no.uio.inf5750.assignment2.gui.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import no.uio.inf5750.assignment2.model.Course;
import no.uio.inf5750.assignment2.model.Student;
import no.uio.inf5750.assignment2.service.StudentSystem;

@RestController
@RequestMapping(value = "/api")
public class ApiController {
	
	@Autowired
	StudentSystem studentSystem;
	
	@RequestMapping(value = "/student", method = RequestMethod.GET)
	public Collection<Student> getAllStudents() {
		return studentSystem.getAllStudents();
	}
	
	@RequestMapping(value="/student/{student_id}/location", method = RequestMethod.GET)
	public Collection<Student> getLocation(@PathVariable int student_id, HttpServletRequest request) {
		String latitude = request.getParameter("latitude");
		String longitude = request.getParameter("longitude");
		studentSystem.setStudentLocation(student_id, latitude, longitude);		
		return studentSystem.getAllStudents();
	}
	
	@RequestMapping(value = "/course", method = RequestMethod.GET)
	public Collection<Course> getAllCourses() {
		return studentSystem.getAllCourses();	
	}
	
}
