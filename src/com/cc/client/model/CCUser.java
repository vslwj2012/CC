package com.cc.client.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CCUser implements Serializable {

  private String username;

    @Override
    public String toString() {
        return "CCUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", image='" + image + '\'' +
                ", repassword='" + repassword + '\'' +
                ", motto='" + motto + '\'' +
                ", friends=" + friends +
                '}';
    }

    @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CCUser ccUser = (CCUser) o;
    return Objects.equals(username, ccUser.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username);
  }

  private String password;
  private String nickname;
  private String image;
  private String repassword;
  private String motto;
  private Set<CCUser> friends=new HashSet<>();//好友列表
  private String isfriendtype;//判定是别人什么类型的好友

  public ArrayList<ChatMessage> getMessageList() {
    return messageList;
  }

  public void setMessageList(ArrayList<ChatMessage> messageList) {
    this.messageList = messageList;
  }

  private ArrayList<ChatMessage> messageList=new ArrayList<>();//存储未接受消息

  public String getIsfriendtype() {
    return isfriendtype;
  }

  public void setIsfriendtype(String isfriendtype) {
    this.isfriendtype = isfriendtype;
  }

  public String getMotto(){return motto;}
  public void setMotto(String motto){this.motto=motto;}

  public void setFriends(Set<CCUser> friends) {
    this.friends = friends;
  }

  public Set<CCUser> getFriends() {
    return friends;
  }

  public String getRepassword(){return repassword;}
  public void setRepassword(String repassword){this.repassword=repassword;}

  public String getUsername() {
    return username;
  }

  public void setUsername(String  username) {
    this.username = username;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }


  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

}
