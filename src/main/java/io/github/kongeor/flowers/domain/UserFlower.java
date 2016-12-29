package io.github.kongeor.flowers.domain;

import com.google.gson.annotations.Expose;

public class UserFlower {

    @Expose
    private Long id;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Expose
    private Long userId;
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    @Expose
    private Long flowerId;
    public Long getFlowerId() { return flowerId; }
    public void setFlowerId(Long flowerId) { this.flowerId = flowerId; }

    @Expose
    private String notes;
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
	return "UserFlower{" + "id=" + id + ", userId=" + userId + ", flowerId=" + flowerId + ", notes='" + notes + '\''
	    + '}';
    }
}
