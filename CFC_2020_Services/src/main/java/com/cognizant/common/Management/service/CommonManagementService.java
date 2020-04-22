/**
 * 
 */
package com.cognizant.common.Management.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.common.Management.bean.CommonRequest;
import com.cognizant.common.Management.bean.Request;
import com.cognizant.common.Management.dao.CommNotify;
import com.cognizant.common.Management.dao.CommPost;
import com.cognizant.common.Management.dao.CommUsers;
import com.cognizant.common.Management.dao.CommonRepositoryDao;
import com.cognizant.common.Management.service.constants.CommonConstants;

/**
 * @author 487245
 *
 */
@Service
public class CommonManagementService {

	@Autowired
	private CommonRepositoryDao commonRepositoryDao;

	Logger log = LoggerFactory.getLogger(CommonManagementService.class);

	public Map<String, Object> commonManagementDetails(CommonRequest pRequest) {
		log.debug("commonManagementDetails log");
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> serviceResponseMap = new HashMap<String, Object>();
		// Map<String,Object> detailsMap = new HashMap<String,Object>();

		Request request = pRequest.getRequest();
		String requestName = request.getRequestType();

		log.debug(requestName);
		if (CommonConstants.PUBLISH.equals(requestName) || CommonConstants.REQUEST.equals(requestName)) {
			int dbUpdateStatus = commonRepositoryDao.saveCommonManagementDetails(pRequest.getRequest());
			System.out.println("dbUpdateStatus :: -> " + dbUpdateStatus);
			serviceResponseMap.put("status", getStatusResponse("0000", "SUCCESS"));
			responseMap.put("response", serviceResponseMap);
		} 
		return responseMap;
	}

	/**
	 * Gets the status response.
	 *
	 * @param code    the code
	 * @param message the message
	 * @return the status response
	 */
	public Map<String, String> getStatusResponse(String code, String message) {
		Map<String, String> statusResponse = new HashMap<String, String>();
		statusResponse.put("statusCode", code);
		statusResponse.put("statusDesc", message);
		return statusResponse;
	}

	public Map<String, Object> getSearchDetailsResponse(CommonRequest pCommonRequest, HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> serviceResponseMap = new HashMap<String, Object>();
		Map<String, Object> detailsResponse = new HashMap<String, Object>();
		Request lRequest = pCommonRequest.getRequest();
		System.out.println("search");
		Map<String, List<CommPost>>  commPost = (Map<String, List<CommPost>>) commonRepositoryDao.retrieveCommonManagementDetails(lRequest);
		if (!commPost.isEmpty()) {
			serviceResponseMap.put("details", commPost);
			serviceResponseMap.put("status", getStatusResponse("0000", "Success"));
			}
		else {
			serviceResponseMap.put("status", getStatusResponse("9999", "Sorry.! No Donor Available. Please raise request"));
			serviceResponseMap.put("details", detailsResponse);
		}
		responseMap.put("response", serviceResponseMap);
		return responseMap;
	}


	public Map<String, Object> removeUser(Request pRequest) {
		Long commPostId = pRequest.getId();
		Map<String, Object> detailsResponse = new HashMap<String, Object>();
		List<CommPost> commPost = commonRepositoryDao.retrieveCommonManagementDetails(pRequest.getUserName());
		for(CommPost comm: commPost) {
			if (commPostId.equals(comm.getId())) {
				commonRepositoryDao.removeCommonManagementDetails(commPostId);
				detailsResponse.put("id", commPostId);
				break;
			}
		}
		return detailsResponse;
	}

	public Map<String, Object> accountVerification(CommonRequest pCommonRequest, HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> serviceResponseMap = new HashMap<String, Object>();
		Map<String, Object> detailsResponse = new HashMap<String, Object>();
		Request lRequest = pCommonRequest.getRequest();

		CommUsers userDetails = commonRepositoryDao.verifyIdentity(lRequest);
		if (null != userDetails) {
			String token = setCookie(userDetails.getId(), request);
			int dbUpdateStaus = commonRepositoryDao.updateLoggedInState(userDetails.getId());
			System.out.println("dbUpdateStaus::"+dbUpdateStaus);
			detailsResponse.put("userName", userDetails.getEmail());
			detailsResponse.put("name", userDetails.getUserName());
			detailsResponse.put("token", token);
			serviceResponseMap.put("details", detailsResponse);
			serviceResponseMap.put("status", getStatusResponse("0000", "Success"));
		} else {
			serviceResponseMap.put("header", getStatusResponse("9999", "User Authentication Failed"));
		}
		responseMap.put("response", serviceResponseMap);
		return responseMap;
	}

	private String setCookie(Long pId, HttpServletRequest request) {
		Date currentDate = new Date();
		String cookieValue = null;
		
		
		String cv = Long.toString(pId) + currentDate;

		cookieValue = encryptWithAESCBC(cv, "6603234562749567");

		try {
			cookieValue = URLEncoder.encode(cookieValue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * Cookie cookie = new Cookie("USER_COOKIE", cookieValue);
		 * cookie.setMaxAge(15552000); cookie.setPath("/"); cookie.setSecure(true);
		 * request.addCookie(cookie);
		 */
		@SuppressWarnings("unchecked")
		List<String> messages = (List<String>)request.getSession().getAttribute("CFC_SESSION");
		
		if (messages == null) {
			messages = new ArrayList<String>();
			request.getSession().setAttribute("CFC_SESSION", messages);
		}
		messages.add(cookieValue);
		request.getSession().setAttribute("CFC_SESSION", messages);

		return cookieValue;
	}

	private String encryptWithAESCBC(String pDataString, String pKey) {

		try {
			byte[] keyBytes = pKey.getBytes("ASCII");
			SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
			Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ivspec = new IvParameterSpec(pKey.getBytes("UTF-8"));
			ecipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

			byte[] dataStringBytes = null;
			byte[] encryptedDataStringBytes = null;

			if (pDataString != null) {
				dataStringBytes = pDataString.getBytes("UTF-8");
				encryptedDataStringBytes = ecipher.doFinal(dataStringBytes);
			}
			return new sun.misc.BASE64Encoder().encode(encryptedDataStringBytes);
		} catch (UnsupportedEncodingException unsuppExcep) {
			System.out.println(unsuppExcep.getMessage());
		} catch (NoSuchAlgorithmException noSuchAlgExcep) {
			System.out.println(noSuchAlgExcep.getMessage());
		} catch (NoSuchPaddingException noSuchPadExcep) {
			System.out.println(noSuchPadExcep.getMessage());
		} catch (InvalidKeyException invalidKeyExcep) {
			System.out.println(invalidKeyExcep.getMessage());
		} catch (InvalidAlgorithmParameterException invalidAlgExcep) {
			System.out.println(invalidAlgExcep);
		} catch (IllegalBlockSizeException illegalBlockExcep) {
			System.out.println(illegalBlockExcep.getMessage());
		} catch (BadPaddingException badPadExcep) {
			System.out.println(badPadExcep.getMessage());
		}
		return null;
	}

	public Map<String, Object> logout(CommonRequest pCommonRequest, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> serviceResponseMap = new HashMap<String, Object>();
		Map<String, Object> detailsResponse = new HashMap<String, Object>();

		HttpSession session = request.getSession(false);
		session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if(cookie.getName().equalsIgnoreCase("nickname")){
				cookie.setMaxAge(0);
			}
		}

		int dpUpdateStatus = commonRepositoryDao.updateLoggedInState(pCommonRequest.getRequest().getUserName());
		System.out.println("dbUpdateStatus"+dpUpdateStatus);
		detailsResponse.put("logout", pCommonRequest.getRequest().getUserName());
		serviceResponseMap.put("details", detailsResponse);
		serviceResponseMap.put("status", getStatusResponse("0000", "Success"));
		responseMap.put("response", serviceResponseMap);
		return responseMap;
	}
	
	public Map<String, Object> getProfilePostsDetails(CommonRequest pRequest) {
		log.debug("getProfilePostsDetails log");
		Map<String,Object> responseMap = new HashMap<String,Object>();
		Map<String,Object> serviceResponseMap = new HashMap<String,Object>();
		//Map<String,Object> detailsMap = new HashMap<String,Object>();
		
		Request request = pRequest.getRequest();
		
		//log.debug(requestName);
		serviceResponseMap.put("status", getStatusResponse("0000", "SUCCESS"));
		serviceResponseMap.put("details", getProfilePostsResponse(request));
		responseMap.put("response", serviceResponseMap);
		return responseMap;
	}
	
	public Map<String, Object> getProfilePostsResponse(Request pRequest) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<CommPost> commPost = commonRepositoryDao.retrieveCommonManagementDetails(pRequest.getUserName());
		List<CommNotify> commNotify = commonRepositoryDao.retrieveNotificationDetails(pRequest.getUserName());
		System.out.println("commNotify"+commNotify);
		List<Map<String, Object>> detailsList = new ArrayList<Map<String, Object>>();
		for(CommPost comm: commPost) {
			Map<String, Object> details = new HashMap<String, Object>();
			details.put("username", comm.getUserName());
			//details.put("name", comm.getName());
			details.put("latitude", comm.getLatitude());
			details.put("longitude", comm.getLongitude());
			details.put("productType", comm.getProductType());
			details.put("subType", comm.getSubType());
			details.put("quantity", comm.getQuantity());
			details.put("postType", comm.getPostType());
			details.put("id", comm.getId());
			detailsList.add(details);
		}
		
		List<Map<String,Object>> notifcationList = new
		ArrayList<Map<String,Object>>(); for (CommNotify notify: commNotify) {
		Map<String,Object> notifyDetails = new HashMap<String,Object>();
		notifyDetails.put("postedUserName", notify.getPostedUserName());
		notifyDetails.put("receiverUserName", notify.getReceiverUserName());
		notifyDetails.put("latitude", notify.getLatitude());
		notifyDetails.put("longitude", notify.getLongitude());
		notifyDetails.put("notficationDate", notify.getSubmissionDate());
		notifcationList.add(notifyDetails); }
		response.put("notificationDetails",notifcationList);
				 
		response.put("profileDetails", detailsList);
		return response;
	}
	public Map<String, Object> saveInfoForNotification(CommonRequest pCommRequest) {
		Map<String,Object> serviceResponseMap = new HashMap<String,Object>();
		Map<String,Object> responseMap = new HashMap<String,Object>();
		int dbUpdateStatus = commonRepositoryDao.saveNotificationDetails(pCommRequest.getRequest());
		System.out.println("dbUpdateStatus :: -> " + dbUpdateStatus);
		serviceResponseMap.put("status", getStatusResponse("0000", "SUCCESS"));
		responseMap.put("response", serviceResponseMap);
		return responseMap;
	}

	public Map<String, Object> deleteCommonManagementDetails(CommonRequest pRequest) {
		Map<String,Object> serviceResponseMap = new HashMap<String,Object>();
		Map<String,Object> responseMap = new HashMap<String,Object>();
		serviceResponseMap.put("status", getStatusResponse("0000", "SUCCESS"));
		serviceResponseMap.put("details", removeUser(pRequest.getRequest()));
		responseMap.put("response", serviceResponseMap);
		return responseMap;
	}

}