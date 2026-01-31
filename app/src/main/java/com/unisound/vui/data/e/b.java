package com.unisound.vui.data.e;

import com.unisound.vui.data.entity.out.UniContact;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class b {
    private UniContact a(com.unisound.vui.data.entity.a.b bVar) {
        UniContact uniContact = new UniContact();
        uniContact.setContactId(bVar.a().longValue());
        uniContact.setContactName(bVar.b());
        uniContact.setContactNamePinYin(bVar.c());
        uniContact.getContactPhoneNO().add(bVar.d());
        return uniContact;
    }

    public List<UniContact> a(List<com.unisound.vui.data.entity.a.b> list) {
        ArrayList arrayList = new ArrayList();
        HashMap map = new HashMap();
        for (com.unisound.vui.data.entity.a.b bVar : list) {
            if (map.containsKey(bVar.b())) {
                ((UniContact) map.get(bVar.b())).getContactPhoneNO().add(bVar.d());
            } else {
                UniContact uniContactA = a(bVar);
                map.put(bVar.b(), uniContactA);
                arrayList.add(uniContactA);
            }
        }
        return arrayList;
    }
}
