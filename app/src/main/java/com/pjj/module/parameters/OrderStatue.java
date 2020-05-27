package com.pjj.module.parameters;

/**
 * Created by XinHeng on 2018/12/07.
 * describe：
 */
public class OrderStatue {
    //{userId:用户id,status:状态1,状态2}
    private String userId;
    /**
     * status:0 待审核/未处理  1 审核未通过  2 审核通过  3 已完成  4 审核过期  5 紧急撤销  6 未支付  7 支付过期
     */
    private String status;
    int pageNo;
    int pageNum;

    public OrderStatue() {
    }

    public OrderStatue(String userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
