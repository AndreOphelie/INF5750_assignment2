package no.uio.inf5750.assignment2.gui.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	
}
