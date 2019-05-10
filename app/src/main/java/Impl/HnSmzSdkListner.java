package Impl;

import JsonBean.GetAddPerson;
import android.graphics.Bitmap;

import java.util.List;

public interface HnSmzSdkListner {
    void getPersonRegister(Bitmap bitmap, GetAddPerson getAddPerson);
//    void loadFacesFromDB();
    void cleanAllListner(List<GetAddPerson> getAddPeopleList);
    void delFaceDataByUserId(String id);

}
