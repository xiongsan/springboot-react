package com.fable.demo.common.pojo;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument
public class Vpnrequestlog {
    @Field
    @Id
    private Integer id;
    @Field
    private String logflag;
    @Field
    private String numid;
    @Field
    private String requesttime;
    @Field
    private String requestid;
    @Field
    private String requester;
    @Field
    private String organization;
    @Field
    private String organizationid;
    @Field
    private String context;
    @Field
    private String responseid;
    @Field
    private String interfacename;
    @Field
    private String requestresult;
    @Field
    private String errorcode;
    @Field
    private String datatype;
    @Field
    private String dataid;
    @Field
    private String datapath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogflag() {
        return logflag;
    }

    public void setLogflag(String logflag) {
        this.logflag = logflag == null ? null : logflag.trim();
    }

    public String getNumid() {
        return numid;
    }

    public void setNumid(String numid) {
        this.numid = numid == null ? null : numid.trim();
    }

    public String getRequesttime() {
        return requesttime;
    }

    public void setRequesttime(String requesttime) {
        this.requesttime = requesttime == null ? null : requesttime.trim();
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid == null ? null : requestid.trim();
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester == null ? null : requester.trim();
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization == null ? null : organization.trim();
    }

    public String getOrganizationid() {
        return organizationid;
    }

    public void setOrganizationid(String organizationid) {
        this.organizationid = organizationid == null ? null : organizationid.trim();
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    public String getResponseid() {
        return responseid;
    }

    public void setResponseid(String responseid) {
        this.responseid = responseid == null ? null : responseid.trim();
    }

    public String getInterfacename() {
        return interfacename;
    }

    public void setInterfacename(String interfacename) {
        this.interfacename = interfacename == null ? null : interfacename.trim();
    }

    public String getRequestresult() {
        return requestresult;
    }

    public void setRequestresult(String requestresult) {
        this.requestresult = requestresult == null ? null : requestresult.trim();
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode == null ? null : errorcode.trim();
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype == null ? null : datatype.trim();
    }

    public String getDataid() {
        return dataid;
    }

    public void setDataid(String dataid) {
        this.dataid = dataid == null ? null : dataid.trim();
    }

	public String getDatapath() {
		return datapath;
	}

	public void setDatapath(String datapath) {
		this.datapath = datapath;
	}


}