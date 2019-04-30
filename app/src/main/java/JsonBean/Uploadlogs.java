package JsonBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Uploadlogs {
    @Id(autoincrement = true)
    private Long pid;
    private String sn;
    private String user_id;
    private String recog_time;
    private String recog_type;
    @Generated(hash = 1329132023)
    public Uploadlogs(Long pid, String sn, String user_id, String recog_time,
            String recog_type) {
        this.pid = pid;
        this.sn = sn;
        this.user_id = user_id;
        this.recog_time = recog_time;
        this.recog_type = recog_type;
    }
    @Generated(hash = 1389653473)
    public Uploadlogs() {
    }
    public void setSn(String sn) {
        this.sn = sn;
    }
    public String getSn() {
        return sn;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setRecog_time(String recog_time) {
        this.recog_time = recog_time;
    }
    public String getRecog_time() {
        return recog_time;
    }

    public void setRecog_type(String recog_type) {
        this.recog_type = recog_type;
    }
    public String getRecog_type() {
        return recog_type;
    }
}
