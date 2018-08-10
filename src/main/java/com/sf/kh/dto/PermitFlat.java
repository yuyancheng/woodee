package com.sf.kh.dto;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

/**
 * 权限扁平结构
 * 
 * @author 01367825
 */
public class PermitFlat implements java.io.Serializable {

    private static final long serialVersionUID = 5191371614061952661L;

    private String permitId; // 权限ID
    private String parentId; // 父权限节点ID
    private String permitName; // 权限名称
    private int    permitType; // 权限类型：1菜单；2按钮；
    private String permitUrl; // 权限URL
    private String permitIcon; // 权限图标
    private int orders; // 排序
    private int status; // 状态：0无效；1有效；

    private boolean available; // 是否可用（parent.available && this.status==ENABLE）
    private int level = 0; // 节点层级
    private List<String> nodePath; // 节点路径
    private boolean leaf; // 是否叶子节点

    public PermitFlat() {}

    public PermitFlat(PermitTree pt) {
        this.parentId = pt.getParentId();
        this.permitId = pt.getPermitId();
        this.permitName = pt.getPermitName();
        this.permitType = pt.getPermitType();
        this.permitUrl = pt.getPermitUrl();
        this.permitIcon = pt.getPermitIcon();
        this.status = pt.getStatus();
        this.available = pt.isAvailable();
        this.level = pt.getLevel();
        this.nodePath = pt.getNodePath();
        this.leaf = CollectionUtils.isEmpty(pt.getChildren());
    }

    // -----------------------------------------------------------getter/setter
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPermitId() {
        return permitId;
    }

    public void setPermitId(String permitId) {
        this.permitId = permitId;
    }

    public String getPermitName() {
        return permitName;
    }

    public void setPermitName(String permitName) {
        this.permitName = permitName;
    }

    public int getPermitType() {
        return permitType;
    }

    public void setPermitType(int permitType) {
        this.permitType = permitType;
    }

    public String getPermitUrl() {
        return permitUrl;
    }

    public void setPermitUrl(String permitUrl) {
        this.permitUrl = permitUrl;
    }

    public String getPermitIcon() {
        return permitIcon;
    }

    public void setPermitIcon(String permitIcon) {
        this.permitIcon = permitIcon;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<String> getNodePath() {
        return nodePath;
    }

    public void setNodePath(List<String> nodePath) {
        this.nodePath = nodePath;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

}
