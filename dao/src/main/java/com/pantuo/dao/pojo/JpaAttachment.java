package com.pantuo.dao.pojo;

import javax.persistence.*;

/**
 * @author tliu
 *
 * 附件
 */
@Entity
@Table(name="attachment")
public class JpaAttachment extends BaseEntity {

    public static enum Type {
        ht_pic,     //合同图片
        ht_fj,      //合同附件
        u_pic,      //用户图片
        u_fj,       //用户附件
        su_file,     //素材文件
        fp_file     //发票文件
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Type type;
    private String url;     //附件url
    private String name;	//附件名称
    private int mainId;
    private String userId;

    public JpaAttachment() {
        //for serialization
    }

    public JpaAttachment(Type type, String url, String name, int mainId, String userId) {
        this.type = type;
        this.url = url;
        this.name = name;
        this.mainId = mainId;
        this.userId = userId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMainId() {
        return mainId;
    }

    public void setMainId(int mainId) {
        this.mainId = mainId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "JpaAttachment{" +
                "type=" + type +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", mainId=" + mainId +
                ", userId=" + userId +
                '}';
    }
}