package packager;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author javip
 */
public class Package implements Serializable{
    
    private String nick, ip, info, msg, status;
    private Map<String, String[]> ips;
    
    public void setNick(String nick){this.nick = nick;}
    public void setIp(String ip){this.ip = ip;}
    public void setInfo(String info){this.info = info;}
    public void setMsg(String msg){this.msg = msg;}
    public void setStatus(String st){this.status = st;}
    public void setObj(Map<String, String[]> ips){this.ips = ips;}
    public String getNick(){return nick;}
    public String getIp(){return ip;}
    public String getInfo(){return info;}
    public String getMsg(){return msg;}
    public String getStatus(){return status;}
    public Map<String,String[]> getObj(){return ips;}
}
