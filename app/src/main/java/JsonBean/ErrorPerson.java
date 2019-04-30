package JsonBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class ErrorPerson {


    @Id(autoincrement = true)
    private Long pid;
    private String user_id;
    private String name;
    private String work_sn;
    private String ic;
    private String id;
    private String id_card;
    private String phone_card;
    private String iris_template;
    private String iris_count;
    private String enroll_type;
    private String face_template;
    private String idface_template;
    private String vlface_template;


    @Generated(hash = 1344931035)
    public ErrorPerson(Long pid, String user_id, String name, String work_sn,
            String ic, String id, String id_card, String phone_card,
            String iris_template, String iris_count, String enroll_type,
            String face_template, String idface_template, String vlface_template) {
        this.pid = pid;
        this.user_id = user_id;
        this.name = name;
        this.work_sn = work_sn;
        this.ic = ic;
        this.id = id;
        this.id_card = id_card;
        this.phone_card = phone_card;
        this.iris_template = iris_template;
        this.iris_count = iris_count;
        this.enroll_type = enroll_type;
        this.face_template = face_template;
        this.idface_template = idface_template;
        this.vlface_template = vlface_template;
    }

    @Generated(hash = 310968221)
    public ErrorPerson() {
    }


    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWork_sn() {
        return work_sn;
    }

    public void setWork_sn(String work_sn) {
        this.work_sn = work_sn;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getPhone_card() {
        return phone_card;
    }

    public void setPhone_card(String phone_card) {
        this.phone_card = phone_card;
    }

    public String getIris_template() {
        return iris_template;
    }

    public void setIris_template(String iris_template) {
        this.iris_template = iris_template;
    }

    public String getIris_count() {
        return iris_count;
    }

    public void setIris_count(String iris_count) {
        this.iris_count = iris_count;
    }

    public String getEnroll_type() {
        return enroll_type;
    }

    public void setEnroll_type(String enroll_type) {
        this.enroll_type = enroll_type;
    }

    public String getFace_template() {
        return face_template;
    }

    public void setFace_template(String face_template) {
        this.face_template = face_template;
    }

    public String getIdface_template() {
        return idface_template;
    }

    public void setIdface_template(String idface_template) {
        this.idface_template = idface_template;
    }

    public String getVlface_template() {
        return vlface_template;
    }

    public void setVlface_template(String vlface_template) {
        this.vlface_template = vlface_template;
    }
}
