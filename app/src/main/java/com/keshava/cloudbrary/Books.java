package com.keshava.cloudbrary;

public class Books {

    private String  bookid;
    private String  bookimg;
    private String  bookname;
    private String  booktype;
    private String  bookprice;
    private int  bookcount;
    private String  bookdiscription;
    private int  status;

    public Books() {
    }

    public Books(String bookid, String bookimg, String bookname, String booktype, String bookprice, int bookcount, String bookdiscription, int status) {
        this.bookid = bookid;
        this.bookimg = bookimg;
        this.bookname = bookname;
        this.booktype = booktype;
        this.bookprice = bookprice;
        this.bookcount = bookcount;
        this.bookdiscription = bookdiscription;
        this.status = status;
    }

    public String getBookid() {
        return bookid;
    }

    public String getBookimg() {
        return bookimg;
    }

    public String getBookname() {
        return bookname;
    }

    public String getBooktype() {
        return booktype;
    }

    public String getBookprice() {
        return bookprice;
    }

    public int getBookcount() {
        return bookcount;
    }

    public String getBookdiscription() {
        return bookdiscription;
    }

    public int getStatus() {
        return status;
    }

    }
