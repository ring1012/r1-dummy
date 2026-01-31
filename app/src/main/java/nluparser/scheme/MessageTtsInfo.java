package nluparser.scheme;

/* loaded from: classes.dex */
public class MessageTtsInfo implements Comparable<MessageTtsInfo> {
    private String groupMemberId;
    private int msgContentType;
    private String msgFromContactsId;
    private String msgFromName;
    private String msgId;
    private String text;
    private long time;

    public MessageTtsInfo(String text, int msgContentType, String msgId, String msgFromContactsId, String msgFromGroupMemberId, String msgFromName, long time) {
        this.text = text;
        this.msgContentType = msgContentType;
        this.msgId = msgId;
        this.msgFromContactsId = msgFromContactsId;
        this.msgFromName = msgFromName;
        this.groupMemberId = msgFromGroupMemberId;
        this.time = time;
    }

    @Override // java.lang.Comparable
    public int compareTo(MessageTtsInfo another) {
        return (int) (getTime() - another.getTime());
    }

    public String getGroupMemberId() {
        return this.groupMemberId;
    }

    public int getMsgContentType() {
        return this.msgContentType;
    }

    public String getMsgFromContactsId() {
        return this.msgFromContactsId;
    }

    public String getMsgFromName() {
        return this.msgFromName;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public String getText() {
        return this.text;
    }

    public long getTime() {
        return this.time;
    }

    public boolean isNaviMessage() {
        return this.text != null && this.text.startsWith("type=location");
    }

    public void setGroupMemberId(String groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    public void setMsgContentType(int msgContentType) {
        this.msgContentType = msgContentType;
    }

    public void setMsgFromContactsId(String msgFromContactsId) {
        this.msgFromContactsId = msgFromContactsId;
    }

    public void setMsgFromName(String msgFromName) {
        this.msgFromName = msgFromName;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
