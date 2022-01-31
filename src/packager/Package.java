package packager;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author javip
 */
public class Package implements Serializable{
    
    private String nick, ip, move, msg, status;
    private ArrayList<String> ips;
    
    public void setNick(String nick){this.nick = nick;}
    public void setIp(String ip){this.ip = ip;}
    public void setMove(String move){this.move = move;}
    public void setMsg(String msg){this.msg = msg;}
    public void setStatus(String st){this.status = st;}
    public void setIps(ArrayList<String> ips){this.ips = ips;}
    public String getNick(){return nick;}
    public String getIp(){return ip;}
    public String getMove(){return move;}
    public String getMsg(){return msg;}
    public String getStatus(){return status;}
    public ArrayList<String> getIps(){return ips;}
}
