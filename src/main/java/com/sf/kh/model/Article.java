package com.sf.kh.model;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.List;

/**
 * @Auther: 01378178
 * @Date: 2018/6/27 16:02
 * @Description:
 */
public class Article extends BaseModel{

    private Long id;

    private String title;

    private Long authorId;

    private String authorName;

    private Long typeId;

    private String typeName;

    private String content;

    private Date publishTm;

    private Integer top;

    private Integer speed;

    private Integer publishStatus;

    /**
     * 提模糊查询使用
     */
    private String titleFuzzy;


    // id 集合
    private List<Long> ids;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getTypeId() {
        return typeId;
    }

    public String getContent() {
        return content;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishTm() {
        return publishTm;
    }

    public void setPublishTm(Date publishTm) {
        this.publishTm = publishTm;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTitleFuzzy() {
        return titleFuzzy;
    }

    public void setTitleFuzzy(String titleFuzzy) {
        this.titleFuzzy = titleFuzzy;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public enum PublishStatusEnum{
        NOT_PUBLISH(1), PUBLISHED(2);

        PublishStatusEnum(Integer code){
            this.code = code;
        }
        private Integer code;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static void main(String[] args) {

        String content = "<p>sdfs </p><p><img width=\"105\" src=\"data:image/png;base64,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\"></p><p>dsfa f</p><p><img width=\"105\" src=\"data:image/png;base64,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\"></p><p>哈哈哈阿萨的合法性爱的&nbsp;爱的发的哈阿萨德撒发的<br></p><p< p=\"\"></p<>";

        Date date = new Date();

        Article a =new Article();
        a.setTitle("标题");
        a.setAuthorId(100L);
        a.setAuthorName("作家");
        a.setTypeId(1L);
        a.setContent(content);
        a.setPublishTm(date);
        a.setTop(1);
        a.setSpeed(1);
        a.setCreateBy(0L);
        a.setCreateTm(date);
        a.setUpdateBy(0L);
        a.setUpdateTm(date);
        a.setVersion(1);

        String s = JSONObject.toJSONString(a);
        System.out.println(s);
    }

}
