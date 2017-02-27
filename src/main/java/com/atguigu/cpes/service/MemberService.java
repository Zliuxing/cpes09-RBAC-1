package com.atguigu.cpes.service;

import java.util.List;
import java.util.Map;

import com.atguigu.cpes.bean.Cert;
import com.atguigu.cpes.bean.Member;

/**
 * 会员服务对象
 * @author 18801
 *
 */
public interface MemberService {

	Member queryMember4Login(Member member);

	void updateMember(Member member);

	List<Cert> queryMember4acctype(String acctype);

	void updateMember4s(Member member);

	void insertMemberCert(Map<String, Object> paramMap);

}
