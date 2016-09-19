package no.uio.inf5750.assignment2.dao.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import no.uio.inf5750.assignment2.dao.StudentDAO;
import no.uio.inf5750.assignment2.model.Student;

@Transactional(propagation = Propagation.MANDATORY)
public class HibernateStudentDao implements StudentDAO{
	
	static Logger logger = Logger.getLogger(HibernateCourseDao.class);
    private SessionFactory sessionFactory;
    
    public void setSessionFactory( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }
	
	/**
     * Persists a student. An unique id is generated if the object is persisted
     * for the first time, and which is both set in the given student object and
     * returned.
     * 
     * @param student the student to add for persistence.
     * @return the id of the student.
     */
	@Override
    public int saveStudent( Student student ){
		return (Integer) sessionFactory.getCurrentSession().save(student);
    }

    /**
     * Returns a student.
     * 
     * @param id the id of the student to return.
     * @return the student or null if it doesn't exist.
     */
    @Override
    public Student getStudent( int id ){
    	return (Student) sessionFactory.getCurrentSession().get(Student.class, id);
    }

    /**
     * Returns a student with a specific name.
     * 
     * @param name the name of the student to return.
     * @return the student or null if it doesn't exist.
     */
    @Override
    public Student getStudentByName( String name ){
    	//session
		Session session = sessionFactory.getCurrentSession();
        
        // HQL query
        String qry = "from Student where name= :name";      
        Query query = session.createQuery(qry);
        query.setString("name", name);

        //get the result 
        return (Student) query.uniqueResult();
    }

    /**
     * Returns all students.
     * 
     * @return all students.
     */
    @Override
    public Collection<Student> getAllStudents(){  	
    	//session
		Session session = sessionFactory.getCurrentSession();
        
        // HQL query
        String qry = "from Student";      
        Query query = session.createQuery(qry);

        //get the result 
        return query.list();
    }

    /**
     * Deletes a student.
     * 
     * @param student the student to delete.
     */
    @Override
    public void delStudent( Student student ){
    	sessionFactory.getCurrentSession().delete(student);
    }
}
