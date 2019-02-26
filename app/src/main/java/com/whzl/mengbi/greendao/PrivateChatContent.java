package com.whzl.mengbi.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author nobody
 * @date 2019/2/13
 */
@Entity
public class PrivateChatContent {
    @Id(autoincrement = true)
    Long id;

    String content;
    Long timestamp;
    Long fromId;

    Long privateUserId;

    @Generated(hash = 1035300402)
    public PrivateChatContent(Long id, String content, Long timestamp, Long fromId,
            Long privateUserId) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.fromId = fromId;
        this.privateUserId = privateUserId;
    }

    @Generated(hash = 1548760302)
    public PrivateChatContent() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getFromId() {
        return this.fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getPrivateUserId() {
        return this.privateUserId;
    }

    public void setPrivateUserId(Long privateUserId) {
        this.privateUserId = privateUserId;
    }

}