package com.sf.kh.model;

/**
 * @Auther: 01378178
 * @Date: 2018/6/27 15:41
 * @Description:
 */
public class ArticleType extends BaseModel{

    private Long id;

    private String typeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
