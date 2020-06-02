package com.example.uber_taxi;

public class UserHelperClass {

        //empty constructor
        public UserHelperClass(){
            // Default constructor required for calls to DataSnapshot.getValue(User.class)

        }

    // This is for the "Customers " table of firebase database
        String email,password,name,address,date,mobile;



    public UserHelperClass(String demail, String dmobile, String dpwd,String dname,String daddr,String ddob){
            this.email=demail;
            this.password=dpwd;
            this.name=dname;
            this.address=daddr;
            this.date=ddob;
            this.mobile = dmobile;
        }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


}
