package com.reactiveapps.chatty.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

/**
 * 文件操作
 *
 * @author chengshengru
 */
public final class FileUtils {

    private static final String APP_DATA_DIR = "/CHATTY"; // 应用的数据存放目录
    private static final String IMAGE_CACHE_DIR = "/cache/image"; // 应用的图片缓存目录
    public static final String DIR_CACHE = "/cache"; // 缓存目录
    public static final String FILE_DIR = "/file"; // 应用的文件存放目录
    public static final String AUDIO_DIR = "/audio"; // 录音文件存放目录
    public static final String CRASH_DIR = "/crash"; // 崩溃文件存放目录
    public static final String IMAGE_DIR = "/image"; // 崩溃文件存放目录
    public static final String DIR_THUMBNAIL = "/thumbnail"; // 发送图片-压缩后的图片存放的目录
    public static final String FORWARD_SLASH = "/";
    private static final String COLON = ":";
    private static final String NO_MEDIA = ".nomedia";
    public static final String IMAGE_TEMP_SENDED_DIR = "/image/temp"; // 临时图片文件存放目录
    /**
     * 文件类型
     **/
    private static final int UNDEFINE = 0;
    private static final int DIRECTORY = 1;//目前pc未发文件夹，暂不展示
    private static final int EXCEL = 2;
    private static final int EXE = 3;
    private static final int IMAGE = 4;
    private static final int MUSIC = 5;
    private static final int PPT = 6;
    private static final int TXT = 7;
    private static final int WORD = 8;
    private static final int ZIP = 9;

    static {
        // 2014-12-3 唐珂 禁止系统媒体服务扫描该目录
        File file = new File(getImageCacheDir(), NO_MEDIA);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拍照文件存放的目录
     */
    public static final String CAMERA_DIR = "/Camera";
    public static final String CAMERA_LOW_DIR = "/Camera/LOW";
    private static final String REPLACE_FORWARD_SLASH = "@";
    private static final String REPLACE_COLON = "&";

    public static boolean hasExternalStorage() {
        String sdStatus = Environment.getExternalStorageState();
        if (sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 获取SD卡的文件路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return getSDCardFile().getAbsolutePath();
    }

    /**
     * 获取SD卡的根目录
     *
     * @return
     */
    public static File getSDCardFile() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 是否有SD卡
     *
     * @return
     */
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                || Environment.getExternalStorageState().equals(
                Environment.MEDIA_SHARED);
    }

    /**
     * 获取当前应用的数据存放目录
     *
     * @return
     */

    public static boolean isDirAvailable(String path, boolean makeIt) {
        File dir = new File(path);
        if (makeIt) {
            if (!dir.exists() && !dir.mkdirs()) {
                return false;
            } else {
                return true;
            }
        } else {
            return dir.exists();
        }
    }

    public static String getAppDataPath() {
        String tmp = getSDCardPath() + APP_DATA_DIR;
        isDirAvailable(tmp, true);
        return tmp;
    }

    /**
     * 创建用户缓存文件
     *
     * @param userName :用户登录名
     * @return
     */
    public static File getUserCacheFile(String userName) {
        String dirName = Md5Encrypt.md5(userName);
        File file = new File(getAppDataPath(), dirName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取图片缓存目录
     *
     * @return
     */
    public static File getImageCacheDir() {
        String path = getAppDataPath() + IMAGE_CACHE_DIR;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取图片目录
     *
     * @return
     */
    public static File getImageDir() {
        File file = new File(getAppDataPath() + IMAGE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取语音缓存目录
     *
     * @return
     */
    public static File getAudioCacheDir() {
        String path = getAppDataPath() + AUDIO_DIR;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();

        }
        return file;
    }

    /**
     * 获取崩溃文件目录
     *
     * @return
     */
    public static File getCrashCacheDir() {
        String path = getAppDataPath() + CRASH_DIR;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 生成崩溃路径
     *
     * @return
     */
    public static String creatCrashCachePath() {
        String path = getAppDataPath() + CRASH_DIR;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path + File.separator + "crash.trace";
    }

    /**
     * 生成语音缓存路径
     *
     * @return
     */
    public static String creatAudioCachePath() {
        String path = getAppDataPath() + AUDIO_DIR;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path + File.separator + String.valueOf(new Date().getTime())
                + ".spx";
    }

    /**
     * 生成复制图片的路径
     *
     * @return
     */
    public static String getCopyPicturePath() {
        String path = Environment.getExternalStorageDirectory() + "/DCIM/copy";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path + File.separator;
    }

    public static File getCameraDir() {
        String path = getAppDataPath() + CAMERA_DIR;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getCameraLowDir() {
        String path = getAppDataPath() + CAMERA_LOW_DIR + "."
                + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 缓存文件是否存在
     *
     * @param name
     * @return
     */
    public static File getCachedFile(String name) {
        File file = new File(getImageCacheDir(), name);
        return file;
    }

    /**
     * 获取文件的存放路径
     *
     * @return
     */
    public static File getSaveFileDir() {
        String path = getAppDataPath() + FILE_DIR;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 通过一个指定的url获取存放文件的完整路径
     *
     * @param url
     * @return
     */
    public static File getSaveFile(String url) {
        File fileDir = getSaveFileDir();
        return getFileFromUrl(fileDir, url);
    }

    /**
     * 通过一个url获取一个缓存图片的路径
     *
     * @param url
     * @return
     */
    public static File getImageCacheFile(String url) {
        File imageCache = getImageCacheDir();
        return getFileFromUrl(imageCache, url);
    }

    /**
     * 通过一个url获取一个缓存图片的路径名称(不创建文件实体)
     *
     * @param url
     * @return String 文件的全路径名称
     */
    public static String getImageCacheFilePath(String url) {
        StringBuffer sb = new StringBuffer();
        sb.append(getImageCacheDir()); // 目录
        sb.append("/"); // 路径分割符
        sb.append(converUrlToName(url)); // 文件名
        sb.append(getExpandedNameByUrl(url)); // 扩展名
        return sb.toString();
    }

    final public static String getExpandedNameByUrl(final String url) {
        if (url == null) {
            return url;
        }
        int n = url.lastIndexOf(".");
        if (n > 0)
            return url.substring(n);
        else
            return null;
    }

    /**
     * 通过一个url和存放目录，获取这个文件的绝对存放路径
     *
     * @param dir
     * @param url
     * @return
     */
    private static File getFileFromUrl(File dir, String url) {
        String fileName = converUrlToName(url);
        File file = new File(dir, fileName);
        /*
         * if (!file.exists()) { try { file.createNewFile(); } catch
		 * (IOException e) {
		 * 
		 * return null; } }
		 */
        return file;
    }

    /**
     * 把一个url转换成可以成为文件名的格式
     *
     * @param url
     * @return
     */
    private static String converUrlToName(String url) {
        if (!TextUtils.isEmpty(url)) {
            url = url.replaceAll(FORWARD_SLASH, REPLACE_FORWARD_SLASH);
            url = url.replaceAll(COLON, REPLACE_COLON);
        }
        return url;
    }

    /**
     * 生成最后一次照相的Uri 的文件名类似CHATTY.20120814225433.jpg
     *
     * @return
     */
    public static Uri createLastCameraUri() {
        String fileName = "CHATTY." + System.currentTimeMillis() + ".jpg";
        File cacheFile = new File(getCameraDir(), fileName);
        // 如果该文件已经存在删掉
        if (cacheFile.exists())
            cacheFile.delete();
        // 获取这个图片的URI
        lastCameraUri = Uri.fromFile(cacheFile);// 这是个实例变量，方便下面获取图片的时候用
        return lastCameraUri;
    }

    private static Uri lastCameraUri = null;
    private static String TAG = "TAG";

    // 获取最后的Uri 在Activity中使用
    public static Uri getLastCameraUri() {
        Uri uri = lastCameraUri;
        lastCameraUri = null; // 获取之后清空
        return uri;
    }

    /**
     * 根据文件名删掉临时文件
     *
     * @param fileName :文件名
     */
    public static void deleteCacheImage(String fileName) {
        File cacheFile = new File(getImageCacheDir(), fileName);
        if (cacheFile.exists())
            cacheFile.delete();
    }

    /**
     * 根据全路径中得到文件名称与扩展名
     *
     * @param fileName :文件全路径
     */
    public static String getFileName(final String str) {
        if (str == null) {
            return str;
        }
        int n = str.lastIndexOf("/");
        if (n > 0)
            return str.substring(n + 1);
        else
            return null;

    }

    /**
     * 得到文件的扩展名称
     *
     * @param fileName :文件名
     */
    public static String getExpandedName(final String str) {
        if (str == null) {
            return str;
        }
        int n = str.lastIndexOf(".");
        if (n > 0)
            return str.substring(n);
        else
            return null;
    }

    /**
     * 删除所有已下载的文件
     *
     * @param filterFile 不需要删除的文件
     */
    public static void removeAllDownloadFile(File filterFile) {
        File saveFileDir = getSaveFileDir();
        removeAllFileFromDir(saveFileDir, filterFile);
    }

    /**
     * 判断文件名是否合法 -- 文件名称不能全部使用特殊字符，至少使用一个数字、字母或者中文
     *
     * @return true:合法 --- false：不合法
     */
    public static boolean checkNameIsLegal(String text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }

        char[] ch = text.toCharArray();

        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            // 判断是否有数字或字母
            if (Character.isLetterOrDigit(c)) {
                return true;
            }

            // 判断是否含有中文
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
            if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                return true;
            }
        }

        return false;
    }

    /**
     * 删除所有的图片缓存
     */
    public static void removeAllImageCache() {
        Log.d(TAG, "removeAllImageCache() ---->");
        File imageDir = getImageCacheDir();
        removeAllFileFromDir(imageDir, null);
    }

    private static void removeAllFileFromDir(File dir, File filterFile) {
        Log.d(TAG, "removeAllFileFromDir() ---->");
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }

        String filterPath = "";
        if (filterFile != null) {
            filterPath = filterFile.getAbsolutePath();
        }

        if (filterPath == null) {
            filterPath = "";
        }

        File[] files = dir.listFiles();

        if (files == null || files.length < 1) {
            return;
        }

        for (File file : files) {
            if (file != null && !filterPath.equals(file.getAbsolutePath())
                    && file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 获取SD卡剩余的空间
     *
     * @return
     */
    public static long getAvailaleSize() {
        // 取得sdcard文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        /* 获取block的SIZE */
        long blockSize = stat.getBlockSize();
        /* 空闲的Block的数量 */
        long availableBlocks = stat.getAvailableBlocks();
        /* 返回bit大小值 */
        return availableBlocks * blockSize;
        // (availableBlocks * blockSize)/1024 KIB 单位
        // (availableBlocks * blockSize)/1024 /1024 MIB单位
    }

    /**
     * 返回SD卡的大小
     *
     * @return
     */
    public static long getAllSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        /* 获取block的SIZE */
        long blockSize = stat.getBlockSize();
        /* 块数量 */
        long availableBlocks = stat.getBlockCount();
        /* 返回bit大小值 */
        return availableBlocks * blockSize;
    }

    public static File getSmallFile(File file) {
        if (file != null) {
            String filename = file.getName();
            String extensionName = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
            if (0 != extensionName.length()) {
                extensionName = "." + extensionName;
            }

            String name = Md5Encrypt.md5(filename);
            return new File(getImageCacheDir() + "/" + name + extensionName);
        }
        return null;
    }

    /**
     * 根据Uri获取文件的真实路径
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {

        if (null == uri)
            return null;

        final String scheme = uri.getScheme();
        String data = null;

        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 删除所有的音频缓存文件
     */
    public static void removeAllAudioFile() {
        // TODO Auto-generated method stub
        Log.d(TAG, "removeAllAudioFile() ---->");
        File audioDir = getAudioCacheDir();
        removeAllFileFromDir(audioDir, null);
    }

    final public static String getDirBase() {
        return APP_DATA_DIR;
    }

    final public static String getDirStorage() {
        StringBuffer sb = new StringBuffer();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append("/");
        sb.append(getDirBase());
        return sb.toString();
    }

    public static File getThumbnailFileDir() {
        String path = getDirStorage() + DIR_THUMBNAIL;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static boolean isThumbnailFile(String path) {
        if (TextUtils.isEmpty(path)
                && path.startsWith(getThumbnailFileDir().getAbsolutePath())) {
            return true;
        }
        return false;
    }

    public static File converUrlToFile(File dir, String url) {
        String fileName = converUrlToFileName(url);
        File file = new File(dir, fileName);
        return file;
    }

    public static String converUrlToFileName(String url) {
        url = url.replaceAll(FORWARD_SLASH, REPLACE_FORWARD_SLASH);
        url = url.replaceAll(COLON, REPLACE_COLON);
        return url;
    }

    final public static String getDirCache() {
        StringBuffer sb = new StringBuffer();
        sb.append(getDirStorage());
        sb.append(DIR_CACHE);
        sb.append("/");
        return sb.toString();
    }

    public static boolean isFileExist(String filename) {
        if (filename != null) {
            return new File(filename).exists();
        } else {
            return false;
        }
    }

    public static String randomString(final int size) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < size; i++) {
            int num = random.nextInt(62);
            buffer.append(str.charAt(num));
        }
        return buffer.toString();
    }

    public static String formatSizeShow(long size) {
        String format = null;
        double dB = size / 1024.0;
        if (dB < 1.0) {
            format = String.valueOf(size) + " B";
        } else {
            DecimalFormat df = new DecimalFormat("#.##");
            double dM = dB / 1024.0;
            if (dM < 1.0) {
                format = df.format(dB) + " K";
            } else {
                format = df.format(dM) + " M";
            }
        }
        return format;
    }

    public static int getFileType(String fileName) {
        int fileType = UNDEFINE;
        if (TextUtils.isEmpty(fileName)) {
            return fileType;
        }
        if (fileName.endsWith("exe")) {
            fileType = EXE;
        } else if (fileName.endsWith("txt")) {
            fileType = TXT;
        } else if (fileName.endsWith("doc") || fileName.endsWith("docx")) {
            fileType = WORD;
        } else if (fileName.endsWith("xls") || fileName.endsWith("xlsx")) {
            fileType = EXCEL;
        } else if (fileName.endsWith("ppt") || fileName.endsWith("pptx")) {
            fileType = PPT;
        } else if (fileName.endsWith("zip") || fileName.endsWith("rar")) {
            fileType = ZIP;
        } else if (fileName.endsWith("bmp") || fileName.endsWith("gif") || fileName.endsWith("jpg") || fileName.endsWith("jpeg")
                || fileName.endsWith("jpe") || fileName.endsWith("png") || fileName.endsWith("webp")) {
            fileType = IMAGE;
        } else if (fileName.endsWith("wav") || fileName.endsWith("mp3") || fileName.endsWith("aif") || fileName.endsWith("rm")
                || fileName.endsWith("wmv") || fileName.endsWith("mid") || fileName.endsWith("cda")) {
            fileType = MUSIC;
        }
        return fileType;
    }

    public static File compressBitmapFile(File file) {
        Log.d(TAG, "------ compressBitmapFile() ------>");
        BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
        bitmap_options.inJustDecodeBounds = true;
        bitmap_options.inBitmap = null;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bitmap_options);
        } catch (Exception e) {
            Log.e(TAG, "", e);
            bitmap = null;
            return file;
        }

        bitmap_options.inSampleSize = 0;
        bitmap_options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
        try {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bitmap_options);
        } catch (Exception e) {
            Log.e(TAG, "", e);
            bitmap = null;
            return file;
        }
        Log.d(TAG, "------ Origin bitmap file size: " + file.length() / (1024 * 1024) + "MB");
        FileOutputStream baos = null;
        try {
            baos = new FileOutputStream(file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            return file;
        }

        // 100表示不压缩，0表示压缩到最小
        // 80压缩出来的文件大小在原文件大小的一半左右
        int options = 80;

        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);

        Log.d(TAG, "------ After bitmap file size: " + file.length() / 1024 + "K");
        Log.d(TAG, "<------ compressBitmapFile() ------");
        return file;
    }

    /**
     * 获取临时图片目录
     *
     * @return
     */
    public static File getTempImageDir() {
        File file = new File(getAppDataPath() + IMAGE_TEMP_SENDED_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获得不大于1280X720的图片，等比例缩放
     *
     * @return
     */
    public static Bitmap getSendBitmapFromFile(File file, int targetSize) {
        if (!file.exists())
            return null;
        Bitmap bitmap = getBitmapFromCache(file.getAbsolutePath(), targetSize);
        return resolveImageRotation(bitmap, file.getPath());
    }

    public static Bitmap getBitmapFromCache(String path, int targetSize) {
        try {
            if (!TextUtils.isEmpty(path)) {
//                Object o = get(path);
//                if (o != null && o instanceof Bitmap && !((Bitmap) o).isRecycled()) {
//                    return (Bitmap) o;
//                } else {
                if (isFileExist(path)) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(path, options);
                    options.inSampleSize = computeSampleSize(options, -1, targetSize);
                    options.inJustDecodeBounds = false;
                    Bitmap value = BitmapFactory.decodeFile(path, options);
//                        put(path, value);
                    return value;
                }
//                }
            }
        } catch (Exception e) {
        }
        Log.d(TAG, "not in sdcard and not in cache");
        return null;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static Bitmap resolveImageRotation(Bitmap originalBitmap, String path) {
        if (null == originalBitmap) {
            return null;
        }
        int imageRotation = getImageRotation(path);
        if (0 < imageRotation) {
            Matrix matrix = new Matrix();
            matrix.setRotate(imageRotation);
//			if(!ImageMgr.getInstance().containsBitmap(originalBitmap)){
            if (!originalBitmap.isRecycled()) {
                Bitmap bitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
                        originalBitmap.getWidth(), originalBitmap.getHeight(),
                        matrix, true);
//                originalBitmap.recycle();
                return bitmap;
            }
//			}
//			ImageMgr.getInstance().removeRecycledBitmap(path);
        }
        return originalBitmap;
    }

    public static int getImageRotation(String path) {
        int orientation = getImageOrientation(path);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            default:
                return 0;
        }
    }

    public static int getImageOrientation(String path) {
        try {
            ExifInterface exif = new ExifInterface(path);
            return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ExifInterface.ORIENTATION_UNDEFINED;
    }

    public static void save(String filename, Bitmap bitmap) throws IOException {
        if (bitmap == null || TextUtils.isEmpty(filename)) {
            return;
        }
        File f = new File(filename);
        if (f.exists()) {
            f.delete();
        }
        if (!f.exists()) {
            f.createNewFile();
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
            }
            if (filename.endsWith("png")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, fOut);// 低网速时，大图片传不上去，所以将100降为50
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            }
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
            }
//            if (!bitmap.isRecycled()) {
//                bitmap.recycle();
//            }
        }
    }

    public static void delete(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}
