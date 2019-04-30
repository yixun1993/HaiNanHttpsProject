package JsonBean;

public class FeeBackExample {
    private  int type ;
    private String sn;
    private String msg;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "FeeBackExample{" +
                "type=" + type +
                ", sn='" + sn + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
