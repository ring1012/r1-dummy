package com.unisound.vui.data.entity.out;

import com.unisound.vui.common.a.b;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class UniContact extends b implements Serializable {
    private long contactId;
    private String contactName;
    private String contactNamePinYin;
    private ArrayList<String> contactPhoneNO;
    private int hasPhoneNumber;
    private boolean isHotlineNum;
    private boolean isMultiMark;
    private int photoId;
    private List<String> spellWordsList;

    public UniContact() {
        this.contactId = -1L;
        this.contactName = "";
        this.contactNamePinYin = "";
        this.photoId = 0;
        this.hasPhoneNumber = 0;
        this.contactPhoneNO = new ArrayList<>();
        this.isMultiMark = false;
        this.isHotlineNum = false;
    }

    public UniContact(int contactId, String contactName, String contactNamePinYin, int photoId, int hasPhoneNumber, ArrayList<String> contactPhoneNO) {
        this.contactId = -1L;
        this.contactName = "";
        this.contactNamePinYin = "";
        this.photoId = 0;
        this.hasPhoneNumber = 0;
        this.contactPhoneNO = new ArrayList<>();
        this.isMultiMark = false;
        this.isHotlineNum = false;
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactNamePinYin = contactNamePinYin;
        this.photoId = photoId;
        this.hasPhoneNumber = hasPhoneNumber;
        this.contactPhoneNO = contactPhoneNO;
    }

    private String createParticipleText(String contactName) {
        return contactName + ":";
    }

    public void cleanPhone() {
        this.contactPhoneNO.clear();
    }

    public long getContactId() {
        return this.contactId;
    }

    public String getContactName() {
        return this.contactName;
    }

    public String getContactNamePinYin() {
        return this.contactNamePinYin;
    }

    public ArrayList<String> getContactPhoneNO() {
        return this.contactPhoneNO;
    }

    public int getHasPhoneNumber() {
        return this.hasPhoneNumber;
    }

    public int getPhotoId() {
        return this.photoId;
    }

    public boolean isContainSearchName(String name) {
        return this.spellWordsList != null && this.spellWordsList.contains(name);
    }

    public boolean isHotlineNum() {
        return this.isHotlineNum;
    }

    public boolean isMultiMark() {
        return this.isMultiMark;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactNamePinYin(String contactNamePinYin) {
        this.contactNamePinYin = contactNamePinYin;
    }

    public void setContactPhoneNO(ArrayList<String> contactPhoneNO) {
        this.contactPhoneNO = contactPhoneNO;
    }

    public void setHasPhoneNumber(int hasPhoneNumber) {
        this.hasPhoneNumber = hasPhoneNumber;
    }

    public void setIsHotlineNum(boolean isHotlineNum) {
        this.isHotlineNum = isHotlineNum;
    }

    public void setMultiMark(boolean multiMark) {
        this.isMultiMark = multiMark;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public void setSpellWordsList(List<String> l) {
        this.spellWordsList = l;
    }

    @Override // com.unisound.vui.common.a.b
    public String toString() {
        return createParticipleText(this.contactName);
    }
}
