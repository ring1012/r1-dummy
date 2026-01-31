package com.unisound.client;

import com.unisound.common.y;
import com.unisound.sdk.ci;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ErrorCode {
    public static final int ASRCLIENT_COMPRESS_PCM_ERROR = -30003;
    public static final int ASRCLIENT_INVALID_PARAMETERS = -30004;
    public static final int ASRCLIENT_MAX_SPEECH_TIMEOUT = -30002;
    public static final int ASRCLIENT_VAD_TIMEOUT = -30001;
    public static final int ASR_ERROR_RESETMODEL = -50001;
    public static final int ASR_FIXENGINE_AUTHORIZE_ERROR = -63608;
    public static final int ASR_FIXENGINE_COMMUNICATION_ERROR = -63602;
    public static final int ASR_FIXENGINE_COMPRESS_PCM_ERROR = -63603;
    public static final int ASR_FIXENGINE_ENGINE_ERROR = -63612;
    public static final int ASR_FIXENGINE_FATAL_ERROR = -63601;
    public static final int ASR_FIXENGINE_MAX_SPEECH_TIMEOUT = -63606;
    public static final int ASR_FIXENGINE_MEM_ALLOCATION_ERROR = -63604;
    public static final int ASR_FIXENGINE_MODEL_NOT_SUPPORT = -63611;
    public static final int ASR_FIXENGINE_RECOGNIZER_WRONG_OPS = -63609;
    public static final int ASR_FIXENGINE_SERVICE_NOT_RUNNING = -63605;
    public static final int ASR_FIXENGINE_TRANS_ERROR = -63600;
    public static final int ASR_FIXENGINE_UNKNOW_ERROR = -63999;
    public static final int ASR_FIXENGINE_VAD_TIMEOUT = -63607;
    public static final int ASR_FOURMIC_RECORDING_ERROR = -63544;
    public static final int ASR_INSERT_VOCABCONTENT_ERROR = -63005;
    public static final int ASR_INSERT_VOCABNAME_ERROR = -63004;
    public static final int ASR_INSERT_VOCAB_ENGINE_ERROR = -63006;
    public static final int ASR_SDK_ACTIVATE_ERROR = -63543;
    public static final int ASR_SDK_ACTIVATE_NO_PERMISSION = -67004;
    public static final int ASR_SDK_ACTIVATE_OVER_MAX_ACT_FREQUENCY = -67005;
    public static final int ASR_SDK_ACTIVATE_SIGN_ERROR = -67003;
    public static final int ASR_SDK_APPKEY_MD5_CHECK_ERROR = -63533;
    public static final int ASR_SDK_FIX_COMPILE_ERROR = -63504;
    public static final int ASR_SDK_FIX_COMPILE_NO_INIT = -63503;
    public static final int ASR_SDK_FIX_INSERTVOCAB_EXT_ERROR = -63505;
    public static final int ASR_SDK_FIX_LOADGRAMMAR_ERROR = -63506;
    public static final int ASR_SDK_FIX_RECOGNIZER_INIT_ERROR = -63501;
    public static final int ASR_SDK_FIX_RECOGNIZER_NO_INIT = -63502;
    public static final int ASR_SDK_INIT_ERROR = -63531;
    public static final int ASR_SDK_NO_NLURESULT_ERROR = -63551;
    public static final int ASR_SDK_SET_ASR_SERVER_ADDR_ERROR = -63542;
    public static final int ASR_SDK_START_ERROR = -63532;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ERR_BAD_PARA = -65807;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ERR_DECODE = -65803;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ERR_ILLEGAL_CHAR = -65800;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ERR_IO = -65804;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ERR_NOT_CONSISTENCY = -65805;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ERR_NO_WORD = -65802;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ERR_SAME_DATA = -65808;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ERR_TOO_MANY_WORDS = -65801;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ERR_WRONG_NEW_WORDS = -65806;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ONLINE_EMPTY_WORD = -63407;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ONLINE_ENCODE_ERROR = -63405;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ONLINE_ERROR = -63406;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ONLINE_EXCEED_MAXLIMIT = -63402;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ONLINE_ILLEGAL = -63401;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ONLINE_NETWORK_ERROR = -63403;
    public static final int ASR_SDK_UPLOAD_ONESHOT_ONLINE_SERVER_REFUSED = -63404;
    public static final int ASR_SDK_WAKEUP_COMPILE_ERROR = -63540;
    public static final int ASR_TOKEN_ERROR = -69001;
    public static final int CHECK_MD5_ERROR = -91154;
    public static final int COMPRESS_PCM_ERROR = -91156;
    public static final int CONNECT_ERROR = -90002;
    public static final int CONNECT_SELECT_ERROR = -90003;
    public static final int CONNECT_SELECT_TIMEOUT = -90004;
    public static final int DECODE_ERROR = -91005;
    public static final int ERR_BAD_PARA = 807;
    public static final int ERR_DECODE = 803;
    public static final int ERR_ILLEGAL_CHAR = 800;
    public static final int ERR_IO = 804;
    public static final int ERR_NOT_CONSISTENCY = 805;
    public static final int ERR_NO_WORD = 802;
    public static final int ERR_SAME_DATA = 808;
    public static final int ERR_TOO_MANY_WORDS = 801;
    public static final int ERR_WRONG_NEW_WORDS = 806;
    public static final int FAILED_START_RECORDING = -61001;
    public static final int GENERAL_COMPILER_INIT_ERROR = -64002;
    public static final int GENERAL_INIT_ERROR = -64001;
    public static final int GENERAL_NO_READ_PHONE_STATE_PERMISSION = -68001;
    public static final int GET_HOST_NAME_ERROR = -90013;
    public static final int GET_INFO_ERROR = -91134;
    public static final int GET_NO_RESPONSE_ERROR = -91010;
    public static final int GET_RESPONSE_ERROR = -91008;
    public static final int HANDLE_ERROR = -91138;
    public static final int HTTP_CONNECT_ERROR = -91007;
    public static final int HTTP_ERROR_ACTIVATE = -91745;
    public static final int HTTP_ERROR_APPKEY = -91744;
    public static final int HTTP_ERROR_COMPOSITE_SERVICES = -91740;
    public static final int HTTP_ERROR_CORE_SERVER_CONNECT_ERROR = -91743;
    public static final int HTTP_ERROR_DECODER_NOT_MATCH_SPEECH_CODING = -91831;
    public static final int HTTP_ERROR_DECODING_ERROR = -91825;
    public static final int HTTP_ERROR_ENGINE_ERROR = -91812;
    public static final int HTTP_ERROR_EXCEED_MAXIMUM_LENGTH = -91823;
    public static final int HTTP_ERROR_EXCEED_MAXIMUM_TEXT_LENGTH = -91830;
    public static final int HTTP_ERROR_FIND_NO_SERVER = -91742;
    public static final int HTTP_ERROR_FORBID = -91703;
    public static final int HTTP_ERROR_HTTP_VERION = -91725;
    public static final int HTTP_ERROR_INNER_SERVER = -91720;
    public static final int HTTP_ERROR_MD5_VALIDATION_ERROR = -91727;
    public static final int HTTP_ERROR_MIN = -91700;
    public static final int HTTP_ERROR_NEED_PAY = -91702;
    public static final int HTTP_ERROR_NLU_PARAMS_PARSE_ERROR = -91802;
    public static final int HTTP_ERROR_NLU_PROCESSING_ERROR = -91804;
    public static final int HTTP_ERROR_NLU_RESOURCE_INIT_ERROR = -91806;
    public static final int HTTP_ERROR_NLU_RETURN_MUST_JSON = -91800;
    public static final int HTTP_ERROR_NLU_RETURN_NULL = -91801;
    public static final int HTTP_ERROR_NOBASIC_SERVER = -91721;
    public static final int HTTP_ERROR_NOTFIND_SERVER = -91704;
    public static final int HTTP_ERROR_NO_CARRY_KEY_INFO = -91722;
    public static final int HTTP_ERROR_NO_CARRY_SESSIONID = -91730;
    public static final int HTTP_ERROR_NO_ENCRYPTION_NO_SESSIONID = -91733;
    public static final int HTTP_ERROR_NO_SERVICES = -91741;
    public static final int HTTP_ERROR_NO_SUPPORT_SERVICE = -91723;
    public static final int HTTP_ERROR_NULL_APPKEY_AND_USERID = -91824;
    public static final int HTTP_ERROR_NULL_TEXT = -91833;
    public static final int HTTP_ERROR_PARAMS_ERROR = -91736;
    public static final int HTTP_ERROR_POST_PROCESSING_ERROR = -91807;
    public static final int HTTP_ERROR_POST_TRAFFIC_PARAMETER_ERROR = -91808;
    public static final int HTTP_ERROR_PROXY_REQ = -91707;
    public static final int HTTP_ERROR_REQUESTSERVICE_NO_SESSIONID = -91734;
    public static final int HTTP_ERROR_REQUEST_TIMEOUT = -91814;
    public static final int HTTP_ERROR_REQUEST_TOO_FREQUENT = -91735;
    public static final int HTTP_ERROR_REQ_INFO_LOSE = -91724;
    public static final int HTTP_ERROR_RESOURCE_ALLOCATION_ERROR = -91815;
    public static final int HTTP_ERROR_RESOURCE_STATE_ERROR = -91813;
    public static final int HTTP_ERROR_SERVER_INNER_ERROR = -91739;
    public static final int HTTP_ERROR_SERVER_LOAD = -91732;
    public static final int HTTP_ERROR_SERVER_TIMEOUT = -91738;
    public static final int HTTP_ERROR_SERVICE_PARAM_ERROR = -91818;
    public static final int HTTP_ERROR_SERVICE_RESOURCE_SHORTAGE = -91811;
    public static final int HTTP_ERROR_SESSIONID_ERROR = -91746;
    public static final int HTTP_ERROR_TOKEN_ERROR = -91737;
    public static final int HTTP_ERROR_TOKEN_VALIDATION_ERROR = -91731;
    public static final int HTTP_ERROR_TRAFFIC_PARAMETER_ERROR = -91805;
    public static final int HTTP_ERROR_UNAUTHORIZED = -91701;
    public static final int HTTP_ERROR_UNKNOWN_ERROR = -91819;
    public static final int HTTP_ERROR_UNSUPPORTED_CODING = -91821;
    public static final int HTTP_ERROR_UNSUPPORTED_COMMOND = -91817;
    public static final int HTTP_ERROR_UNSUPPORTED_COMPRESSION_FORMAT = -91822;
    public static final int HTTP_ERROR_UNSUPPORTED_FAR = -91820;
    public static final int HTTP_ERROR_UNSUPPORTED_FIELD = -91826;
    public static final int HTTP_ERROR_UNSUPPORTED_REQUEST_COMMAND = -91726;
    public static final int HTTP_ERROR_UNSUPPORTED_SERVICE = -91816;
    public static final int HTTP_ERROR_UNSUPPORTED_TEXT = -91832;
    public static final int HTTP_REQ_ERROR = -91004;
    public static final int ILLEGAL_DEVICESN = -69002;
    public static final int INIT_ERROR = -91000;
    public static final int INVALID_INPUT_DATA = -91157;
    public static final int MAX_SPEECH_TIMEOUT = -91155;
    public static final int NETWORK_ERROR = -91003;
    public static final int NLU_OTHER_ERROR = -71003;
    public static final int NLU_REQUEST_EMPTY = -71002;
    public static final int NLU_SERVER_ERROR = -71001;
    public static final int NO_INIT_ERROR = -91001;
    public static final int NO_SECRET_ERROR = -91158;
    public static final int NO_START_ERROR = -91006;
    public static final int NO_SUPPORT_CODEC_ERROR = -91136;
    public static final int NO_SUPPORT_FORMAT_ERROR = -91135;
    public static final int OPTION_ID_ERROR = -91151;
    public static final int OPTION_PARAM_ERROR = -91152;
    public static final int OTHER_ERROR = -90020;
    public static final int RECOGNITION_EXCEPTION = -62001;
    public static final int RECOGNITION_TIMEOUT = -62002;
    public static final int RECOGNIZER_OK = 0;
    public static final int RECORDING_EXCEPTION = -61002;
    public static final int RECV_ERROR = -90008;
    public static final int RECV_SELECT_ERROR = -90009;
    public static final int RECV_SELECT_TIMEOUT = -90010;
    public static final int REQ_INIT_ERROR = -91002;
    public static final int SEND_ERROR = -90005;
    public static final int SEND_REQUEST_ERROR = -91009;
    public static final int SEND_SELECT_ERROR = -90006;
    public static final int SEND_SELECT_TIMEOUT = -90007;
    public static final int SER_IP_ADDRESS_ERROR = -91137;
    public static final int SET_CRYPT_VERSION_ERROR = -91153;
    public static final int SET_FCNTL_ERROR = -90012;
    public static final int SET_SERVICE_ERROR = -91133;
    public static final int SET_SOCKET_OPTION_ERROR = -90011;
    public static final int SOCKET_ERROR = -90001;
    public static final int SUCCESS = 0;
    public static final int TEXT_NULL_ERROR = -91131;
    public static final int TEXT_TOO_LONG_ERROR = -91132;
    public static final int TOKEN_CREATE_ERROR = -91141;
    public static final int TTS_ERROR_AUDIOSOURCE_OPEN = -73306;
    public static final int TTS_ERROR_GET_ENGINE_INFO = -73311;
    public static final int TTS_ERROR_GET_MODEL = -73301;
    public static final int TTS_ERROR_LOAD_MODEL = -73302;
    public static final int TTS_ERROR_OFFLINE_CHANGE_SPEAKER_FAIL = -73310;
    public static final int TTS_ERROR_OFFLINE_CHANGE_SPEAKER_IS_PROCESSING = -73312;
    public static final int TTS_ERROR_OFFLINE_ENGINE_IS_PROCESSING = -73309;
    public static final int TTS_ERROR_OFFLINE_ENGINE_NOT_INIT = -73308;
    public static final int TTS_ERROR_OFFLINE_SYNTHESIZER_SET_TEXT = -73304;
    public static final int TTS_ERROR_ONLINE_SYNTHESIZER_INIT = -73305;
    public static final int TTS_ERROR_PLAYING_EXCEPTION = -73307;
    public static final int TTS_ERROR_TEXT_UNUSEABLE = -73303;
    public static final int TTS_PLAYING_ERROR = 11;
    public static final int TTS_SYNTHESIZE_ERROR = 10;
    public static final int UPLOAD_SCENE_DATA_EMPTY = -63013;
    public static final int UPLOAD_SCENE_DATA_NETWORK_ERROR = -63012;
    public static final int UPLOAD_SCENE_DATA_SERVER_REFUSED = -63011;
    public static final int UPLOAD_SCENE_DATA_SIZE_IS_FORBIDDEN = -63025;
    public static final int UPLOAD_SCENE_DATA_TOO_FAST = -63019;
    public static final int UPLOAD_SCENE_ENCODE_ERROR = -63020;
    public static final int UPLOAD_SCENE_GENERAL_ERROR = -63021;
    public static final int UPLOAD_SCENE_INVALID_KEY = -63022;
    public static final int UPLOAD_SCENE_INVALID_VER = -63026;
    public static final int UPLOAD_SCENE_OUT_MAX_COUNT = -63014;
    public static final int UPLOAD_SCENE_STREAM_IO_ERR = -63023;
    public static final int UPLOAD_SCENE_TOO_LARGE = -63017;
    public static final int UPLOAD_SCENE_UNKNOWN_ERR = -63024;
    public static final int UPLOAD_USER_DATA_EMPTY = -63003;
    public static final int UPLOAD_USER_DATA_NETWORK_ERROR = -63002;
    public static final int UPLOAD_USER_DATA_SERVER_REFUSED = -63001;
    public static final int UPLOAD_USER_DATA_TOO_FAST = -63009;
    public static final int UPLOAD_USER_ENCODE_ERROR = -63010;
    public static final int UPLOAD_USER_TOO_LARGE = -63007;
    public static final int VPR_CLIENT_PARAM_ERROR = 400;
    public static final int VPR_REGISTE_AUDIO_SHORT_ERROR = 504;
    public static final int VPR_REGISTE_ERROR = 501;
    public static final int VPR_USERNAME_VPRDATA_ERROR = 502;
    public static final int VPR_VERIFY_AUDIO_ERROR = 503;

    public static ci createOralError(int i) {
        switch (i) {
            case RECOGNITION_EXCEPTION /* -62001 */:
                return new ci(i, "识别异常");
            case RECORDING_EXCEPTION /* -61002 */:
                return new ci(i, "录音异常");
            case FAILED_START_RECORDING /* -61001 */:
                return new ci(i, "启动录音失败");
            case ASRCLIENT_COMPRESS_PCM_ERROR /* -30003 */:
                return new ci(i, "数据压缩错误");
            case ASRCLIENT_MAX_SPEECH_TIMEOUT /* -30002 */:
                return new ci(i, "说话时间超出限制");
            default:
                return new ci(-10001, "服务器连接错误");
        }
    }

    public static String toJsonMessage(int i) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("errorCode", i);
            jSONObject.put("errorMsg", toMessage(i));
        } catch (JSONException e) {
            e.printStackTrace();
            y.c("ErrorCode", "errorCode Not Define.");
        }
        return jSONObject.toString();
    }

    public static String toMessage(int i) {
        switch (i) {
            case HTTP_ERROR_NULL_TEXT /* -91833 */:
                return "用户传入的文本为空";
            case HTTP_ERROR_UNSUPPORTED_TEXT /* -91832 */:
                return "用户传入不支持的文本格式";
            case HTTP_ERROR_DECODER_NOT_MATCH_SPEECH_CODING /* -91831 */:
                return "解码器与语音编码不匹配";
            case HTTP_ERROR_EXCEED_MAXIMUM_TEXT_LENGTH /* -91830 */:
                return "文本长度超过最大文本长度";
            case HTTP_ERROR_UNSUPPORTED_FIELD /* -91826 */:
                return "不支持的领域错误";
            case HTTP_ERROR_DECODING_ERROR /* -91825 */:
                return "解码错误";
            case HTTP_ERROR_NULL_APPKEY_AND_USERID /* -91824 */:
                return "appKey和userId为空";
            case HTTP_ERROR_EXCEED_MAXIMUM_LENGTH /* -91823 */:
                return "请求的语音文件超过最大语音文件长度";
            case HTTP_ERROR_UNSUPPORTED_COMPRESSION_FORMAT /* -91822 */:
                return "不支持的压缩格式";
            case HTTP_ERROR_UNSUPPORTED_CODING /* -91821 */:
                return "不支持的编码率";
            case HTTP_ERROR_UNSUPPORTED_FAR /* -91820 */:
                return "不支持远讲错误";
            case HTTP_ERROR_UNKNOWN_ERROR /* -91819 */:
                return "未知错误";
            case HTTP_ERROR_SERVICE_PARAM_ERROR /* -91818 */:
                return "服务参数错误";
            case HTTP_ERROR_UNSUPPORTED_COMMOND /* -91817 */:
                return "不支持的命令";
            case HTTP_ERROR_UNSUPPORTED_SERVICE /* -91816 */:
                return "不支持的服务";
            case HTTP_ERROR_RESOURCE_ALLOCATION_ERROR /* -91815 */:
                return "资源分配错误";
            case HTTP_ERROR_REQUEST_TIMEOUT /* -91814 */:
                return "请求超时";
            case HTTP_ERROR_RESOURCE_STATE_ERROR /* -91813 */:
                return "资源状态错误";
            case HTTP_ERROR_ENGINE_ERROR /* -91812 */:
                return "引擎返回错误";
            case HTTP_ERROR_SERVICE_RESOURCE_SHORTAGE /* -91811 */:
                return "服务资源不足";
            case HTTP_ERROR_POST_TRAFFIC_PARAMETER_ERROR /* -91808 */:
                return "trafficParameter设置错误";
            case HTTP_ERROR_POST_PROCESSING_ERROR /* -91807 */:
                return "后处理错误";
            case HTTP_ERROR_NLU_RESOURCE_INIT_ERROR /* -91806 */:
                return "nlu资源初始化错误";
            case HTTP_ERROR_TRAFFIC_PARAMETER_ERROR /* -91805 */:
                return "trafficParameter错误";
            case HTTP_ERROR_NLU_PROCESSING_ERROR /* -91804 */:
                return "nlu处理过程中出现错误";
            case HTTP_ERROR_NLU_PARAMS_PARSE_ERROR /* -91802 */:
                return "nlu参数解析错误";
            case HTTP_ERROR_NLU_RETURN_NULL /* -91801 */:
                return "nlu返回结果为空";
            case HTTP_ERROR_NLU_RETURN_MUST_JSON /* -91800 */:
                return "nlu返回格式错误";
            case HTTP_ERROR_SESSIONID_ERROR /* -91746 */:
                return "找不到此sessionID";
            case HTTP_ERROR_ACTIVATE /* -91745 */:
                return "激活功能出错";
            case HTTP_ERROR_APPKEY /* -91744 */:
                return "AppKey错误";
            case HTTP_ERROR_CORE_SERVER_CONNECT_ERROR /* -91743 */:
                return "核心服务连接错误";
            case HTTP_ERROR_FIND_NO_SERVER /* -91742 */:
                return "找不到请求的服务器";
            case HTTP_ERROR_NO_SERVICES /* -91741 */:
                return "没有基础服务可以连接";
            case HTTP_ERROR_COMPOSITE_SERVICES /* -91740 */:
                return "组合服务全部返回错误";
            case HTTP_ERROR_SERVER_INNER_ERROR /* -91739 */:
                return "服务器内部异常";
            case HTTP_ERROR_SERVER_TIMEOUT /* -91738 */:
                return "服务器已经超时，客户端两次请求间隔太长";
            case HTTP_ERROR_TOKEN_ERROR /* -91737 */:
                return "产生token错误";
            case HTTP_ERROR_PARAMS_ERROR /* -91736 */:
                return "参数错误";
            case HTTP_ERROR_REQUEST_TOO_FREQUENT /* -91735 */:
                return "请求太过频繁";
            case HTTP_ERROR_REQUESTSERVICE_NO_SESSIONID /* -91734 */:
                return "没有该服务";
            case HTTP_ERROR_NO_ENCRYPTION_NO_SESSIONID /* -91733 */:
                return "请求无sessionID";
            case HTTP_ERROR_SERVER_LOAD /* -91732 */:
                return "找不到核心服务";
            case HTTP_ERROR_TOKEN_VALIDATION_ERROR /* -91731 */:
                return "Token验证错误";
            case HTTP_ERROR_NO_CARRY_SESSIONID /* -91730 */:
                return "未带sessionID错误";
            case HTTP_ERROR_MD5_VALIDATION_ERROR /* -91727 */:
                return "验证错误";
            case HTTP_ERROR_UNSUPPORTED_REQUEST_COMMAND /* -91726 */:
                return "不支持的请求命令";
            case HTTP_ERROR_HTTP_VERION /* -91725 */:
                return "HTTP版本不受支持";
            case HTTP_ERROR_REQ_INFO_LOSE /* -91724 */:
                return "本次请求信息丢失";
            case HTTP_ERROR_NO_SUPPORT_SERVICE /* -91723 */:
                return "服务连接错误";
            case HTTP_ERROR_NO_CARRY_KEY_INFO /* -91722 */:
                return "未携带关键信息";
            case HTTP_ERROR_NOBASIC_SERVER /* -91721 */:
                return "没有基础服务可以连接";
            case HTTP_ERROR_INNER_SERVER /* -91720 */:
                return "服务器内部错误";
            case HTTP_ERROR_PROXY_REQ /* -91707 */:
                return "代理认证请求";
            case HTTP_ERROR_NOTFIND_SERVER /* -91704 */:
                return "服务器找不到给定的资源";
            case HTTP_ERROR_FORBID /* -91703 */:
                return "禁止";
            case HTTP_ERROR_NEED_PAY /* -91702 */:
                return "需要付款";
            case HTTP_ERROR_UNAUTHORIZED /* -91701 */:
                return "未授权";
            case HTTP_ERROR_MIN /* -91700 */:
                return "错误请求";
            case NO_SECRET_ERROR /* -91158 */:
                return "没有设置secret";
            case INVALID_INPUT_DATA /* -91157 */:
                return "输入数据非法";
            case COMPRESS_PCM_ERROR /* -91156 */:
                return "传输pcm过程中失败";
            case MAX_SPEECH_TIMEOUT /* -91155 */:
                return "请求时间过长";
            case CHECK_MD5_ERROR /* -91154 */:
                return "服务器签名验证错误";
            case SET_CRYPT_VERSION_ERROR /* -91153 */:
                return "设置加密版本号错误";
            case OPTION_PARAM_ERROR /* -91152 */:
                return "参数设置错误";
            case OPTION_ID_ERROR /* -91151 */:
                return "参数设置 ID 错误";
            case TOKEN_CREATE_ERROR /* -91141 */:
                return "Token产生错误";
            case HANDLE_ERROR /* -91138 */:
                return "句柄错误";
            case SER_IP_ADDRESS_ERROR /* -91137 */:
                return "IP 地址设置错误";
            case NO_SUPPORT_CODEC_ERROR /* -91136 */:
                return "解码格式不支持";
            case NO_SUPPORT_FORMAT_ERROR /* -91135 */:
                return "语音格式不支持";
            case GET_INFO_ERROR /* -91134 */:
                return "获取信息错误";
            case SET_SERVICE_ERROR /* -91133 */:
                return "服务设置错误";
            case TEXT_TOO_LONG_ERROR /* -91132 */:
                return "文本文件过长";
            case TEXT_NULL_ERROR /* -91131 */:
                return "文本文件为空";
            case GET_NO_RESPONSE_ERROR /* -91010 */:
            case SEND_REQUEST_ERROR /* -91009 */:
            case GET_RESPONSE_ERROR /* -91008 */:
            case NO_START_ERROR /* -91006 */:
            case HTTP_REQ_ERROR /* -91004 */:
            case NO_INIT_ERROR /* -91001 */:
            case INIT_ERROR /* -91000 */:
            case OTHER_ERROR /* -90020 */:
            case GET_HOST_NAME_ERROR /* -90013 */:
            case SET_FCNTL_ERROR /* -90012 */:
            case SET_SOCKET_OPTION_ERROR /* -90011 */:
            case RECV_SELECT_ERROR /* -90009 */:
            case RECV_ERROR /* -90008 */:
            case SEND_SELECT_TIMEOUT /* -90007 */:
            case SEND_SELECT_ERROR /* -90006 */:
            case SEND_ERROR /* -90005 */:
            case CONNECT_SELECT_TIMEOUT /* -90004 */:
            case CONNECT_SELECT_ERROR /* -90003 */:
            case CONNECT_ERROR /* -90002 */:
            case SOCKET_ERROR /* -90001 */:
                return "网络错误";
            case HTTP_CONNECT_ERROR /* -91007 */:
                return "联网失败";
            case DECODE_ERROR /* -91005 */:
                return "解码错误";
            case NETWORK_ERROR /* -91003 */:
            case RECV_SELECT_TIMEOUT /* -90010 */:
            case RECOGNITION_TIMEOUT /* -62002 */:
                return "网络超时";
            case REQ_INIT_ERROR /* -91002 */:
                return "设备无网络，联网失败";
            case TTS_ERROR_OFFLINE_CHANGE_SPEAKER_IS_PROCESSING /* -73312 */:
                return "离线tts正在执行切换发音人，请稍后再试";
            case TTS_ERROR_GET_ENGINE_INFO /* -73311 */:
                return "离线tts获取引擎信息错误，请先执行init并设置本地模式！";
            case TTS_ERROR_OFFLINE_CHANGE_SPEAKER_FAIL /* -73310 */:
                return "离线tts切换发音人模型错误";
            case TTS_ERROR_OFFLINE_ENGINE_IS_PROCESSING /* -73309 */:
                return "离线tts引擎正在执行合成，请稍等再试";
            case TTS_ERROR_OFFLINE_ENGINE_NOT_INIT /* -73308 */:
                return "离线tts引擎未初始化，请确认执行init并接收init回调！";
            case TTS_ERROR_PLAYING_EXCEPTION /* -73307 */:
                return "播放异常错误";
            case TTS_ERROR_AUDIOSOURCE_OPEN /* -73306 */:
                return "播放线程打开audioSource出错";
            case TTS_ERROR_ONLINE_SYNTHESIZER_INIT /* -73305 */:
                return "在线合成初始化错误";
            case TTS_ERROR_OFFLINE_SYNTHESIZER_SET_TEXT /* -73304 */:
                return "离线合成设置文成出错";
            case TTS_ERROR_TEXT_UNUSEABLE /* -73303 */:
                return "合成文本不可用";
            case TTS_ERROR_LOAD_MODEL /* -73302 */:
                return "模型加载失败！";
            case TTS_ERROR_GET_MODEL /* -73301 */:
                return "模型生成失败！";
            case NLU_OTHER_ERROR /* -71003 */:
                return "语义理解: 其他错误";
            case NLU_REQUEST_EMPTY /* -71002 */:
                return "语义理解: 语义请求结果为空";
            case NLU_SERVER_ERROR /* -71001 */:
                return "语义理解: 语义服务访问异常";
            case ILLEGAL_DEVICESN /* -69002 */:
                return "设备DeviceSn长度超出限制(最多24个字符)";
            case ASR_TOKEN_ERROR /* -69001 */:
                return "token校验失败";
            case GENERAL_NO_READ_PHONE_STATE_PERMISSION /* -68001 */:
                return "没有READ_PHONE_STATE权限";
            case ASR_SDK_ACTIVATE_OVER_MAX_ACT_FREQUENCY /* -67005 */:
                return "激活失败：超过了最大激活频率";
            case ASR_SDK_ACTIVATE_NO_PERMISSION /* -67004 */:
                return "激活失败：没有激活权限";
            case ASR_SDK_ACTIVATE_SIGN_ERROR /* -67003 */:
                return "激活失败：签名错误,设备appkey和secret不匹配";
            case ASR_SDK_UPLOAD_ONESHOT_ERR_SAME_DATA /* -65808 */:
                return "上传在线oneshot唤醒词错误：无新增词，不需要处理";
            case ASR_SDK_UPLOAD_ONESHOT_ERR_BAD_PARA /* -65807 */:
                return "上传在线oneshot唤醒词错误：参数错误";
            case ASR_SDK_UPLOAD_ONESHOT_ERR_WRONG_NEW_WORDS /* -65806 */:
                return "上传在线oneshot唤醒词错误：新增加的唤醒词在已经生效的词列表中";
            case ASR_SDK_UPLOAD_ONESHOT_ERR_NOT_CONSISTENCY /* -65805 */:
                return "上传在线oneshot唤醒词错误：唤醒词不一致：客户端上传的生效唤醒词和服务器端保存的不一致";
            case ASR_SDK_UPLOAD_ONESHOT_ERR_IO /* -65804 */:
                return "上传在线oneshot唤醒词错误：IO读取失败";
            case ASR_SDK_UPLOAD_ONESHOT_ERR_DECODE /* -65803 */:
                return "上传在线oneshot唤醒词错误：编解码失败";
            case ASR_SDK_UPLOAD_ONESHOT_ERR_NO_WORD /* -65802 */:
                return "上传在线oneshot唤醒词错误：没有唤醒词";
            case ASR_SDK_UPLOAD_ONESHOT_ERR_TOO_MANY_WORDS /* -65801 */:
                return "上传在线oneshot唤醒词错误：超过唤醒词上限值";
            case ASR_SDK_UPLOAD_ONESHOT_ERR_ILLEGAL_CHAR /* -65800 */:
                return "上传在线oneshot唤醒词错误：含有非法字符";
            case GENERAL_COMPILER_INIT_ERROR /* -64002 */:
                return "离线编译引擎未初始化";
            case GENERAL_INIT_ERROR /* -64001 */:
                return "程序初始化异常，请尝试重新调用init";
            case ASR_FIXENGINE_ENGINE_ERROR /* -63612 */:
                return "引擎使用错误";
            case ASR_FIXENGINE_MODEL_NOT_SUPPORT /* -63611 */:
                return "引擎模型ID错误";
            case ASR_FIXENGINE_RECOGNIZER_WRONG_OPS /* -63609 */:
                return "引擎OPS错误";
            case ASR_FIXENGINE_AUTHORIZE_ERROR /* -63608 */:
                return "引擎授权错误";
            case ASR_FIXENGINE_VAD_TIMEOUT /* -63607 */:
                return "引擎VAD超时";
            case ASR_FIXENGINE_MAX_SPEECH_TIMEOUT /* -63606 */:
                return "引擎超时";
            case ASR_FIXENGINE_SERVICE_NOT_RUNNING /* -63605 */:
                return "引擎没有运行";
            case ASR_FIXENGINE_MEM_ALLOCATION_ERROR /* -63604 */:
                return "引擎内存申请错误";
            case ASR_FIXENGINE_COMPRESS_PCM_ERROR /* -63603 */:
                return "引擎PCM压缩错误";
            case ASR_FIXENGINE_COMMUNICATION_ERROR /* -63602 */:
                return "引擎通讯错误";
            case ASR_FIXENGINE_FATAL_ERROR /* -63601 */:
                return "引擎致命错误";
            case ASR_SDK_NO_NLURESULT_ERROR /* -63551 */:
                return "语义结果为空异常";
            case ASR_FOURMIC_RECORDING_ERROR /* -63544 */:
                return "4mic录音错误";
            case ASR_SDK_ACTIVATE_ERROR /* -63543 */:
                return "激活失败";
            case ASR_SDK_SET_ASR_SERVER_ADDR_ERROR /* -63542 */:
                return "语言识别服务地址设置失败";
            case ASR_SDK_WAKEUP_COMPILE_ERROR /* -63540 */:
                return "离线唤醒模型编译失败";
            case ASR_SDK_APPKEY_MD5_CHECK_ERROR /* -63533 */:
                return "appkey校验失败";
            case ASR_SDK_START_ERROR /* -63532 */:
                return "开始识别异常";
            case ASR_SDK_INIT_ERROR /* -63531 */:
                return "引擎初始化异常";
            case ASR_SDK_FIX_LOADGRAMMAR_ERROR /* -63506 */:
                return "离线引擎加载模型失败";
            case ASR_SDK_FIX_INSERTVOCAB_EXT_ERROR /* -63505 */:
                return "离线引擎Ext编译加载模型失败";
            case ASR_SDK_FIX_COMPILE_ERROR /* -63504 */:
                return "离线识别模型编译失败";
            case ASR_SDK_FIX_COMPILE_NO_INIT /* -63503 */:
                return "离线引擎没有初始化,不能编译用户数据";
            case ASR_SDK_FIX_RECOGNIZER_NO_INIT /* -63502 */:
                return "离线引擎没有初始化";
            case ASR_SDK_FIX_RECOGNIZER_INIT_ERROR /* -63501 */:
                return "离线引擎初始化错误";
            case ASR_SDK_UPLOAD_ONESHOT_ONLINE_EMPTY_WORD /* -63407 */:
                return "上传在线oneshot唤醒词错误：含有null或空字符串";
            case ASR_SDK_UPLOAD_ONESHOT_ONLINE_ERROR /* -63406 */:
                return "上传在线oneshot唤醒词失败";
            case ASR_SDK_UPLOAD_ONESHOT_ONLINE_ENCODE_ERROR /* -63405 */:
                return "上传在线oneshot唤醒词错误：编码失败";
            case ASR_SDK_UPLOAD_ONESHOT_ONLINE_SERVER_REFUSED /* -63404 */:
                return "上传在线oneshot唤醒词错误：服务器拒绝";
            case ASR_SDK_UPLOAD_ONESHOT_ONLINE_NETWORK_ERROR /* -63403 */:
                return "上传在线oneshot唤醒词错误：网络错误";
            case ASR_SDK_UPLOAD_ONESHOT_ONLINE_EXCEED_MAXLIMIT /* -63402 */:
                return "上传在线oneshot唤醒词错误：超过唤醒词上限值";
            case ASR_SDK_UPLOAD_ONESHOT_ONLINE_ILLEGAL /* -63401 */:
                return "上传在线oneshot唤醒词错误：含有非法字符";
            case UPLOAD_SCENE_INVALID_VER /* -63026 */:
                return "上传场景数据无效的SDK版本号";
            case UPLOAD_SCENE_DATA_SIZE_IS_FORBIDDEN /* -63025 */:
                return "上传场景数据上传的数据体积异常";
            case UPLOAD_SCENE_UNKNOWN_ERR /* -63024 */:
                return "上传场景数据未知异常";
            case UPLOAD_SCENE_STREAM_IO_ERR /* -63023 */:
                return "上传场景数据上传的数据流异常";
            case UPLOAD_SCENE_INVALID_KEY /* -63022 */:
                return "上传场景数据无效的AppKey";
            case UPLOAD_SCENE_GENERAL_ERROR /* -63021 */:
                return "上传场景数据:常见错误";
            case UPLOAD_SCENE_ENCODE_ERROR /* -63020 */:
                return "上传场景数据:编码失败";
            case UPLOAD_SCENE_DATA_TOO_FAST /* -63019 */:
                return "上传场景数据:上传过于频繁";
            case UPLOAD_SCENE_TOO_LARGE /* -63017 */:
                return "上传场景数据:内容太多";
            case UPLOAD_SCENE_DATA_EMPTY /* -63013 */:
                return "上传场景数据:不能为空";
            case UPLOAD_SCENE_DATA_NETWORK_ERROR /* -63012 */:
                return "上传场景数据:网络连接失败";
            case UPLOAD_SCENE_DATA_SERVER_REFUSED /* -63011 */:
                return "上传场景数据:服务器拒绝";
            case UPLOAD_USER_ENCODE_ERROR /* -63010 */:
                return "上传个性化数据:编码失败";
            case UPLOAD_USER_DATA_TOO_FAST /* -63009 */:
                return "上传个性化数据:上传过于频繁";
            case UPLOAD_USER_TOO_LARGE /* -63007 */:
                return "上传个性化数据:内容太多";
            case UPLOAD_USER_DATA_EMPTY /* -63003 */:
                return "上传个性化数据:不能为空";
            case UPLOAD_USER_DATA_NETWORK_ERROR /* -63002 */:
                return "上传个性化数据:网络连接失败";
            case UPLOAD_USER_DATA_SERVER_REFUSED /* -63001 */:
                return "上传个性化数据:服务器拒绝";
            case RECOGNITION_EXCEPTION /* -62001 */:
                return "识别异常";
            case RECORDING_EXCEPTION /* -61002 */:
                return "录音异常";
            case FAILED_START_RECORDING /* -61001 */:
                return "启动录音失败";
            case ASR_ERROR_RESETMODEL /* -50001 */:
                return "正在重置模型，请勿重复调用";
            case ASRCLIENT_INVALID_PARAMETERS /* -30004 */:
                return "非法参数错误";
            case ASRCLIENT_COMPRESS_PCM_ERROR /* -30003 */:
                return "数据压缩错误";
            case ASRCLIENT_MAX_SPEECH_TIMEOUT /* -30002 */:
                return "说话时间超出限制";
            case 0:
                return "操作成功";
            case VPR_CLIENT_PARAM_ERROR /* 400 */:
                return "VPR客户端参数错误";
            case VPR_REGISTE_ERROR /* 501 */:
                return "声纹注册或验证异常";
            case VPR_USERNAME_VPRDATA_ERROR /* 502 */:
                return "重复用户名或者声纹持久化异常";
            case VPR_VERIFY_AUDIO_ERROR /* 503 */:
                return "无法提取声纹特征（声音超级短，或空白语音）";
            case VPR_REGISTE_AUDIO_SHORT_ERROR /* 504 */:
                return "VPR语音太短错误(最短注册5s,匹配2s)";
            default:
                return "其他异常";
        }
    }

    public ci createBasicError(int i) {
        if (i == 0) {
            return null;
        }
        int profession = toProfession(i);
        return new ci(profession, getMessage(profession));
    }

    public ci createPremiumError(int i) {
        if (i == 0) {
            return null;
        }
        return new ci(i, getMessage(toProfession(i)));
    }

    public ci createProfessionError(int i) {
        if (i == 0) {
            return null;
        }
        return new ci(i, getMessage(toProfession(i)));
    }

    public String getMessage(int i) {
        String message = toMessage(i);
        return message != null ? message : "错误:" + i;
    }

    public int toProfession(int i) {
        return i;
    }
}
