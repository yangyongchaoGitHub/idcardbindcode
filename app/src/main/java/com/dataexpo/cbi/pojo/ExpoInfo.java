package com.dataexpo.cbi.pojo;

import java.io.Serializable;
import java.util.Date;

public class ExpoInfo implements Serializable {
    private Integer id;
    /**
     * 展会名称
     */
    private String exhiName;
    /**
     * 主办\在主场账号ID
     */
    private Integer loginId;
    /**
     * 项目类别；1：专业展；2：购票消费展；3：大型政府展；4：大型会议；5：体育赛事
     */
    private Integer exhiType = 0;
    /**
     * 目项类别；1：主办项目；2：主场项目
     */
    private Integer projectType;

    /**
     * 主办方
     */
    private String sponsor;

    /**
     * 举办省份
     */
    private String province;
    /**
     * ä¸¾åŠžåŸŽå¸‚
     */
    private String city;
    /**
     * è¯¦ç»†åœ°å€
     */
    private String address;

    /**
     * 是否置顶 0不置顶 1置顶
     */
    private Integer roof = 0;
    /**
     * å¼€å§‹æ—¶é—´
     */
    private Date startTime;
    /**
     * ç»“æŸæ—¶é—´
     */
    private Date endTime;
    /**
     * logå›¾è·¯å¾„
     */
    private String logPath;
    /**
     * å±•ä¼šè¯¦æƒ…
     */
    private String exhiRemark;
    /**
     * 状态；状态 ：1：正常，2：停用，3：删除
     */
    private Integer status = 1;
    /**
     * çŸ­ä¿¡ç­¾å
     */
    private String autograph;
    /**
     * åˆ›å»ºæ—¶é—´
     */
    private Date createtime;
    /**
     * åˆ›å»ºè€…
     */
    private String createuser;

    /**
     * 展示图
     */
    private String showLogo;

    /**
     * 按钮文案
     */
    private String buttonCopywriting;
    /**
     * 主办方
     */
    private String company;

    private String longitude;

    private String latitude;

    private String auditName;

    private Date auditStartTime;

    private Date auditEndTime;


    private Integer auditStatus;

    private String titleName;

    private String expoIntroduce;

    private String adsUrl;

    private String bottomInstructions;

    private String bottomHref;

    private String indoorMap;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExhiName() {
        return exhiName;
    }

    public void setExhiName(String exhiName) {
        this.exhiName = exhiName;
    }

    public Integer getLoginId() {
        return loginId;
    }

    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }

    public Integer getExhiType() {
        return exhiType;
    }

    public void setExhiType(Integer exhiType) {
        this.exhiType = exhiType;
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getRoof() {
        return roof;
    }

    public void setRoof(Integer roof) {
        this.roof = roof;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getExhiRemark() {
        return exhiRemark;
    }

    public void setExhiRemark(String exhiRemark) {
        this.exhiRemark = exhiRemark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public String getShowLogo() {
        return showLogo;
    }

    public void setShowLogo(String showLogo) {
        this.showLogo = showLogo;
    }

    public String getButtonCopywriting() {
        return buttonCopywriting;
    }

    public void setButtonCopywriting(String buttonCopywriting) {
        this.buttonCopywriting = buttonCopywriting;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public Date getAuditStartTime() {
        return auditStartTime;
    }

    public void setAuditStartTime(Date auditStartTime) {
        this.auditStartTime = auditStartTime;
    }

    public Date getAuditEndTime() {
        return auditEndTime;
    }

    public void setAuditEndTime(Date auditEndTime) {
        this.auditEndTime = auditEndTime;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getExpoIntroduce() {
        return expoIntroduce;
    }

    public void setExpoIntroduce(String expoIntroduce) {
        this.expoIntroduce = expoIntroduce;
    }

    public String getAdsUrl() {
        return adsUrl;
    }

    public void setAdsUrl(String adsUrl) {
        this.adsUrl = adsUrl;
    }

    public String getBottomInstructions() {
        return bottomInstructions;
    }

    public void setBottomInstructions(String bottomInstructions) {
        this.bottomInstructions = bottomInstructions;
    }

    public String getBottomHref() {
        return bottomHref;
    }

    public void setBottomHref(String bottomHref) {
        this.bottomHref = bottomHref;
    }

    public String getIndoorMap() {
        return indoorMap;
    }

    public void setIndoorMap(String indoorMap) {
        this.indoorMap = indoorMap;
    }
}
