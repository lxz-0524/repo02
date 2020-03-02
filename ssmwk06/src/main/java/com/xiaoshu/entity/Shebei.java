package com.xiaoshu.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

public class Shebei implements Serializable {
    @Id
    private Integer id;

    private String shebeiname;

    private Integer pid;

    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    @Transient
    private Pinpai pinpai ;

    public Pinpai getPinpai() {
        return pinpai;
    }

    public void setPinpai(Pinpai pinpai) {
        this.pinpai = pinpai;
    }

    private static final long serialVersionUID = 1L;

    public Shebei() {
    }

    public Shebei(String shebeiname, Integer pid, String phone, Date createtime) {
        this.shebeiname = shebeiname;
        this.pid = pid;
        this.phone = phone;
        this.createtime = createtime;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return shebeiname
     */
    public String getShebeiname() {
        return shebeiname;
    }

    /**
     * @param shebeiname
     */
    public void setShebeiname(String shebeiname) {
        this.shebeiname = shebeiname == null ? null : shebeiname.trim();
    }

    /**
     * @return pid
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * @param pid
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * @return createtime
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", shebeiname=").append(shebeiname);
        sb.append(", pid=").append(pid);
        sb.append(", phone=").append(phone);
        sb.append(", createtime=").append(createtime);
        sb.append("]");
        return sb.toString();
    }
}