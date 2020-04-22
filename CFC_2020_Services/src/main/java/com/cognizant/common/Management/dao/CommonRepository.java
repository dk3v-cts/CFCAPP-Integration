/**
 * 
 */
package com.cognizant.common.Management.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


/**
 * @author 487245
 *
 */
public interface CommonRepository extends CrudRepository<CommPost, Long> {

	List<CommPost> findByUserName(String userName);
}
