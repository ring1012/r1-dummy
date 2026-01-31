package com.unisound.vui.util;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import cn.yunzhisheng.common.PinyinConverter;
import com.unisound.vui.data.entity.out.UniContact;
import com.unisound.vui.util.entity.CommandInfo;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import nluparser.scheme.MusicResult;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: classes.dex */
public class LocalizeDataUtil {
    private static final int CONTACT_DISPLAY_NAME_INDEX = 1;
    private static final int CONTACT_HAS_PHONE_NUMBER_INDEX = 2;
    private static final int CONTACT_ID_INDEX = 0;
    private static final int PHONE_CONTACT_ID_INDEX = 0;
    private static final int PHONE_NUMBER_INDEX = 1;
    private static LocalizeDataUtil localizeDataUtil;
    private Context context;
    private SAXParserFactory factory;
    private a handler;
    private Uri mAudioUri;
    private HashMap<String, CommandInfo> mCommandMap;
    private Uri mContactUri;
    private Uri mPhoneUri;
    private List<String> mUserCommands;
    private SAXParser parser;
    private static final String[] PROJECTION_CONTACTS = {"_id", "display_name", "has_phone_number"};
    private static ArrayList<CommandInfo> mCommandInFile = null;
    private List<com.unisound.vui.util.a.a> mAppinfos = new CopyOnWriteArrayList();
    private List<String> mAppsName = new CopyOnWriteArrayList();
    private final String contactSelectAll = "display_name NOTNULL AND display_name != '' ";
    private final String[] projectionPhone = {"contact_id", "data1", "data2", "data3", "is_primary", "is_super_primary"};
    private List<UniContact> mContacts = new CopyOnWriteArrayList();
    private List<com.unisound.vui.util.a.b> phoneNumberInfos = new CopyOnWriteArrayList();
    private List<String> contactNamesList = new ArrayList();
    private final ArrayList<String> supportedFormatter = new ArrayList<>();
    private List<MusicResult.Music> musicList = new ArrayList();
    private List<String> songsNameList = new ArrayList();
    private List<String> artistsNameList = new ArrayList();
    private List<String> albumsNameList = new ArrayList();

    private class a extends DefaultHandler {
        private ArrayList<CommandInfo> b;
        private CommandInfo c;
        private StringBuilder d;

        private a() {
        }

        public ArrayList<CommandInfo> a() {
            return this.b;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            this.d.append(ch, start, length);
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if ("user_command".equals(localName)) {
                this.c.setCommand(this.d.toString());
                return;
            }
            if ("user_operands".equals(localName)) {
                this.c.setOperands(this.d.toString());
                return;
            }
            if ("user_operator".equals(localName)) {
                this.c.setOperator(this.d.toString());
            } else if ("user_value".equals(localName)) {
                this.c.setValue(this.d.toString());
            } else if ("command".equals(localName)) {
                this.b.add(this.c);
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startDocument() throws SAXException {
            super.startDocument();
            this.b = new ArrayList<>();
            this.d = new StringBuilder();
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if ("command".equals(localName)) {
                this.c = new CommandInfo();
            }
            this.d.setLength(0);
        }
    }

    private LocalizeDataUtil(Context context) {
        this.supportedFormatter.add("wav");
        this.supportedFormatter.add("mp3");
        this.supportedFormatter.add("ogg");
        this.supportedFormatter.add("flac");
        this.supportedFormatter.add("m4a");
        this.supportedFormatter.add("aac");
        this.mUserCommands = new CopyOnWriteArrayList();
        this.mCommandMap = new HashMap<>();
        this.context = context;
    }

    public static CommandInfo getCommandInfo(String wakeupWord) {
        if (mCommandInFile == null) {
            return null;
        }
        Iterator<CommandInfo> it = mCommandInFile.iterator();
        while (it.hasNext()) {
            CommandInfo next = it.next();
            if (next.getCommand().contains(wakeupWord)) {
                return next;
            }
        }
        return null;
    }

    private ArrayList<UniContact> getNameMappingRecordByNamePinYin(String name) {
        ArrayList<UniContact> arrayList = new ArrayList<>();
        if (this.mContacts != null && this.mContacts.size() > 0) {
            String[] nameSpell1 = PinyinConverter.getNameSpell1(name);
            String str = nameSpell1[0];
            if (str == null || "".equals(str)) {
                return null;
            }
            String[] strArrSplit = str.split(PinyinConverter.PINYIN_SEPARATOR);
            for (int i = 0; i < this.mContacts.size(); i++) {
                UniContact uniContact = this.mContacts.get(i);
                String contactNamePinYin = uniContact.getContactNamePinYin();
                if (arrayList.size() >= 20) {
                    break;
                }
                if (strArrSplit.length == 1) {
                    if (!TextUtils.isEmpty(contactNamePinYin) && uniContact.isContainSearchName(nameSpell1[0])) {
                        arrayList.add(uniContact);
                    }
                } else if (!TextUtils.isEmpty(contactNamePinYin) && nameSpell1.length > 0 && contactNamePinYin.contains(nameSpell1[0])) {
                    arrayList.add(uniContact);
                }
            }
        }
        return arrayList;
    }

    private ArrayList<com.unisound.vui.util.a.b> getPhones(long contactId) {
        ArrayList<com.unisound.vui.util.a.b> arrayList = new ArrayList<>();
        if (this.phoneNumberInfos != null && this.phoneNumberInfos.size() > 0) {
            for (com.unisound.vui.util.a.b bVar : this.phoneNumberInfos) {
                if (bVar.a() == contactId) {
                    arrayList.add(bVar);
                }
            }
        }
        return arrayList;
    }

    private void getSystemContact() {
        Cursor cursorQuery;
        if (this.context == null || (cursorQuery = query(this.context.getContentResolver(), ContactsContract.Contacts.CONTENT_URI, PROJECTION_CONTACTS, "display_name NOTNULL AND display_name != '' ", null, null)) == null) {
            return;
        }
        if (cursorQuery.getCount() > 0) {
            while (cursorQuery.moveToNext()) {
                UniContact uniContact = new UniContact();
                uniContact.setContactId(cursorQuery.getInt(0));
                String string = cursorQuery.getString(1);
                uniContact.setHasPhoneNumber(cursorQuery.getInt(2));
                uniContact.setContactName(string);
                String[] nameSpell1 = PinyinConverter.getNameSpell1(string);
                if (nameSpell1 != null && nameSpell1.length > 0) {
                    uniContact.setContactNamePinYin(nameSpell1[0]);
                }
                this.contactNamesList.add(string);
                this.mContacts.add(uniContact);
            }
        }
        cursorQuery.close();
    }

    private void getSystemPhoneNumber() {
        Cursor cursorQuery;
        if (this.context == null || (cursorQuery = this.context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, this.projectionPhone, null, null, null)) == null) {
            return;
        }
        if (cursorQuery.getCount() > 0) {
            while (cursorQuery.moveToNext()) {
                com.unisound.vui.util.a.b bVar = new com.unisound.vui.util.a.b();
                bVar.a(cursorQuery.getInt(0));
                bVar.a(cursorQuery.getString(1));
                this.phoneNumberInfos.add(bVar);
            }
        }
        cursorQuery.close();
    }

    private boolean isContainChinese(String str) {
        return Pattern.compile("[一-龥]").matcher(str).find();
    }

    private void matchPhoneNumberToContact() {
        if (this.mContacts == null || this.mContacts.size() == 0 || this.phoneNumberInfos == null || this.phoneNumberInfos.size() == 0) {
            return;
        }
        for (UniContact uniContact : this.mContacts) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (com.unisound.vui.util.a.b bVar : this.phoneNumberInfos) {
                if (uniContact.getContactId() == bVar.a()) {
                    arrayList.add(bVar.b());
                    uniContact.setContactPhoneNO(arrayList);
                }
            }
        }
    }

    public static LocalizeDataUtil newInstance(Context context) {
        if (localizeDataUtil == null) {
            localizeDataUtil = new LocalizeDataUtil(context);
        }
        try {
            PinyinConverter.init(context.getAssets().open("un2py.mg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return localizeDataUtil;
    }

    private ArrayList<CommandInfo> parse(InputStream is) throws Exception {
        this.factory = SAXParserFactory.newInstance();
        this.parser = this.factory.newSAXParser();
        this.handler = new a();
        this.parser.parse(is, this.handler);
        return this.handler.a();
    }

    private Cursor query(ContentResolver resolver, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (SQLiteException e) {
            return null;
        }
    }

    private String removeSongNameBlankCharAndSuffix(String name) {
        if (!isContainChinese(name)) {
            return name.trim();
        }
        String name2 = name.replaceAll(PinyinConverter.PINYIN_SEPARATOR, "").trim();
        return !name2.endsWith("flac") ? name2.substring(0, name2.length() - 4) : name2.substring(0, name2.length() - 5);
    }

    private String removeStrBlankChar(String str) {
        return !isContainChinese(str) ? str.trim() : str.replaceAll(PinyinConverter.PINYIN_SEPARATOR, "").trim();
    }

    public List<String> getAlbumsNamesList() {
        return null;
    }

    public MusicResult getAllMusic() {
        return null;
    }

    public List<com.unisound.vui.util.a.a> getAppByName(String appName) {
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        if (this.mAppinfos != null && this.mAppinfos.size() > 0) {
            for (com.unisound.vui.util.a.a aVar : this.mAppinfos) {
                if (!TextUtils.isEmpty(aVar.a()) && aVar.a().equals(appName)) {
                    copyOnWriteArrayList.add(aVar);
                }
            }
        }
        return copyOnWriteArrayList;
    }

    public List<String> getApps() {
        return this.mAppsName.size() == 0 ? updateAppsData() : this.mAppsName;
    }

    public List<String> getArtistsNameList() {
        return null;
    }

    public HashMap<String, CommandInfo> getCommandMap() {
        return this.mCommandMap;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0011, code lost:
    
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.unisound.vui.data.entity.out.UniContact getContactByPhoneNumber(java.lang.String r8) {
        /*
            r7 = this;
            java.util.List<com.unisound.vui.data.entity.out.UniContact> r0 = r7.mContacts
            int r0 = r0.size()
            if (r0 != 0) goto Lb
            r7.updateContacts()
        Lb:
            java.util.List<com.unisound.vui.data.entity.out.UniContact> r0 = r7.mContacts
            java.util.Iterator r3 = r0.iterator()
        L11:
            boolean r0 = r3.hasNext()
            if (r0 == 0) goto L48
            java.lang.Object r0 = r3.next()
            com.unisound.vui.data.entity.out.UniContact r0 = (com.unisound.vui.data.entity.out.UniContact) r0
            java.util.ArrayList r4 = r0.getContactPhoneNO()
            int r1 = r4.size()
            if (r1 <= 0) goto L11
            r1 = 0
            r2 = r1
        L29:
            int r1 = r4.size()
            if (r2 >= r1) goto L11
            java.lang.Object r1 = r4.get(r2)
            java.lang.String r1 = (java.lang.String) r1
            java.lang.String r5 = " "
            java.lang.String r6 = ""
            java.lang.String r1 = r1.replaceAll(r5, r6)
            boolean r1 = r8.equals(r1)
            if (r1 == 0) goto L44
        L43:
            return r0
        L44:
            int r1 = r2 + 1
            r2 = r1
            goto L29
        L48:
            r0 = 0
            goto L43
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.vui.util.LocalizeDataUtil.getContactByPhoneNumber(java.lang.String):com.unisound.vui.data.entity.out.UniContact");
    }

    public List<String> getContacts() {
        return this.mContacts.size() == 0 ? updateContacts() : this.contactNamesList;
    }

    public MusicResult getMusicByAlbumName(String albumName) {
        return null;
    }

    public MusicResult getMusicByArtistName(String artistName) {
        return null;
    }

    public MusicResult getMusicBySongsName(String songName) {
        return null;
    }

    public List<String> getSongsNamesList() {
        return null;
    }

    public List<String> getUserDef() {
        return this.mUserCommands.size() == 0 ? updateUserCommands() : this.mUserCommands;
    }

    public void registerAppReceiver(BroadcastReceiver receiver) {
        if (this.context == null || receiver == null) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppStateReceiver.INSTALL_APP);
        intentFilter.addAction(AppStateReceiver.UNINSTALL_APP);
        intentFilter.addDataScheme("package");
        this.context.registerReceiver(receiver, intentFilter);
    }

    public void registerContactDataObserver(ContentObserver contactObserver, ContentObserver phoneNumberObserver) {
        if (this.context == null || contactObserver == null || phoneNumberObserver == null) {
            return;
        }
        this.mContactUri = ContactsContract.Contacts.CONTENT_URI;
        this.mPhoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        this.context.getContentResolver().registerContentObserver(this.mContactUri, true, contactObserver);
        this.context.getContentResolver().registerContentObserver(this.mPhoneUri, true, phoneNumberObserver);
    }

    public ArrayList<UniContact> searchContactByName(String name) {
        ArrayList<UniContact> nameMappingRecordByNamePinYin;
        com.unisound.vui.util.b.b bVar = new com.unisound.vui.util.b.b();
        ArrayList<UniContact> arrayList = new ArrayList<>();
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        if (arrayList != null && arrayList.size() <= 0) {
            if (this.mContacts != null && this.mContacts.size() > 0 && bVar != null && (nameMappingRecordByNamePinYin = getNameMappingRecordByNamePinYin(name)) != null && nameMappingRecordByNamePinYin.size() > 0) {
                linkedHashSet.addAll(nameMappingRecordByNamePinYin);
            }
            if (linkedHashSet != null && linkedHashSet.size() > 0) {
                Iterator it = linkedHashSet.iterator();
                while (it.hasNext()) {
                    arrayList.add((UniContact) it.next());
                }
            }
        }
        if (arrayList != null && arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                UniContact uniContact = arrayList.get(i);
                ArrayList<String> arrayList2 = new ArrayList<>();
                ArrayList<com.unisound.vui.util.a.b> phones = getPhones(uniContact.getContactId());
                uniContact.cleanPhone();
                if (phones != null) {
                    for (int i2 = 0; i2 < phones.size(); i2++) {
                        arrayList2.add(phones.get(i2).b().replaceAll("([^一-龥a-zA-Z0-9.])", ""));
                        uniContact.setContactPhoneNO(arrayList2);
                    }
                }
            }
        }
        return arrayList;
    }

    public void unRegisterContactObserver(ContentObserver contactObserver, ContentObserver phoneNumberObserver) {
        if (this.context == null || contactObserver == null || phoneNumberObserver == null) {
            return;
        }
        this.context.getContentResolver().unregisterContentObserver(contactObserver);
        this.context.getContentResolver().unregisterContentObserver(phoneNumberObserver);
    }

    public void unregisterAppReceiver(BroadcastReceiver receiver) {
        if (this.context == null || receiver == null || this.context == null || receiver == null) {
            return;
        }
        this.context.unregisterReceiver(receiver);
    }

    public List<String> updateAppsData() {
        this.mAppinfos.clear();
        this.mAppsName.clear();
        if (this.context == null) {
            return null;
        }
        PackageManager packageManager = this.context.getPackageManager();
        new Intent("android.intent.action.MAIN", (Uri) null).addCategory("android.intent.category.LAUNCHER");
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        if (installedPackages != null && installedPackages.size() > 0) {
            for (PackageInfo packageInfo : installedPackages) {
                com.unisound.vui.util.a.a aVar = new com.unisound.vui.util.a.a();
                aVar.a(packageInfo.packageName);
                this.mAppsName.add(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
                aVar.b(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
                this.mAppinfos.add(aVar);
            }
        }
        return this.mAppsName;
    }

    public List<String> updateContacts() {
        if (this.context == null) {
            return null;
        }
        this.mContacts.clear();
        this.phoneNumberInfos.clear();
        this.contactNamesList.clear();
        getSystemContact();
        getSystemPhoneNumber();
        matchPhoneNumberToContact();
        return this.contactNamesList;
    }

    public void updateMusics() {
    }

    public List<String> updateUserCommands() throws Throwable {
        InputStream inputStreamOpen;
        InputStream inputStream;
        this.mUserCommands.clear();
        if (this.context == null) {
            return null;
        }
        mCommandInFile = null;
        try {
            LogMgr.i("like", "start open commands/user_commands_config.xml");
            inputStreamOpen = this.context.getApplicationContext().getAssets().open("commands/user_commands_config.xml");
            try {
                LogMgr.i("like", "open commands/user_commands_config.xml succes");
                mCommandInFile = parse(inputStreamOpen);
                Iterator<CommandInfo> it = mCommandInFile.iterator();
                while (it.hasNext()) {
                    CommandInfo next = it.next();
                    String command = next.getCommand();
                    LogMgr.i("like", "mCommand = " + command + " mOperands= " + next.getOperands() + "  mOperator=" + next.getOperator() + " mValue=" + next.getValue());
                    if (!TextUtils.isEmpty(command)) {
                        String[] strArrSplit = command.split("\\|");
                        for (int i = 0; i < strArrSplit.length; i++) {
                            LogMgr.i("like", "mCommands[" + i + "] = " + strArrSplit[i]);
                            if (!TextUtils.isEmpty(strArrSplit[i])) {
                                this.mUserCommands.add(strArrSplit[i].trim());
                                this.mCommandMap.put(strArrSplit[i].trim(), next);
                            }
                        }
                    }
                }
                if (inputStreamOpen != null) {
                    try {
                        inputStreamOpen.close();
                    } catch (IOException e) {
                    } finally {
                    }
                }
            } catch (Exception e2) {
                inputStream = inputStreamOpen;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                        this.factory = null;
                        this.parser = null;
                        this.handler = null;
                    } catch (IOException e3) {
                    } finally {
                    }
                }
                return this.mUserCommands;
            } catch (Throwable th) {
                th = th;
                if (inputStreamOpen != null) {
                    try {
                        inputStreamOpen.close();
                    } catch (IOException e4) {
                    } finally {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            inputStream = null;
        } catch (Throwable th2) {
            th = th2;
            inputStreamOpen = null;
        }
        return this.mUserCommands;
    }
}
