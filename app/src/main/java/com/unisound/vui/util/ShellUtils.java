package com.unisound.vui.util;

import java.util.List;

/* loaded from: classes.dex */
public class ShellUtils {
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_SU = "su";

    public static class CommandResult {
        public String errorMsg;
        public int result;
        public String successMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }

        public String toString() {
            return "CommandResult{result=" + this.result + ", successMsg='" + this.successMsg + "', errorMsg='" + this.errorMsg + "'}";
        }
    }

    private ShellUtils() {
        throw new AssertionError();
    }

    public static boolean checkRootPermission() {
        return execCommand("echo root", true, false).result == 0;
    }

    public static CommandResult execCommand(String command, boolean isRoot) {
        return execCommand(new String[]{command}, isRoot, true);
    }

    public static CommandResult execCommand(String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(new String[]{command}, isRoot, isNeedResultMsg);
    }

    public static CommandResult execCommand(List<String> commands, boolean isRoot) {
        return execCommand(commands == null ? null : (String[]) commands.toArray(new String[0]), isRoot, true);
    }

    public static CommandResult execCommand(List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(commands == null ? null : (String[]) commands.toArray(new String[0]), isRoot, isNeedResultMsg);
    }

    public static CommandResult execCommand(String[] commands, boolean isRoot) {
        return execCommand(commands, isRoot, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:159:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00d4  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0115  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0124  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.unisound.vui.util.ShellUtils.CommandResult execCommand(java.lang.String[] r11, boolean r12, boolean r13) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 423
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.vui.util.ShellUtils.execCommand(java.lang.String[], boolean, boolean):com.unisound.vui.util.ShellUtils$CommandResult");
    }
}
