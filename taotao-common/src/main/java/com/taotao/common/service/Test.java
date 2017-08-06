package com.taotao.common.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Test {
public static void main(String[] args) {
	Random rand = new Random();
	int ms;
	ms = rand.nextInt(1000000000);
	Date date = new Date(ms);
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	System.out.println(sdf.format(date).toString());
	
	String s="2017-09-01 00:00:00";
	s=s.substring(0, 10);
	System.out.println(s);
	
	int [] arr = {1,2,3,4,44,66,65};
	//产生0-(arr.length-1)的整数值,也是数组的索引
	int index=(int)(Math.random()*arr.length);
	int rands = arr[index];
	System.out.println(rands);
	
	
	
	
	
    List<String> mlist = new ArrayList<>();
    mlist.add("zhu");
    mlist.add("wen");
    mlist.add("tao");
    final int size = mlist.size();
    String[] arrs =(String[])mlist.toArray(new String[size]);
   
   
        int max=1000;
        int min=100;
        Random random = new Random();

       System.out.println(random.nextInt(max)%(max-min+1) + min);
    String s1="1,3,4,5,8,9,";
    String s2=s1.substring(0, s1.lastIndexOf(","));
    String [] s3=s2.split(",");
    for(int i=0;i<s3.length;i++){

    	   System.out.println(s3[i]);
    	  }
System.out.println(s3);
    
	
}
}
