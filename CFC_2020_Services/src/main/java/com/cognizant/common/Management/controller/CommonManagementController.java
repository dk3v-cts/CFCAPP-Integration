/**
 * 
 */
package com.cognizant.common.Management.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.common.Management.bean.CommonRequest;
import com.cognizant.common.Management.service.CommonManagementService;

/**
 * @author 487245
 *
 */
@RestController
public class CommonManagementController {
	
	@Autowired
	private CommonManagementService pCommonManagementService;
	
	@RequestMapping(method=RequestMethod.POST, path="/common/operation")
	public ResponseEntity<Map<String, Object>> commonPublish(@RequestBody CommonRequest pRequest) {
		Map<String,Object> responseMap = pCommonManagementService.commonManagementDetails(pRequest);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, path="/api/profile/deletePosts")
	public ResponseEntity<Map<String, Object>> deletePosts(@RequestBody CommonRequest pRequest) {
		Map<String,Object> responseMap = pCommonManagementService.deleteCommonManagementDetails(pRequest);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, path="/api/auth/signIn")
	public ResponseEntity<Map<String, Object>> login(@RequestBody CommonRequest pCommonRequest,HttpServletRequest request) {
		Map<String,Object> responseMap = pCommonManagementService.accountVerification(pCommonRequest,request);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}
	
	@RequestMapping(value="/api/auth/logout", method=RequestMethod.POST)  
    public ResponseEntity<Map<String, Object>> logoutPage(@RequestBody CommonRequest pCommonRequest,HttpServletRequest request, HttpServletResponse response) {  
		Map<String,Object> responseMap = pCommonManagementService.logout(pCommonRequest,request,response);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
    }
	
	@RequestMapping(method=RequestMethod.POST, path="/api/profile/getProfilePosts")
	public ResponseEntity<Map<String, Object>> getProfilePosts(@RequestBody CommonRequest pRequest) {
		Map<String,Object> responseMap = pCommonManagementService.getProfilePostsDetails(pRequest);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}
	
	//search
	@RequestMapping(method=RequestMethod.POST, path="/api/product/search")
	public ResponseEntity<Map<String, Object>> search(@RequestBody CommonRequest pCommonRequest,HttpServletResponse response) {
		Map<String,Object> responseMap = pCommonManagementService.getSearchDetailsResponse(pCommonRequest,response);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}
	

	@RequestMapping(method=RequestMethod.POST, path="/api/auth/notificationService")
	public ResponseEntity<Map<String, Object>> notificationDetails(@RequestBody CommonRequest pRequest) {
		Map<String,Object> responseMap = pCommonManagementService.saveInfoForNotification(pRequest);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}
	
	
	@RequestMapping(method=RequestMethod.POST, path="/api/product/getItem")
	public ResponseEntity<Map<String, Object>> getItem(@RequestBody CommonRequest pCommonRequest,HttpServletResponse response) {
		Map<String,Object> responseMap = pCommonManagementService.getSearchDetailsResponse(pCommonRequest,response);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}
	
	
}