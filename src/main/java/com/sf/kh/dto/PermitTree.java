package com.sf.kh.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.sf.kh.model.Permit;

import code.ponfee.commons.collect.Collects;
import code.ponfee.commons.util.Strings;

/**
 * 权限树形结构
 * 
 * @author 01367825
 */
public class PermitTree implements java.io.Serializable {

    private static final long serialVersionUID = -9081626363752680404L;

    public static final String DEFAULT_ROOT_NAME = "__Root__";

    private final String permitId;
    private final String parentId;
    private final String permitName;
    private final int    permitType;
    private final String permitUrl;
    private final String permitIcon;
    private final int orders;
    private final int status;

    private int level = 0; // 节点层级
    private boolean available; // 是否可用（parent.available && status==ENABLE）
    private List<String> nodePath; // 节点路径
    private List<PermitTree> children; // 子节点

    /**
     * 构造空结点作为根节点
     */
    public PermitTree(String permitId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(permitId), 
                                    "节点编号不能为空");

        this.permitId = permitId;
        this.parentId = null;
        this.permitName = null;
        this.permitType = Permit.TYPE_MENU; // 菜单
        this.permitUrl = null;
        this.permitIcon = null;
        this.orders = 0; // 节点次序为0
        this.status = Permit.STATUS_ENABLE; // 状态为可用
        this.available = true;
    }

    /**
     * 指定一个节点作为根节点
     * 
     * @param p the Permit object which as a tree root node
     */
    public PermitTree(Permit p) {
        Preconditions.checkArgument(StringUtils.isNotBlank(p.getPermitId()), 
                                    "节点编号不能为空");

        this.permitId = p.getPermitId();
        this.parentId = p.getParentId();
        this.permitName = p.getPermitName();
        this.permitType = p.getPermitType();
        this.permitUrl = p.getPermitUrl();
        this.permitIcon = p.getPermitIcon();
        this.orders = p.getOrders();
        this.status = p.getStatus();
        this.available = (p.getStatus() == Permit.STATUS_ENABLE);
    }

    public void build(List<Permit> permits) {
        build(permits, true, false);
    }

    /**
     * 以此节点为根，传入子节点来构建节点树
     * 
     * @param permits         节点列表
     * @param admitNullParent 是否相认父节点编号为空的节点
     * @param ignoreOrphan    忽略孤儿节点
     */
    public void build(List<Permit> permits, boolean admitNullParent, boolean ignoreOrphan) {
        if (CollectionUtils.isEmpty(permits)) {
            return;
        }

        Set<String> nodeIds = Sets.newHashSet(this.getPermitId());

        // 1、检查是否存在重复节点
        for (Permit p : permits) {
            if (!nodeIds.add(p.getPermitId())) {
                throw new RuntimeException("重复的节点：" + p.getPermitId());
            }
        }

        // 2、以此节点为根构建节点树
        this.build0(permits, admitNullParent, this.getPermitId()); // 若有节点的父节点编号为空，则以此节点编号作为父节点

        // 3、检查是否存在孤儿节点
        if (!ignoreOrphan) {
            Set<String> checkIds = Sets.newHashSet();
            for (PermitFlat pf : this.flat()) {
                checkIds.add(pf.getPermitId());
            }
            Set<String> diff = Collects.different(nodeIds, checkIds);
            if (diff != null && !diff.isEmpty()) {
                throw new RuntimeException("无效的孤儿节点：" + diff);
            }
        }
    }

    /**
     * 节点树形转为扁平化
     * 
     * @return
     */
    public List<PermitFlat> flat() {
        List<PermitFlat> collect = new ArrayList<>();
        flat0(collect);
        return collect;
    }

    // -----------------------------------------------------------private methods
    private void build0(List<Permit> permits, boolean admitNullParent, String pidIfNull) {
        // current "this" is parent: PermitNode parent = this;
        for (Permit permit : permits) {
            if (!admitNullParent && StringUtils.isBlank(permit.getParentId())) {
                continue; // 如果父节点编号为空 且 不接纳父节点编号为空的节点 则跳过
            }
            String parentId = Strings.ifBlank(permit.getParentId(), pidIfNull);
            if (this.permitId.equals(parentId)) {
                // found a child
                if (this.nodePath != null && this.nodePath.contains(this.permitId)) {
                    throw new RuntimeException("节点循环依赖：" + this.permitId);
                }
                if (this.children == null) {
                    this.children = new ArrayList<>();
                }
                PermitTree child = new PermitTree(permit);
                child.setAvailable(this.available && child.getStatus() == Permit.STATUS_ENABLE);
                child.setNodePath(Collects.add(this.nodePath, this.permitId));
                child.setLevel(this.level + 1);
                this.children.add(child);
            }
        }

        if (this.children != null) {
            // sort the children list
            this.children.sort(Comparator.comparing(PermitTree::getOrders));

            // recursion to build child tree
            for (PermitTree pt : this.children) {
                pt.build0(permits, admitNullParent, pidIfNull);
            }
        }

        this.setNodePath(Collects.add(this.nodePath, this.permitId)); // 节点路径追加自身的ID
    }

    private void flat0(List<PermitFlat> collect) {
        collect.add(new PermitFlat(this));
        if (this.children != null) {
            for (PermitTree pt : this.children) {
                pt.flat0(collect);
            }
        }
    }

    // -----------------------------------------------------------getter/setter
    public String getPermitId() {
        return permitId;
    }

    public String getParentId() {
        return parentId;
    }

    public String getPermitName() {
        return permitName;
    }

    public int getPermitType() {
        return permitType;
    }

    public String getPermitUrl() {
        return permitUrl;
    }

    public String getPermitIcon() {
        return permitIcon;
    }

    public int getOrders() {
        return orders;
    }

    public int getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
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

    public List<PermitTree> getChildren() {
        return children;
    }

    public void setChildren(List<PermitTree> children) {
        this.children = children;
    }

}
