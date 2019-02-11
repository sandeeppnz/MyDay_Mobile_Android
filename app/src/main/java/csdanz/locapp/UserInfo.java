package csdanz.locapp;

public class UserInfo {

    public String Name;
    public String Email;
    public String Uid;
    public String VisitorId;

    public UserInfo(String name,String email)
    {
        this.Email = email;
        this.Name = name;
        this.Uid ="";
        this.VisitorId ="";
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public void setVisitorId(String visitorId) {
        VisitorId = visitorId;
    }
}
