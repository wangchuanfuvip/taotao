package com.taotao.search.pojo;

import org.apache.solr.client.solrj.beans.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

    @Field("id")
    private String id;

    @Field("title")
    private String title;

 
  

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


}
