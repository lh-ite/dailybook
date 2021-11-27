package com.example.dailybook;

public class dailybook {//用于存放日记本内容的类
    private String Writer;
    private String Content;
    private String Title;
    private String Time;
    private  Integer id;
    private int photeid;

    public Integer getId()
    {
        return id;
    }
    public  void setId(Integer d)
    {
        this.id=d;
    }
    public String getWriter()
    {
        return Writer;
    }

    public void setWriter(String writer) {
        this.Writer=writer;
    }

    public String getContent()
    {
        return Content;
    }

    public void setContent(String content) {
        this.Content=content;
    }

    public String getTitle()
    {
        return Title;
    }

    public void setTitle(String title)
    {
        this.Title=title;
    }

    public String getTime()
    {
        return Time;
    }

    public void setTime(String time)
    {
        this.Time=time;
    }
    public void setPhoteid(int id)
    {
        this.photeid=id;
    }
    public int getPhoteid()
    {
        return photeid;
    }

    public dailybook(Integer i,String title,String time,String content)
    {
        this.id=i;
        this.Title=title;
        this.Time=time;
        this.Content=content;
    }
}
