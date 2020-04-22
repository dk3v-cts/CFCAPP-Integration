/**
 * 
 */
package com.cognizant.common.Management.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cognizant.common.Management.bean.Request;
import com.cognizant.common.Management.service.constants.CommonConstants;

/**
 * @author 487245
 *
 */
@Repository
public class CommonRepositoryDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate; 
	
	@Autowired
	private CommonRepository commonRepository;
	
	private int id = 1000;
	public int saveCommonManagementDetails(Request pRequest) {  
		String postType = null;
		if (CommonConstants.PUBLISH.equalsIgnoreCase(pRequest.getRequestType())) {
			postType = CommonConstants.AVAILABLE;
		} else if (CommonConstants.REQUEST.equalsIgnoreCase(pRequest.getRequestType())) {
			postType = CommonConstants.POSTED;
		}
	    
		String query="insert into commpost values('"+id+"','"+pRequest.getLatitude()+"','"+pRequest.getLongitude()+"','"+pRequest.getRequestType()+"','"
	    +pRequest.getQuantity()+"','"+postType+"','"+pRequest.getSubType()+"','"+pRequest.getType()+"','"+pRequest.getUserName()+"')"; 
		
		id++;
	    return jdbcTemplate.update(query);
	}
	
	public List<CommPost> retrieveCommonManagementDetails(String pUserName) {
		return commonRepository.findByUserName(pUserName);
	}
	
	public void removeCommonManagementDetails(Long pId) {
		commonRepository.deleteById(pId);
	}

	public CommUsers verifyIdentity(Request pRequest) {
		CommUsers commUsers = null;
		String query="select * from commusers where email='"+pRequest.getUserName()+"'and password='"+pRequest.getPassword()+"'";
		try {
			commUsers = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(CommUsers.class));	
		} catch (EmptyResultDataAccessException  excep) {
			return null;
		}
		
		return commUsers;
		
	}
	
	public HashMap<String, List<CommPost>> retrieveCommonManagementDetails(Request pRequest) {
		HashMap<String, List<CommPost>> commPost = new HashMap<String, List<CommPost>>();
		// String query="SELECT ( 6371 * acos( cos(
		// radians("+pRequest.getLattitude()+") ) * cos( radians( lattitude ) )
		// * cos( radians( longitude ) - radians("+pRequest.getLongitude()+") )
		// + sin( radians("+pRequest.getLattitude()+") ) * sin( radians(
		// lattitude ) ) ) ) AS distance FROM COMMPOST HAVING distance < 25
		// ORDER BY distance LIMIT 0 , 20;";
		String query = null;
		if(StringUtils.isNullOrEmpty(pRequest.getType()) ||  StringUtils.isNullOrEmpty(pRequest.getSubType())){
			query = "SELECT * from COMMPOST";
		}else{
			query = "SELECT * from COMMPOST where product_Type='" + pRequest.getType() + "' and sub_type='"
					+ pRequest.getSubType() + "'";
		}
		List<Map<String, Object>> commpostRecs = jdbcTemplate.queryForList(query);
		for (Map commpostRec : commpostRecs) {
			CommPost resultSet = new CommPost();
			double theta = Double.parseDouble(pRequest.getLongitude())
					- Double.parseDouble((String) commpostRec.get("longitude"));
			double dist = Math.sin(Math.toRadians(Double.parseDouble(pRequest.getLatitude())))
					* Math.sin(Math.toRadians(Double.parseDouble((String) commpostRec.get("latitude"))))
					+ Math.cos(Math.toRadians(Double.parseDouble(pRequest.getLatitude())))
							* Math.cos(Math.toRadians(Double.parseDouble((String) commpostRec.get("latitude"))))
							* Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			dist = dist * 1.609344;
			if (dist <= 10) {
				resultSet.setId((Long) commpostRec.get("id"));
				resultSet.setLatitude((String) commpostRec.get("latitude"));
				resultSet.setLongitude((String) commpostRec.get("longitude"));
				resultSet.setPostType((String) commpostRec.get("post_type"));
				resultSet.setQuantity((String) commpostRec.get("quantity"));
				resultSet.setSubType((String) commpostRec.get("sub_type"));
				resultSet.setProductType((String) commpostRec.get("product_Type"));
				resultSet.setUserName((String) commpostRec.get("user_name"));
				if(commPost.containsKey((String) commpostRec.get("user_name"))){
					List<CommPost> list = commPost.get((String) commpostRec.get("user_name"));
					list.add(resultSet);
				}else{
					List<CommPost> commList = new ArrayList<CommPost>();
					commList.add(resultSet);
					commPost.put((String) commpostRec.get("user_name"), commList);
				}
			}
		}
		return commPost;
        }
	
	@SuppressWarnings("unused")
	public int saveNotificationDetails(Request pRequest) {
		/*
		 * String currentDate = pRequest.getCurrentDate();
		 * System.out.println("currentDate-->"+currentDate); Date submissionDate = null;
		 * try { if (null != submissionDate) { submissionDate = new
		 * SimpleDateFormat("dd/MM/yyyy").parse(currentDate); } } catch (ParseException
		 * parseExcep) { System.out.println(parseExcep.getMessage()); }
		 */
		
		String query="insert into commnotify values('"+id+"','"+pRequest.getLatitude()+"','"+pRequest.getLongitude()+"','"+pRequest.getPostedUserName()+"','"
			    +pRequest.getReceiverUserName()+"','"+new Timestamp(pRequest.getSubmittedDate().getTime())+"')"; 
				
		id++;
		return jdbcTemplate.update(query);
	}

	public List<CommNotify> retrieveNotificationDetails(String userName) {
		List<CommNotify> commNotify = new ArrayList<CommNotify>();
		String query = "select * from COMMNOTIFY where posted_user_name='"+userName+"'or receiver_user_name='"+userName+"'";
		System.out.println("Query--->"+query);
		List<Map<String, Object>> commnotifyRecs = jdbcTemplate.queryForList(query);
		System.out.println("***commnotifyRecs****"+commnotifyRecs.size());
		for (Map commnotifyRec : commnotifyRecs) {
			CommNotify resultSet = new CommNotify();
			resultSet.setId((Long) commnotifyRec.get("id"));
			resultSet.setPostedUserName((String)commnotifyRec.get("posted_user_name"));
			resultSet.setReceiverUserName((String) commnotifyRec.get("receiver_user_name"));
			resultSet.setLatitude((String) commnotifyRec.get("latitude"));
			resultSet.setLongitude((String) commnotifyRec.get("longitude"));
			resultSet.setSubmissionDate((Date) commnotifyRec.get("submission_date"));
			commNotify.add(resultSet);
		}
		return commNotify;
	}

	public int updateLoggedInState(Long id) {
		String query = "update commusers set loggedin_status='"+true+"'where id='"+id+"'";
		return jdbcTemplate.update(query); 
	}

	public int updateLoggedInState(String email) {
		String query = "update commusers set loggedin_status='"+false+"'where email='"+email+"'";
		return jdbcTemplate.update(query);
	}
}