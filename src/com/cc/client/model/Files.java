package com.cc.client.model;

import java.io.Serializable;

public class Files implements Serializable {

  private long fileid;
  private String filename;
  private long filesize;
  private String fromuser;
  private String touser;


  public long getFileid() {
    return fileid;
  }

  public void setFileid(long fileid) {
    this.fileid = fileid;
  }


  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }


  public long getFilesize() {
    return filesize;
  }

  public void setFilesize(long filesize) {
    this.filesize = filesize;
  }


  public String getFromuser() {
    return fromuser;
  }

  public void setFromuser(String fromuser) {
    this.fromuser = fromuser;
  }


  public String getTouser() {
    return touser;
  }

  public void setTouser(String touser) {
    this.touser = touser;
  }

}
