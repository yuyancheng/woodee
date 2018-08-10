package com.sf.kh.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sf.kh.dto.PermitFlat;
import com.sf.kh.dto.PermitTree;
import com.sf.kh.model.Permit;
import com.sf.kh.service.IPermitService;
import com.sf.kh.util.WebContextHolder;

import code.ponfee.commons.model.Result;

/**
 * Role Controller
 * 
 * @author 01367825
 */
@RestController
@RequestMapping("permit")
public class PermitController {

    private @Resource IPermitService service;

    @PostMapping("add")
    public Result<Void> add(@RequestBody Permit permit) {
        long currentUserId = WebContextHolder.currentUser().getId();
        permit.setCreateBy(currentUserId);
        permit.setUpdateBy(currentUserId);
        return service.add(permit);
    }

    @PostMapping("update")
    public Result<Void> update(@RequestBody Permit permit) {
        permit.setUpdateBy(WebContextHolder.currentUser().getId());
        return service.update(permit);
    }

    @PostMapping("delete")
    public Result<Void> delete(@RequestBody String[] permitIds) {
        return service.delete(permitIds);
    }

    @GetMapping("get")
    public Result<Permit> get(@RequestParam("permitId") String permitId) {
        return service.get(permitId);
    }

    @GetMapping("list/tree")
    public Result<PermitTree> listAsTree() {
        return service.listAsTree();
    }

    @GetMapping("list/flat")
    public Result<List<PermitFlat>> listAsFlat() {
        return service.listAsFlat();
    }
}
