package com.xiaoshu.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.*;
import com.xiaoshu.service.*;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("shebei")
public class ShebeiController extends LogController{
	static Logger logger = Logger.getLogger(ShebeiController.class);

	@Autowired
	private ShebeiService shebeiService;
	@Autowired
	private PinpaiService pinpaiService;
	
	@Autowired
	private RoleService roleService ;
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("shebeiIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Role> roleList = roleService.findRole(new Role());
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		List<Pinpai> pinpaiList = pinpaiService.findUser(null);
		request.getSession().setAttribute("menuid",menuid);
		request.setAttribute("pinpaiList",pinpaiList);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "shebei";
	}
	
	
	@RequestMapping(value="shebeiList")
	public void userList(HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			String order = request.getParameter("order");
			String ordername = request.getParameter("ordername");
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<Shebei> shebeiList= shebeiService.findUserPage(pageNum,pageSize,ordername,order);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",shebeiList.getTotal() );
			jsonObj.put("rows", shebeiList.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户展示错误",e);
			throw e;
		}
	}
	
	// 新增或修改
	@RequestMapping("reserveShebei")
	public void reserveUser(HttpServletRequest request,Shebei shebei,HttpServletResponse response){
		Integer pid = shebei.getId();
		JSONObject result=new JSONObject();
		try {
			if (pid != null) {   // userId不为空 说明是修改
				Shebei sname = shebeiService.existUserWithUserName(shebei.getShebeiname());
				if(sname == null ){
					shebeiService.updateUser(shebei);
					result.put("success", true);
				}else{
					result.put("success", true);
					result.put("errorMsg", "该设备名被使用");
				}
				
			}else {   // 添加
				if(shebeiService.existUserWithUserName(shebei.getShebeiname())==null){  // 没有重复可以添加
					shebei.setCreatetime(new Date());
					shebeiService.addShebei(shebei);
					result.put("success", true);
				} else {
					result.put("success", true);
					result.put("errorMsg", "该设备名被使用");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存设备信息错误",e);
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}

	@RequestMapping("in")
	public String inShebei(MultipartFile file,HttpServletResponse response,HttpServletRequest request){
		try{
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file.getInputStream());
			HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
			for (int i=0;i<sheetAt.getLastRowNum();i++){
				HSSFRow row = sheetAt.getRow(i + 1);
				String sname = row.getCell(1).getStringCellValue();
				String pname = row.getCell(2).getStringCellValue();
				Pinpai pinpai = new Pinpai();
				pinpai.setName(pname);
				if (pinpaiService.getId(pinpai)==null){
					pinpaiService.addUser(pinpai);
				}
				Integer pid = pinpaiService.getId(pinpai);
				String phone = row.getCell(3).getStringCellValue();
				String ctime = row.getCell(4).getStringCellValue();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date createtime = sdf.parse(ctime);
				Shebei shebei = new Shebei(sname, pid, phone, createtime);
				shebeiService.addShebei(shebei);
			}
			hssfWorkbook.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		//此值在index中存储，方便返回，确保有返回值，不会异常
		int att = (int) request.getSession().getAttribute("menuid");
		return  "redirect:shebeiIndex.htm?menuid="+att;

	}
	
}
