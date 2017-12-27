package com.ruizton.main.dao;

// default package

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fhedginginfo;
import com.ruizton.main.model.Fhedginginfo;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fhedginginfo entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Fhedginginfo
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FhedginginfoDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FhedginginfoDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String FTITLE = "ftitle";
	public static final String FRATE = "frate";

	public void save(Fhedginginfo transientInstance) {
		log.debug("saving Fhedginginfo instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fhedginginfo persistentInstance) {
		log.debug("deleting Fhedginginfo instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fhedginginfo findById(java.lang.Integer id) {
		log.debug("getting Fhedginginfo instance with id: " + id);
		try {
			Fhedginginfo instance = (Fhedginginfo) getSession().get(
					"com.ruizton.main.model.Fhedginginfo", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fhedginginfo> findByExample(Fhedginginfo instance) {
		log.debug("finding Fhedginginfo instance by example");
		try {
			List<Fhedginginfo> results = (List<Fhedginginfo>) getSession()
					.createCriteria("com.ruizton.main.model.Fhedginginfo")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fhedginginfo instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Fhedginginfo as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fhedginginfo> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<Fhedginginfo> findByFtitle(Object ftitle) {
		return findByProperty(FTITLE, ftitle);
	}

	public List<Fhedginginfo> findByFrate(Object frate) {
		return findByProperty(FRATE, frate);
	}

	public List findAll() {
		log.debug("finding all Fhedginginfo instances");
		try {
			String queryString = "from Fhedginginfo";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fhedginginfo merge(Fhedginginfo detachedInstance) {
		log.debug("merging Fhedginginfo instance");
		try {
			Fhedginginfo result = (Fhedginginfo) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fhedginginfo instance) {
		log.debug("attaching dirty Fhedginginfo instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fhedginginfo instance) {
		log.debug("attaching clean Fhedginginfo instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public List<Fhedginginfo> list(int firstResult, int maxResults, String filter,
			boolean isFY) {
		List<Fhedginginfo> list = null;
		log.debug("finding Fhedginginfo instance with filter");
		try {
			String queryString = "from Fhedginginfo " + filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if (isFY) {
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fhedginginfo name failed", re);
			throw re;
		}
		return list;
	}
}