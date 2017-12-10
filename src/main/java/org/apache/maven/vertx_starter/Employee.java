package org.apache.maven.vertx_starter;
import io.vertx.core.json.JsonObject;

public class Employee {
	private long employeeId;
	private String employeeName;
	private String dept;
	public Employee(long id, String name, String dept) {
		this.employeeId = id;
		this.employeeName = name;
		this.dept = dept;
	}
	public JsonObject toJson() {
	    JsonObject json = new JsonObject()
	        .put("employeeId", employeeId)
	        .put("employeeName", employeeName)
	    	.put("department", dept);
	    if (employeeId == 0) {
	      json.put("_id", employeeId);
	    }
	    return json;
	  }
	public long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
}
