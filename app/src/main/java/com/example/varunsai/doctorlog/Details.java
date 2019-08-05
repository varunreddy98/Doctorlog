package com.example.varunsai.doctorlog;
public class Details {
    String pfname,gender,tokenno,dob,cause,phoneno,lat,lng,key;
    Details(String key,String pname,String gender,String tokenno,String dob,String cause,String  phoneno,String lat,String lng)
    {   this.key=key;
        this.phoneno=phoneno;
        this.pfname=pname;
        this.gender=gender;
        this.tokenno=tokenno;
        this.dob=dob;
        this.cause=cause;
        this.lat=lat;
        this.lng=lng;
    }
}
