package edu.itstudy.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import edu.itstudy.bean.medicinebean;
import edu.itstudy.bean.productbean;
import edu.itstudy.bean.userbean;
import edu.itstudy.dao.IUserDao;
import edu.itstudy.utils.DBContil;

public class IUserdaoImpl implements IUserDao {
//注册
	@Override
	public int register(userbean user1) {
		Connection conn = DBContil.getConn();
		int res = 0;
		String sql = "insert into userinfo ( name , password , Apassword , email , brotherName)  values( ? , ? , ?  , ? , ? )";
		String sql2 = "select name from userinfo where name  = ?";
		PreparedStatement NameEqual = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);// 初始化
			pstmt.setString(1, user1.getName());// 传递从前端拿到的值
			pstmt.setString(2, user1.getPassword());
			pstmt.setString(3, user1.getApassword());
			pstmt.setString(4, user1.getEmail());
			pstmt.setString(5, user1.getBrotherName());

//			这里考试搞用户名重复问题
			NameEqual = conn.prepareStatement(sql2);// 初始化
			NameEqual.setString(1, user1.getName());// 拿到本次用户注册的用户名
			ResultSet rsName = NameEqual.executeQuery();// 将前台数据存入resultset中
			if (rsName.next() == false) {// 名字无匹配项
				res = pstmt.executeUpdate();// 直接进行上传
			}
			if (rsName.next() != false) {// 名字有重复
				res = -1;// 返回-1，表示失败
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return res;
	}
//登录
	@Override
	public userbean login(userbean user1) {
		int uid = 0;
		userbean user = null;
		Connection conn = DBContil.getConn();
		String sql = "select uid,email,brotherName,isAdmin from userinfo where name =? and password = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user1.getName());
			pstmt.setString(2, user1.getPassword());
//			  pstmt.setString(3,user1.getBrotherName());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				uid = rs.getInt(1);
				String email = rs.getString(2);
				String brotherName = rs.getString(3);
				int  isAdmin = rs.getInt(4);
				user = new userbean(uid,user1.getName(),user1.getPassword(),null,email,brotherName,isAdmin);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public String forgetPassword(userbean user1) {
		String result = "";//初始化
		Connection conn = DBContil.getConn();//建立连接
		String sql = "select password from userinfo where name = ? and brotherName = ?";//查询条件
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);//去数据库查询
			pstmt.setString(1, user1.getName());
			pstmt.setString(2,user1.getBrotherName());
			ResultSet rs = pstmt.executeQuery();//数据存储
			if (rs.next()) {
				result = rs.getString(1);//拿到密码数据
				System.out.println(result);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return result;
//		list.clear();
	}

	@Override
	public List<productbean> getAllinfos() {
		Connection conn = DBContil.getConn();//初始化
		//sql语句，查找数据库中的数据
		String sql = "select itemId,price,begin,dest,jiphone,dephone,isDeal,time,productID,Date from product where isD = 1";
		PreparedStatement pstmt = null;
		List<productbean> list = new ArrayList<productbean>();
		try {
			pstmt = conn.prepareStatement(sql);//将sql语句进入Mysql进行查询
			ResultSet rs = pstmt.executeQuery();//将查询到所有的数据存储至ResultSet容器中
			while (rs.next()) {
				//遍历ResultSet容器，将数据库数据挨个拿出
				int pid = rs.getInt(1);
				int price = rs.getInt(2);
				String begin = rs.getString(3);
				String dest = rs.getString(4);
				String jiphone = rs.getString(5);
				String dephone = rs.getString(6);
				int isDeal = rs.getInt(7);
				int time = rs.getInt(8);
				String productID = rs.getString(9);
				String Date = rs.getString(10);
				//将数据拿出后进行productbean对象封装
				productbean product = new productbean(pid, productID, price, begin, dest, jiphone, dephone, isDeal,
						time, Date);
				//添加至list，作为返回值
				list.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
//获取特定的用户信息
//	public List<userbean> getSpecialUserInfo
//	获取用户表
	@Override
	public List<userbean> getAllUserinfos() {
		Connection conn = DBContil.getConn();//初始化
		//sql语句，查找数据库中的数据
		String sql = "select uid , name , email,brotherName from userinfo where isAdmin = 0";
		PreparedStatement pstmt = null;
		List<userbean> list = new ArrayList<userbean>();
		try {
			pstmt = conn.prepareStatement(sql);//将sql语句进入Mysql进行查询
			ResultSet rs = pstmt.executeQuery();//将查询到所有的数据存储至ResultSet容器中
			while (rs.next()) {
				//遍历ResultSet容器，将数据库数据挨个拿出
				int uid = rs.getInt("uid");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String brotherName = rs.getString("brotherName");

				//将数据拿出后进行productbean对象封装
				userbean user = new userbean(uid,name,null,null,email,brotherName);
				//添加至list，作为返回值
				list.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<medicinebean> getMedicineInfo() {
		Connection conn = DBContil.getConn();//初始化
		//sql语句，查找数据库中的数据
		String sql = "select medicineIntroId , medicineName , medicineIntroduce ,isOTC,dosage,img from medicineintroduce";
		PreparedStatement pstmt = null;
		List<medicinebean> list = new ArrayList<medicinebean>();
		try {
			pstmt = conn.prepareStatement(sql);//将sql语句进入Mysql进行查询
			ResultSet rs = pstmt.executeQuery();//将查询到所有的数据存储至ResultSet容器中
			while (rs.next()) {
				//遍历ResultSet容器，将数据库数据挨个拿出
				int medicineIntroId = rs.getInt("medicineIntroId");
				String medicineName = rs.getString("medicineName");
				String medicineIntroduce = rs.getString("medicineIntroduce");
				int isOTC = rs.getInt("isOTC");
				String dosage = rs.getString("dosage");
				String img = rs.getString("img");

				//将数据拿出后进行productbean对象封装
//				userbean user = new userbean(uid,name,null,null,email,brotherName);
				medicinebean medicineInfomation  = new medicinebean(medicineIntroId,medicineName,medicineIntroduce,isOTC,dosage,img);
				//添加至list，作为返回值
//				System.out.println("这里真的拿到了medicin信息了么？" + medicineInf);?
				list.add(medicineInfomation);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	
	@Override
	public List<productbean> getSearchInfo(productbean product) {
		Connection conn = DBContil.getConn();//初始化
		String sql = "select productID,begin,dest,Date,isDeal,itemId from product where isD=1 ";//对数据库数据进行查询
		StringBuffer sb = new StringBuffer(sql);//转化为stringbuffer类型，方便动态sql
		List<Object> params = new ArrayList<Object>();
		//以下的三条if目的是判断输入框中内容是否为空，如果不为空，进行动态sql条件的附加。
		if (product.getProductID() != null && !"".equals(product.getProductID())) {
			sb.append(" and productID like ? ");
			params.add("%" + product.getProductID() + "%");
		}
		if (product.getBegin() != null && !"".equals(product.getBegin())) {
			sb.append(" and begin like ? ");
			params.add("%" + product.getBegin() + "%");
		}
		if (product.getDest() != null && !"".equals(product.getDest())) {
			sb.append(" and dest like ? ");
			params.add("%" + product.getDest() + "%");
		}
		sql = sb.toString(); //将完整的String转化
		PreparedStatement pstmt = null;
		List<productbean> list = null;
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));//对list添加对象
			}
			ResultSet rs = pstmt.executeQuery();//拿到需要显示的内容
			list = new ArrayList<productbean>();
			while (rs.next()) {
				//将需要显示的内容从rs 中取出。
				String productID = rs.getString(1);
				String begin = rs.getString(2);
				String dest = rs.getString(3);
				String riqi = rs.getString(4);
				int isDeal = rs.getInt(5);
				int uid = rs.getInt(6);
				//对象封装
				productbean product1 = new productbean(uid, productID, begin, dest, isDeal, riqi);
				list.add(product1);//添加至显示list
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;//返回
	}


	
	@Override
	public productbean getProductById(int id) {
		Connection conn = DBContil.getConn();
		String sql = "select itemId,productID,price,jiphone,dephone,Date,begin,dest,time,isDeal from product where itemId = ? ";
		PreparedStatement pstmt = null;
		productbean product = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int pid = rs.getInt(1);
				String productID = rs.getString(2);
				int price = rs.getInt(3);
				String jiphone = rs.getString(4);
				String dephone = rs.getString(5);
				String Date = rs.getString(6);
				String begin = rs.getString(7);
				String dest = rs.getString(8);
				int time = rs.getInt(9);
				int isDeal = rs.getInt(10);
				product = new productbean(pid, productID, price, begin, dest, jiphone, dephone, isDeal, time, Date);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return product;

	}

	@Override
	public int updateproduct(productbean product) {
		Connection conn = DBContil.getConn();
		int res = 0;
		String sql = "update product set productID = ? , price=? , begin = ? , dest = ? , jiphone = ?,dephone = ? , isDeal = ? , time = ? , Date = ? where itemId = ?";
//		String sql2 = "select name from userinfo where name  = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);// 初始化

			pstmt.setString(1, product.getProductID());// 传递从前端拿到的值
			pstmt.setInt(2, product.getPrice());
			pstmt.setString(3, product.getBegin());
			pstmt.setString(4, product.getDest());
			pstmt.setString(5, product.getJiphone());
			pstmt.setString(6, product.getDephone());
			pstmt.setInt(7, product.getIsDeal());
			pstmt.setInt(8, product.getTime());
			pstmt.setString(9, product.getDate());
			pstmt.setInt(10, product.getUid());
			res = pstmt.executeUpdate();
			
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return res;
	}

	@Override
	public int delete(int uid,int isD) {
		Connection conn = DBContil.getConn();
		int res = 0;
		String sql = "update product set isD=? where itemId = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);// 初始化

			pstmt.setInt(1, isD);// 传递从前端拿到的值
			pstmt.setInt(2, uid);
			
			res = pstmt.executeUpdate();
			
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return res;
	}

	@Override
	public int addProduct(productbean product) {
		Connection conn = DBContil.getConn();
		int res = 0;
		String sql = "insert into product ( productID , price , begin , dest , jiphone , dephone , isDeal , time , Date )  values( ? , ? , ? , ? ,? , ? , ?,?,?  )";
		String sql2 = "select productID from product where productID  = ?";
		PreparedStatement NameEqual = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);// 初始化
			pstmt.setString(1,product.getProductID() );// 传递从前端拿到的值
			pstmt.setInt(2,product.getPrice() );
			pstmt.setString(3,product.getBegin() );
			pstmt.setString(4, product.getDest());
			pstmt.setString(5, product.getJiphone());
			pstmt.setString(6, product.getDephone());
			pstmt.setInt(7, product.getIsDeal());
			pstmt.setInt(8, product.getTime());
			pstmt.setString(9, product.getDate());

//			这里考试搞用户名重复问题
			NameEqual = conn.prepareStatement(sql2);// 初始化
			NameEqual.setString(1, product.getProductID());//
			ResultSet rsName = NameEqual.executeQuery();// 将前台数据存入resultset中
			if (rsName.next() == false) {// 名字无匹配项
				res = pstmt.executeUpdate();// 直接进行上传
			}
			if (rsName.next() != false) {// 名字有重复
				res = -1;// 返回-1，表示失败
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
}
