package com.keshava.cloudbrary;

public class Books {

    private String  bookid;
    private String  bookimg;
    private String  bookname;
    private String  booktype;
    private String  bookprice;
    private String  bookcount;
    private String  bookdiscription;
    private int  reader;
    private int  status;

    public Books() {
    }

    public Books(String bookid, String bookimg, String bookname, String booktype, String bookprice, String bookcount, String bookdiscription, int reader, int status) {
        this.bookid = bookid;
        this.bookimg = bookimg;
        this.bookname = bookname;
        this.booktype = booktype;
        this.bookprice = bookprice;
        this.bookcount = bookcount;
        this.bookdiscription = bookdiscription;
        this.reader = reader;
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

    public String getBookcount() {
        return bookcount;
    }

    public String getBookdiscription() {
        return bookdiscription;
    }

    public int getReader() {
        return reader;
    }

    public int getStatus() {
        return status;
    }

    }
