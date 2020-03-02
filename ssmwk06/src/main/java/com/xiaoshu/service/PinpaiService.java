package com.xiaoshu.service;

import com.xiaoshu.dao.PinpaiMapper;
import com.xiaoshu.entity.Pinpai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PinpaiService {

	@Autowired
	PinpaiMapper pinpaiMapper;

	// 查询所有
	public List<Pinpai> findUser(Pinpai t) throws Exception {
		return pinpaiMapper.select(t);
	};

	// 数量
	public int countPinpai(Pinpai t) {
		return pinpaiMapper.selectCount(t);
	};

	// 通过ID查询
	public Pinpai findOneUser(Integer id) {
		return pinpaiMapper.selectByPrimaryKey(id);
	};

	// 新增
	public void addUser(Pinpai t) {
		pinpaiMapper.insert(t);
	};

	//通过pinpai对象获取id
	public Integer getId(Pinpai pinpai){
		Pinpai one = pinpaiMapper.selectOne(pinpai);
		return one.getId();
	}

}
