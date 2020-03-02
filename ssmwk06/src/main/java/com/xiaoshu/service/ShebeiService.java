package com.xiaoshu.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.ShebeiMapper;
import com.xiaoshu.entity.Shebei;
import com.xiaoshu.entity.ShebeiExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShebeiService {

	@Autowired
	ShebeiMapper shebeiMapper;
	@Autowired
	private PinpaiService pinpaiService;

	// 查询所有
	public List<Shebei> findUser(Shebei t) throws Exception {
		return shebeiMapper.select(t);
	};

	// 数量
	public int countUser(Shebei t) throws Exception {

		return shebeiMapper.selectCount(t);
	};

	// 通过ID查询
	public Shebei findOneUser(Integer id) throws Exception {
		return shebeiMapper.selectByPrimaryKey(id);
	};

	// 新增
	public void addShebei(Shebei t) throws Exception {
		shebeiMapper.insert(t);
	};

	// 修改
	public void updateUser(Shebei t) throws Exception {
		shebeiMapper.updateByPrimaryKeySelective(t);
	};

	// 删除
	public void deleteUser(Integer id) throws Exception {
		shebeiMapper.deleteByPrimaryKey(id);
	};


	// 通过用户名判断是否存在，（新增时不能重名）
	public Shebei existUserWithUserName(String shebeiname) throws Exception {
		ShebeiExample example = new ShebeiExample();
		ShebeiExample.Criteria criteria = example.createCriteria();
		criteria.andShebeinameEqualTo(shebeiname);
		List<Shebei> shebeiList = shebeiMapper.selectByExample(example);
		return shebeiList.isEmpty()?null:shebeiList.get(0);
	};


	public PageInfo<Shebei> findUserPage(int pageNum, int pageSize, String ordername, String order) {
		ordername = StringUtil.isNotEmpty(ordername)?ordername:"userid";
		order = StringUtil.isNotEmpty(order)?order:"desc";
		ShebeiExample example = new ShebeiExample();
		example.setOrderByClause(ordername+" "+order);
		ShebeiExample.Criteria criteria = example.createCriteria();
		PageHelper.startPage(pageNum, pageSize);
		List<Shebei> shebeiList = shebeiMapper.selectByExample(example);
		for (Shebei s:shebeiList){
			s.setPinpai(pinpaiService.findOneUser(s.getPid()));
		}
		PageInfo<Shebei> pageInfo = new PageInfo<Shebei>(shebeiList);
		return pageInfo;
	}


}
