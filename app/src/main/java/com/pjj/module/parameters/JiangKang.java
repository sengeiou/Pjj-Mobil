package com.pjj.module.parameters;

/**
 * Created by XinHeng on 2019/03/26.
 * describe：
 */
public class JiangKang {
    /**
     * personName : 姓名
     * sex : 性别：0女，1男
     * age : 年龄
     * phone : 联系电话
     * areaId : 区域id
     * address : 详细地址
     * physicianName : 预约医院医师
     * content : 预约内容
     * appointmentTime : 预约诊疗时间:yyyy-MM-dd
     * recommendPhone : 推荐人电话
     * remarks : 备注
     * userId : 用户id
     */

    private String personName;
    private String sex;
    private String age;
    private String phone;
    private String areaId;
    private String address;
    private String physicianName;
    private String content;
    private String appointmentTime;
    private String recommendPhone;
    private String remarks;
    private String userId;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhysicianName() {
        return physicianName;
    }

    public void setPhysicianName(String physicianName) {
        this.physicianName = physicianName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getRecommendPhone() {
        return recommendPhone;
    }

    public void setRecommendPhone(String recommendPhone) {
        this.recommendPhone = recommendPhone;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
