package com.buaa.lottery.trie;

/**
 * project bean
 * @author jiehua
 *  
 */
public class ProjectBean {


	private String projectID;

	private String name;

	private String category_id;

	private long fee;

	private long has_fee;

	private double interest;

	private int deadline;

	private long repayment_date;

	private long create_time;

	private int status;
	
	private String rate_range;
	
	private String repayment_type;
	
	/**
	 * 
	 * @param projectID 项目id
	 * @param name 项目名称
	 * @param category_id 区分项目的类型       8=银存宝   3=直投   5=长标
	 * @param fee 项目金额
	 * @param has_fee 已投资金额
	 * @param interest 年化收益
	 * @param deadline 项目期限
	 * @param repayment_date 还款日期
	 * @param create_time 创建日期
	 * @param status 状态值  3=募集中 4=已满标 5=还款中 6=已结束
	 */
	
	/**
	 * repayment_type  还款方式:1每月还息到期还本, 
	 * rate_range  利率区间(长标，根据用户购买的期限不同而利率不同)
	 * 
	 */

	public ProjectBean(){
		
	}
	public ProjectBean(String projectID,String name,String category_id,long fee,long has_fee,double interest,int deadline,
			long repayment_date,long create_time,int status,String repayment_type,String rate_range){
		
		this.projectID = projectID;
		this.name = name;
		this.category_id = category_id;
		this.fee = fee;
		this.has_fee = has_fee;
		this.interest = interest;
		this.deadline = deadline;
		this.repayment_date = repayment_date;
		this.create_time = create_time;
		this.status = status;
		this.repayment_type = repayment_type;
		this.rate_range = rate_range;
	}
	
	public void setRepayment_type(String repayment_type){
		this.repayment_type = repayment_type;
	}
	
	public String getRepayment_type(){
		return repayment_type;
	}
	
	public void setRate_range(String rate_range){
		this.rate_range = rate_range;
	}
	
	public String getRate_range(){
		return rate_range;
	}
	
	public void setProjectID(String projectID){
		this.projectID = projectID;
	}
	
	public String getProjectID(){
		return projectID;
	}
	
	public void setCategory_id(String category_id){
		this.category_id = category_id;
	}
	
	public String getCategory_id(){
		return category_id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setFee(long fee){
		this.fee = fee;
	}
	
	public Long getFee(){
		return fee;
	}
	
	public void setHas_fee(long has_fee){
		this.has_fee = has_fee;
	}
	
	public Long getHas_fee(){
		return has_fee;
	}
	
	public void setRepayment_date(long repayment_date){
		this.repayment_date = repayment_date;
	}
	
	public Long getRepayment_date(){
		return repayment_date;
	}
	
	public void setCreate_time(long create_time){
		this.create_time = create_time;
	}
	
	public Long getCreate_time(){
		return create_time;
	}
	
	public void setStatus(int status){
		this.status =  status;
	}
	
	public Integer getStatus(){
		return status;
	}
	
	public void setDeadline(int deadline){
		this.deadline =  deadline;
	}
	
	public Integer getDeadline(){
		return deadline;
	}
	
	public void setInterest(double interest){
		this.interest = interest;
	}
	
	public Double getInterest(){
		return interest;
	}
	
}
