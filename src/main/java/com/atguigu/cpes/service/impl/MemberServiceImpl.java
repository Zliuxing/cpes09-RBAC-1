package com.atguigu.cpes.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.cpes.bean.Cert;
import com.atguigu.cpes.bean.Member;
import com.atguigu.cpes.dao.MemberDao;
import com.atguigu.cpes.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;

	public Member queryMember4Login(Member member) {
		return memberDao.queryMember4Login(member);
	}

	public void updateMember(Member member) {
		memberDao.updateMember(member);
	}

	public List<Cert> queryMember4acctype(String acctype) {
		return memberDao.queryMember4acctype(acctype);
	}

	public void updateMember4s(Member member) {
		memberDao.updateMember4s(member);
	}

	public void insertMemberCert(Map<String, Object> paramMap) {
		memberDao.insertMemberCert(paramMap);
	}
}
