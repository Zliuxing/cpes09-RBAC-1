package com.atguigu.cpes.dao;

import java.util.List;
import java.util.Map;

import com.atguigu.cpes.bean.Cert;
import com.atguigu.cpes.bean.Member;

/**
 * 会员数据访问对象
 * @author 18801
 *
 */
public interface MemberDao {


	Member queryMember4Login(Member member);

	void updateMember(Member member);

	List<Cert> queryMember4acctype(String acctype);

	void updateMember4s(Member member);

	void insertMemberCert(Map<String, Object> paramMap);

}
