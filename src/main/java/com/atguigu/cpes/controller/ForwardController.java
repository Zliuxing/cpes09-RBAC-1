package com.atguigu.cpes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.cpes.bean.Member;
import com.atguigu.cpes.bean.Menu;
import com.atguigu.cpes.bean.Permission;
import com.atguigu.cpes.bean.User;
import com.atguigu.cpes.service.MenuService;
import com.atguigu.cpes.service.UserService;
import com.atguigu.cpes.util.Const;

/**
 * 页面跳转控制器
 * @author 18801
 *
 */
@Controller
@RequestMapping("/")
public class ForwardController {

	@Autowired
	private MenuService menuService;
	@Autowired
	private UserService userService;
	
	/**
	 * 跳转到登陆页面
	 * @return
	 */
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping("/member")
	public String member() {
		return "member";
	}
	
	/**
	 * 跳转到用户主页面
	 * @return
	 */
	@RequestMapping("/main")
	public String main( Model model, HttpSession session ) {			
		// 查询登陆用户所拥有的权限菜单数据
		User loginUser = (User)session.getAttribute(Const.SESSION_USER);
		List<Permission> permissons = userService.queryPermissionsByUser(loginUser);
		
		// 组合菜单数据
		Permission root = null;
		Map<Integer, Permission> permissionMap = new HashMap<Integer, Permission>();
		for ( Permission permisson : permissons ) {
			permissionMap.put(permisson.getId(), permisson);
		}
		for (Permission permisson : permissons) {
			// 子菜单
			Permission child = permisson;
			
			if ( permisson.getPid() == 0 ) {
				root = permisson;
			} else {
				// 父菜单
				Permission parent = permissionMap.get(child.getPid());
				// 组合父菜单和子菜单的关系
				parent.getChildren().add(child);
			}
		}
		session.setAttribute("smenus", root.getChildren());
		
		return "main";
	}

	public void queryMenus( Menu pmenu ) {
		// 子菜单
		List<Menu> childMenus = menuService.queryChildMenu(pmenu);
		
		// 继续查询子菜单
		for ( Menu childMenu : childMenus ) {
			queryMenus(childMenu);
		}
		
		// 整合父子菜单的关系
		pmenu.setChildrenMenus(childMenus);
	}
}
 