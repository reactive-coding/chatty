package com.reactiveapps.chatty.utils;

import java.util.HashMap;

public class FileType {
    // Audio
    public static final int TYPE_MP3 = 1;
    public static final int TYPE_M4A = 2;
    public static final int TYPE_WAV = 3;
    public static final int TYPE_AMR = 4;
    public static final int TYPE_AWB = 5;
    public static final int TYPE_WMA = 6;
    public static final int TYPE_OGG = 7;
    public static final int TYPE_AAC = 8;

    private static final int FIRST_AUDIO_TYPE = TYPE_MP3;
    private static final int LAST_AUDIO_TYPE = TYPE_AAC;

    // MIDI
    public static final int TYPE_MID = 11;
    public static final int TYPE_SMF = 12;
    public static final int TYPE_IMY = 13;

    private static final int FIRST_MIDI_TYPE = TYPE_MID;
    private static final int LAST_MIDI_TYPE = TYPE_IMY;

    // Video
    public static final int TYPE_MP4 = 21;
    public static final int TYPE_M4V = 22;
    public static final int TYPE_3GPP = 23;
    public static final int TYPE_3GPP2 = 24;
    public static final int TYPE_WMV = 25;
    public static final int TYPE_MPG = 26;
    public static final int TYPE_ASF = 27;
    public static final int TYPE_AVI = 28;
    public static final int TYPE_DIVX = 29;
    public static final int TYPE_SDP = 32;

    private static final int FIRST_VIDEO_TYPE = TYPE_MP4;
    private static final int LAST_VIDEO_TYPE = TYPE_DIVX;

    // Image
    public static final int TYPE_JPEG = 51;
    public static final int TYPE_GIF = 52;
    public static final int TYPE_PNG = 53;
    public static final int TYPE_BMP = 54;
    public static final int TYPE_WBMP = 55;

    private static final int FIRST_IMAGE_TYPE = TYPE_JPEG;
    private static final int LAST_IMAGE_TYPE = TYPE_WBMP;

    // Playlist file types from mtp protocal
    public static final int TYPE_M3U = 71;
    public static final int TYPE_PLS = 72;
    public static final int TYPE_WPL = 73;
    private static final int FIRST_PLAYLIST_TYPE = TYPE_M3U;
    private static final int LAST_PLAYLIST_TYPE = TYPE_WPL;

    // Documents
    public static final int TYPE_PDF = 81;
    public static final int TYPE_DOC = 82;
    public static final int TYPE_XLS = 83;
    public static final int TYPE_PPT = 84;
    public static final int TYPE_TXT = 85;
    private static final int FIRST_DOCUMENT_TYPE = TYPE_PDF;
    private static final int LAST_DOCUMENT_TYPE = TYPE_TXT;
    public static final int TYPE_DCF = 87;
    public static final int TYPE_ODF = 88;
    public static final int TYPE_QSS = 86;

    // flash files
    public static final int TYPE_SWF = 90;
    public static final int TYPE_SVG = 91;
    private static final int FIRST_FLASH_TYPE = TYPE_SWF;
    private static final int LAST_FLASH_TYPE = TYPE_SVG;

    // android install package
    public static final int TYPE_APK = 100;
    private static final int FIRST_INSTALL_TYPE = TYPE_APK;
    private static final int LAST_INSTALL_TYPE = TYPE_APK;

    // j2me
    public static final int TYPE_JAD = 110;
    public static final int TYPE_JAR = 111;
    private static final int FIRST_JAVA_TYPE = TYPE_JAD;
    private static final int LAST_JAVA_TYPE = TYPE_JAR;

    // vnote, vcalender
    public static final int TYPE_VCS = 120;
    public static final int TYPE_VCF = 121;
    public static final int TYPE_VNT = 122;

    private static class FileInfo {
        int fileType;
        String mimeType;
        String description;

        FileInfo(int fileType, String mimeType, String desc) {
            this.fileType = fileType;
            this.mimeType = mimeType;
            this.description = desc;
        }
    }

    private static HashMap<String, FileInfo> mFileTypeMap = new HashMap<String, FileInfo>();

    static void addFileType(String extension, int fileType, String mimeType,
                            String desc) {
        mFileTypeMap.put(extension, new FileInfo(fileType, mimeType, desc));
    }

    static {
        addFileType("MP3", TYPE_MP3, "audio/mpeg", "Mpeg");
        addFileType("M4A", TYPE_M4A, "audio/mp4", "M4A");
        addFileType("WAV", TYPE_WAV, "audio/x-wav", "WAVE");
        addFileType("AMR", TYPE_AMR, "audio/amr", "AMR");
        addFileType("AWB", TYPE_AWB, "audio/amr-wb", "AWB");
        addFileType("WMA", TYPE_WMA, "audio/x-ms-wma", "WMA");
        addFileType("OGG", TYPE_OGG, "audio/ogg", "OGG");
        addFileType("AAC", TYPE_AAC, "audio/aac", "AAC");

        addFileType("MID", TYPE_MID, "audio/midi", "MIDI");
        addFileType("XMF", TYPE_MID, "audio/midi", "XMF");
        addFileType("MXMF", TYPE_MID, "audio/midi", "MXMF");
        addFileType("RTTTL", TYPE_MID, "audio/midi", "RTTTL");
        addFileType("SMF", TYPE_SMF, "audio/sp-midi", "SMF");
        addFileType("IMY", TYPE_IMY, "audio/imelody", "IMY");
        addFileType("MIDI", TYPE_MID, "audio/midi", "MIDI");

        addFileType("MPEG", TYPE_MPG, "video/mpeg", "MPEG");
        addFileType("MPG", TYPE_MPG, "video/mpeg", "MPEG");
        addFileType("MP4", TYPE_MP4, "video/mp4", "MP4");
        addFileType("M4V", TYPE_M4V, "video/mp4", "M4V");
        addFileType("3GP", TYPE_3GPP, "video/3gpp", "3GP");
        addFileType("3GPP", TYPE_3GPP, "video/3gpp", "3GPP");
        addFileType("3G2", TYPE_3GPP2, "video/3gpp2", "3G2");
        addFileType("3GPP2", TYPE_3GPP2, "video/3gpp2", "3GPP2");
        addFileType("WMV", TYPE_WMV, "video/x-ms-wmv", "WMV");
        addFileType("ASF", TYPE_ASF, "video/x-ms-asf", "ASF");
        addFileType("AVI", TYPE_AVI, "video/avi", "AVI");
        addFileType("DIVX", TYPE_DIVX, "video/divx", "DIVX");
        addFileType("SDP", TYPE_SDP, "application/sdp", "SDP");

        addFileType("JPG", TYPE_JPEG, "image/jpeg", "JPEG");
        addFileType("JPEG", TYPE_JPEG, "image/jpeg", "JPEG");
        addFileType("MY5", TYPE_JPEG, "image/vnd.tmo.my5", "JPEG");
        addFileType("GIF", TYPE_GIF, "image/gif", "GIF");
        addFileType("PNG", TYPE_PNG, "image/png", "PNG");
        addFileType("BMP", TYPE_BMP, "image/x-ms-bmp", "Microsoft BMP");
        addFileType("WBMP", TYPE_WBMP, "image/vnd.wap.wbmp", "Wireless BMP");

        addFileType("QSS", TYPE_QSS, "slide/qss", "QSS");

        addFileType("M3U", TYPE_M3U, "audio/x-mpegurl", "M3U");
        addFileType("PLS", TYPE_PLS, "audio/x-scpls", "WPL");
        addFileType("WPL", TYPE_WPL, "application/vnd.ms-wpl", " ");

        addFileType("PDF", TYPE_PDF, "application/pdf", "Acrobat PDF");
        addFileType("DOC", TYPE_DOC, "application/msword",
                "Microsoft Office WORD");
        addFileType("DOCX", TYPE_DOC, "application/msword",
                "Microsoft Office WORD");
        addFileType("XLS", TYPE_XLS, "application/vnd.ms-excel",
                "Microsoft Office Excel");
        addFileType("XLSX", TYPE_XLS, "application/vnd.ms-excel",
                "Microsoft Office Excel");
        addFileType("PPT", TYPE_PPT, "application/vnd.ms-powerpoint",
                "Microsoft Office PowerPoint");
        addFileType("PPTX", TYPE_PPT, "application/vnd.ms-powerpoint",
                "Microsoft Office PowerPoint");
        addFileType("TXT", TYPE_TXT, "text/plain", "Text Document");

        addFileType("SWF", TYPE_SWF, "application/x-shockwave-flash", "SWF");
        addFileType("SVG", TYPE_SVG, "image/svg+xml", "SVG");

        addFileType("APK", TYPE_APK, "application/vnd.android.package-archive",
                "Android package install file");

        addFileType("JAD", TYPE_JAD, "text/vnd.sun.j2me.app-descriptor ", "JAD");
        addFileType("JAR", TYPE_JAR, "application/java-archive ", "JAR");

        addFileType("VCS", TYPE_VCS, "text/x-vCalendar", "VCS");
        addFileType("VCF", TYPE_VCF, "text/x-vcard", "VCF");
        addFileType("VNT", TYPE_VNT, "text/x-vnote", "VNT");
    }

    public static final String UNKNOWN_STRING = "<unknown>";

    public static boolean isAudio(int fileType) {
        return ((fileType >= FIRST_AUDIO_TYPE && fileType <= LAST_AUDIO_TYPE) || (fileType >= FIRST_MIDI_TYPE && fileType <= LAST_MIDI_TYPE));
    }

    public static boolean isVideo(int fileType) {
        return (fileType >= FIRST_VIDEO_TYPE && fileType <= LAST_VIDEO_TYPE);
    }

    public static boolean isImage(int fileType) {
        return (fileType >= FIRST_IMAGE_TYPE && fileType <= LAST_IMAGE_TYPE);
    }

    public static boolean isPlayList(int fileType) {
        return (fileType >= FIRST_PLAYLIST_TYPE && fileType <= LAST_PLAYLIST_TYPE);
    }

    public static boolean isDocument(int fileType) {
        return (fileType >= FIRST_DOCUMENT_TYPE && fileType <= LAST_DOCUMENT_TYPE);
    }

    public static boolean isFlash(int fileType) {
        return (fileType >= FIRST_FLASH_TYPE && fileType <= LAST_FLASH_TYPE);
    }

    public static boolean isInstall(int fileType) {
        return (fileType >= FIRST_INSTALL_TYPE && fileType <= LAST_INSTALL_TYPE);
    }

    public static boolean isJava(int fileType) {
        return (fileType >= FIRST_JAVA_TYPE && fileType <= LAST_JAVA_TYPE);
    }

    public static String getExtension(String path) {
        if (path != null) {
            int lastDot = path.lastIndexOf(".");
            if (lastDot < 0)
                return UNKNOWN_STRING;
            return path.substring(lastDot + 1).toUpperCase();
        } else {
            return "";
        }
    }

    private static FileInfo getType(String path) {
        String strExt = getExtension(path);
        return mFileTypeMap.get(strExt);
    }

    public static int getTypeInt(String path) {
        FileInfo mediaType = getType(path);
        return (mediaType == null ? 0 : mediaType.fileType);
    }

    public static String getMimeType(String path) {
        FileInfo mediaType = getType(path);
        return (mediaType == null ? "" : mediaType.mimeType);
    }

    public static String getDescription(String path) {
        FileInfo mediaType = getType(path);
        return (mediaType == null ? "" : mediaType.description);
    }

    public static String getShareMimeType(String path) {
        FileInfo mediaType = getType(path);
        return (mediaType == null ? "application/*" : mediaType.mimeType);
    }

}
