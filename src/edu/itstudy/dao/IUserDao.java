package edu.itstudy.dao;

import edu.itstudy.bean.productbean;
import edu.itstudy.bean.userbean;
import edu.itstudy.bean.medicinebean;

import java.util.*;
public interface IUserDao {
	
	public int register(userbean user1);//��װuserbean����
	public userbean login(userbean user1);
	public String forgetPassword(userbean user1);
	public List<productbean> getAllinfos();
	public List<userbean> getAllUserinfos();
//	public List<userbean> getSpecialUserInfo();
	public List<medicinebean> getMedicineInfo();
	public List<productbean> getSearchInfo(productbean product);
	public productbean getProductById(int id);
	public int updateproduct(productbean product);
	public int delete(int uid,int isD);
	public int addProduct(productbean product);

}
