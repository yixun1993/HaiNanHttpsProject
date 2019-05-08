package Impl;

import JsonBean.ErrorPerson;
import JsonBean.UploadAttendance;
import JsonBean.Uploadlogs;
import android.content.Context;
import com.example.hainanhttpsproject.HnSmz;

import java.util.List;

public interface HnSmzImpl {

     HnSmz InitSMZ(Context context);
     HnSmz setSn(String sn);
     HnSmz setDesKey(String desKey);
     HnSmz setHnSmzSdkListner(HnSmzSdkListner hnSmzSdkListner);
     void uploadAttendanceData();
     void addUpload(String mSn,String user_id);
     void getAddPerson();
     void addErrorPerson(ErrorPerson person);
     void build();
}
