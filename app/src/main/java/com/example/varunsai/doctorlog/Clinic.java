package com.example.varunsai.doctorlog;

import java.util.ArrayList;

public class Clinic {
    static ArrayList<Clinic> cliniclist=new ArrayList<>();
    String starttime,endtime,fee,cname,ckey;
    String address,lat,lng;
    String phone1;
    ArrayList<String> days;
    Clinic(String starttime,String endtime,String fee,String cname,String phone1,ArrayList<String> days,String address,String lat,String lng)
    {   this.address=address;
     this.cname=cname;
     this.endtime=endtime;
     this.starttime=starttime;
     this.fee=fee;
     this.lat=lat;
     this.lng=lng;
     this.phone1=phone1;
     this.days=days;
    }
    Clinic(String ckey,String starttime,String endtime,String fee,String cname,String phone1,ArrayList<String> days,String address,String lat,String lng)
    {   this.address=address;
        this.cname=cname;
        this.ckey=ckey;
        this.endtime=endtime;
        this.starttime=starttime;
        this.fee=fee;
        this.lat=lat;
        this.lng=lng;
        this.phone1=phone1;
        this.days=days;
    }

}
