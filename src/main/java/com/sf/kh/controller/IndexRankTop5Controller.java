package com.sf.kh.controller;

import javax.annotation.Resource;
import java.util.Map;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import code.ponfee.commons.model.Result;

import com.sf.kh.model.IndexRankTop5;
import com.sf.kh.service.IIndexRankTop5Service;
/***
 * 获取首页top5的记录
 * @author 866316
 *
 */
@RestController
@RequestMapping(path = "indexRank")
public class IndexRankTop5Controller {
	
	private @Resource IIndexRankTop5Service indexRankTop5Service;
	/**
	 * 获取首页top5记录集合
	 * @return
	 */
	@PostMapping(path = "top5")
	public Result<Map<String,List<IndexRankTop5>>> getTop5List(){
		return indexRankTop5Service.getTop5List();
	}
}
