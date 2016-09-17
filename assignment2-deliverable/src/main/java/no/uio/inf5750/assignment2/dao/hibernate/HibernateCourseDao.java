package no.uio.inf5750.assignment2.dao.hibernate;

import java.util.Collection;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import no.uio.inf5750.assignment2.dao.CourseDAO;
import no.uio.inf5750.assignment2.model.Course;

@Transactional
public class HibernateCourseDao implements CourseDAO {
	
	static Logger logger = Logger.getLogger(HibernateCourseDao.class);
    private SessionFactory sessionFactory;
    
    public void setSessionFactory( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }
	
	/**
     * Persists a course. An unique id is generated if the object is persisted
     * for the first time, and which is both set in the given course object and
     * returned.
     * 
     * @param course the course to add for persistence.
     * @return the id of the course.
     */
	@Override
    public int saveCourse( Course course ){
    	return (Integer) sessionFactory.getCurrentSession().save(course);
    }

    /**
     * Returns a course.
     * 
     * @param id the id of the course to return.
     * @return the course or null if it doesn't exist.
     */
	@Override
    public Course getCourse( int id ){
    	return (Course) sessionFactory.getCurrentSession().get(Course.class, id);
    }

    /**
     * Returns a course with a specific course code.
     * 
     * @param courseCode the course code of the course to return.
     * @return the course code or null if it doesn't exist.
     */
	@Override
    public Course getCourseByCourseCode( String courseCode ){
		//session
		Session session = sessionFactory.getCurrentSession();
        
        // HQL query
        String qry = "from Course where courseCode= :courseCode";      
        Query query = session.createQuery(qry);
        query.setString("courseCode", courseCode);

        //get the result 
        return (Course) query.uniqueResult();
    	
    }

    /**
     * Returns a course with a specific name.
     * 
     * @param courseCode the course code of the course to return.
     * @return the course code or null if it doesn't exist.
     */
	@Override
    public Course getCourseByName( String name ){
		//session
		Session session = sessionFactory.getCurrentSession();
        
        // HQL query
        String qry = "from Course where name= :name";      
        Query query = session.createQuery(qry);
        query.setString("name", name);

        //get the result 
        return (Course) query.uniqueResult();
    }

    /**
     * Returns all courses.
     * 
     * @return all courses.
     */
	@Override
    public Collection<Course> getAllCourses(){
		//session
		Session session = sessionFactory.getCurrentSession();
        
        // HQL query
        String qry = "from Course";      
        Query query = session.createQuery(qry);

        //get the result 
        return query.list();
    }

    /**
     * Deletes a course.
     * 
     * @param course the course to delete.
     */
	@Override
    public void delCourse( Course course ){
		sessionFactory.getCurrentSession().delete(course);
    }
	
}
