package com.atguigu.cpes.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.atguigu.cpes.bean.Cert;
import com.atguigu.cpes.bean.Member;
import com.atguigu.cpes.service.CertService;
import com.atguigu.cpes.service.MemberService;
import com.atguigu.cpes.util.Const;

/**
 * 会员控制器
 * @author 18801
 *
 */
@Controller
@RequestMapping("/member")
public class MemberController extends  BaseController{

	@Autowired
	private MemberService memberService;
	@Autowired
	private CertService certService;
	
	
	@RequestMapping("/cert")
	public Object cert( )  {
		return "member/cert";
	}


	@RequestMapping("/apply-1")
	public String apply1( String acctype, HttpSession session ) {
		
		Member member = (Member)session.getAttribute(Const.SESSION_USER);
		member.setAcctype(acctype);
		memberService.updateMember(member);
		
		List<Cert> lcert = memberService.queryMember4acctype(acctype);
		session.setAttribute("cert", lcert);
		return "member/apply-1";
	}
    @ResponseBody
	@RequestMapping("/doapply1")
	public Object apply2( String realname,String cardno, HttpSession session ) {
		start();
		
		try {
			Member member = (Member)session.getAttribute(Const.SESSION_USER);
			member.setRealname(realname);
			member.setCardno(cardno);
			memberService.updateMember4s(member);
			success(true);
		} catch ( Exception e ) {
			e.printStackTrace();
			success(false);
		}
		
		return end();
	}
	
	@RequestMapping("/apply-2")
	public String apply2( HttpSession session, Model model ) {
		Member member = (Member)session.getAttribute(Const.SESSION_USER);
		List<Cert> certs = certService.queryCertByAcctype(member.getAcctype());
		model.addAttribute("certs", certs);
		return "member/apply-2";
	}

	
	
	@ResponseBody
	@RequestMapping("/doapply2")
	public Object doapply2( HttpServletRequest req, HttpSession session ) {
		start();
		
		try {
			
			String realPath = session.getServletContext().getRealPath("imgs");
			
			Member member = (Member)session.getAttribute(Const.SESSION_USER);
			
			MultipartHttpServletRequest request =
				(MultipartHttpServletRequest)req;
			Iterator<String> iters = request.getFileNames();
			while ( iters.hasNext() ) {
				String filename = iters.next();
				String certid = filename.split("-")[1];
				
				MultipartFile file = request.getFile(filename);
				String name = file.getOriginalFilename();
				name = name.substring(name.lastIndexOf("."));
				String path = UUID.randomUUID().toString();
				name = path + name;
				InputStream in = file.getInputStream();
				FileOutputStream out = new FileOutputStream(new File(realPath + "/" + name));
				
				int i = -1;
				while ( (i = in.read()) != -1 ) {
					out.write(i);
				}
				
				out.close();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("memberid", member.getId());
				paramMap.put("certid", Integer.parseInt(certid));
				paramMap.put("path", name);
				memberService.insertMemberCert(paramMap);
			}
			
			success(true);
		} catch ( Exception e ) {
			e.printStackTrace();
			success(false);
		}
		
		return end();
	}
	
	
	
	
	
	
	
	
	@ResponseBody
	@RequestMapping("/dologin")
	public Object dologin( Member member, HttpSession session) throws Exception {
		// Model数据存储范围为request
		start();
		Member dbMember = memberService.queryMember4Login(member);
		
		if ( dbMember == null ) {
			success(false);
		} else {
			session.setAttribute(Const.SESSION_USER, dbMember);
			success(true);
		}
		return end();
	}
	
}
