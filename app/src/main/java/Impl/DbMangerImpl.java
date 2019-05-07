package Impl;

import JsonBean.ErrorPerson;
import JsonBean.GetAddPerson;
import JsonBean.Uploadlogs;
import android.content.Context;
import android.util.Log;

import java.util.List;

public interface DbMangerImpl {

    void InitDb(Context context);
    void addPerson(GetAddPerson addPerson);
    void deletePerson(GetAddPerson addPerson);
    void updatePerson(GetAddPerson addPerson);
    void cleanAllPerson();
    void cleanGetPseronAll();
    void cleanGetPersonListBeanAll();
    GetAddPerson queryPersonByID(String id);
    void addUploadLogsBo(Uploadlogs uploadlogs);
    List<GetAddPerson> findPeson();
    //上传成功清空考勤数据库
    void deleteUploadData();
    //查询考勤数据
    List<Uploadlogs> queryUploadData();
    //注册失败保存失败人员数据库
    void addErrorPserson(ErrorPerson errorPerson);
    //根据user_id查询注册失败数据
//    ErrorPerson queryByUserId(String userId);

}
