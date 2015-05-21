package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tliu
 *
 * 城市
 */
@Entity
@Table(name="city")
public class JpaCity extends BaseEntity {
    //媒体类型
    public enum MediaType {
        screen ("屏幕广告"),
        body ("车身广告");

        private final String name;
        private MediaType(String name) {
            this.name = name;
        }

        public String getTypeName() {
            return name;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private MediaType mediaType;

    public JpaCity() {
        //for serialization
    }

    public JpaCity(String name, MediaType mediaType) {
        this.name = name;
        this.mediaType = mediaType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType media) {
        this.mediaType = media;
    }

    public String getMediaTypeName() {
        return mediaType == null ? "" : mediaType.getTypeName();
    }

    @Override
    public String toString() {
        return "JpaCity{" +
                "mediaType='" + mediaType + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}