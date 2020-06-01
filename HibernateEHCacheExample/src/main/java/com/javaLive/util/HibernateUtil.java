package com.javaLive.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * @author JavaLive.com 
 * 	       One of the major benefit of using Hibernate in large
 *         application is it’s support for cache, hence reducing database
 *         queries and better performance. In earlier example, we looked into
 *         the Hibernate First Level Cache and today we will look into Hibernate
 *         Second Level Cache using Hibernate EHCache implementation.
 * 
 *         Hibernate Second Level cache providers include EHCache and
 *         Infinispan, but EHCache is more popular and we will use it for our
 *         example project. However before we move to our project, we should
 *         know different strategies for caching an object.
 * 
 *         Read Only: This caching strategy should be used for persistent
 *         objects that will always read but never updated. It’s good for
 *         reading and caching application configuration and other static data
 *         that are never updated. This is the simplest strategy with best
 *         performance because there is no overload to check if the object is
 *         updated in database or not. Read Write: It’s good for persistent
 *         objects that can be updated by the hibernate application. However if
 *         the data is updated either through backend or other applications,
 *         then there is no way hibernate will know about it and data might be
 *         stale. So while using this strategy, make sure you are using
 *         Hibernate API for updating the data. Nonrestricted Read Write: If the
 *         application only occasionally needs to update data and strict
 *         transaction isolation is not required, a nonstrict-read-write cache
 *         might be appropriate. Transactional: The transactional cache strategy
 *         provides support for fully transactional cache providers such as
 *         JBoss TreeCache. Such a cache can only be used in a JTA environment
 *         and you must specify hibernate.transaction.manager_lookup_class.
 *         
 *         Hibernate EHCache :- Since EHCache supports all the above cache
 *         strategies, it’s the best choice when you are looking for second
 *         level cache in hibernate.
 */
public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	private static SessionFactory buildSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			System.out.println("Hibernate Configuration loaded");

			serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			System.out.println("Hibernate serviceRegistry created");

			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

			return sessionFactory;
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null)
			sessionFactory = buildSessionFactory();
		return sessionFactory;
	}

	public static void shutdown() {
		if (serviceRegistry != null) {
			StandardServiceRegistryBuilder.destroy(serviceRegistry);
		}
	}
}
