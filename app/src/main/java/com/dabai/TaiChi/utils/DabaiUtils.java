package com.dabai.TaiChi.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class DabaiUtils {

    /**
     开发者 DABAI2017
     创建日期 2019.5.9
     创建类型 工具类
     **/


    private String formetFileSize(File file){
        String fileSize = "0B";
        if (file == null){
            fileSize = "文件不存在";
            return fileSize;
        }
        long fileLength = file.length();
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileLength < 1024){
            fileSize = df.format((double) fileLength) + "B";
        }else if (fileLength < 1048576){
            fileSize = df.format((double) fileLength / 1024) + "KB";
        }else if (fileLength < 1073741824){
            fileSize = df.format((double) fileLength / 1048576) + "MB";
        }else{
            fileSize = df.format((double) fileLength / 1073741824) + "GB";
        }
        return fileSize;
    }



    /**
     * 打开app
     */

public void openApp(Context context,String Packname){
    Intent resolveIntent = context.getPackageManager().getLaunchIntentForPackage(Packname);
    context.startActivity(resolveIntent);
}


//常见权限  要写在清单文件才能动态调用

    /**
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     * <uses-permission android:name="android.permission.INTERNET"/>
     * <uses-permission android:name="android.permission.WRITE_SETTINGS"/>
     **/


//检查权限
    public boolean isPermissionChecked(Context context, String mani) {

        //检查代码是否拥有这个权限
        int checkResult = context.checkCallingOrSelfPermission(mani);
        //if(!=允许),抛出异常
        if (checkResult != PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 打开链接
     *
     * @param link
     */
    public void openLink(Context context, String link) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(link));
            context.startActivity(intent);

    }


    /**
     * 将bitmap中的某种颜色值替换成新的颜色
     * @param oldColor
     * @param newColor
     * @return Bitmap
     */
    public static Bitmap replaceBitmapColor(Bitmap oldBitmap, int oldColor, int newColor)
    {
        //相关说明可参考 http://xys289187120.blog.51cto.com/3361352/657590/
        Bitmap mBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);
        //循环获得bitmap所有像素点
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        int mArrayColorLengh = mBitmapWidth * mBitmapHeight;
        int[] mArrayColor = new int[mArrayColorLengh];
        int count = 0;
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                //获得Bitmap 图片中每一个点的color颜色值
                //将需要填充的颜色值如果不是
                //在这说明一下 如果color 是全透明 或者全黑 返回值为 0
                //getPixel()不带透明通道 getPixel32()才带透明部分 所以全透明是0x00000000
                //而不透明黑色是0xFF000000 如果不计算透明部分就都是0了
                int color = mBitmap.getPixel(j, i);
                //将颜色值存在一个数组中 方便后面修改
                if (color == oldColor) {
                    mBitmap.setPixel(j, i, newColor);  //将白色替换成透明色
                }

            }
        }
        return mBitmap;
    }


    /**
     * 获取随机颜色值
     * @return
     */
    public static String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;

        return "#"+r + g + b;
    }

    //检查字符串包含中文

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }


    //length用户要求产生字符串的长度
    public String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyz0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(36);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }



    //发送文本
    public void sendText(Context context,String p0) {

        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            // 指定发送内容的类型
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, p0);
            sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(Intent.createChooser(sendIntent, "Share"));
        } catch (Exception e) {
            Toast.makeText(context, "调用分享组件失败!"+e, Toast.LENGTH_SHORT).show();
            Log.d("dabaizzz", e+"");
        }


    }


    /**
     * 解压缩功能.
     * 将zipFile文件解压到folderPath目录下.
     *
     */
    public int unzip_DirFile(File zipFile, String folderPath) throws ZipException, IOException {
        //public static void upZipFile() throws Exception{
        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                Log.d("upZipFile", "ze.getName() = " + ze.getName());
                String dirstr = folderPath + ze.getName();
                //dirstr.trim();
                dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                Log.d("upZipFile", "str = " + dirstr);
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            Log.d("upZipFile", "ze.getName() = " + ze.getName());
            OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen = 0;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zfile.close();
        Log.d("upZipFile", "finishssssssssssssssssssss");
        return 0;
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir     指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        String substr = null;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                try {
                    //substr.trim();
                    substr = new String(substr.getBytes("8859_1"), "GB2312");

                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ret = new File(ret, substr);

            }
            Log.d("upZipFile", "1ret = " + ret);
            if (!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length - 1];
            try {
                //substr.trim();
                substr = new String(substr.getBytes("8859_1"), "GB2312");
                Log.d("upZipFile", "substr = " + substr);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            ret = new File(ret, substr);
            Log.d("upZipFile", "2ret = " + ret);
            return ret;
        }
        return ret;
    }


    //解压压缩包内所有文件到指定目录。  不支持多分级目录
    public void unzip_oneFile(String zipPtath, String outputDirectory) throws IOException {
        /**
         * 解压assets的zip压缩文件到指定目录
         * @param context上下文对象
         * @param assetName压缩文件名
         * @param outputDirectory输出目录
         * @param isReWrite是否覆盖
         * @throws IOException
         */

        // 创建解压目标目录
        File file = new File(outputDirectory);
        // 如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        // 打开压缩文件
        InputStream inputStream = new FileInputStream(zipPtath);
        ;
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        // 读取一个进入点
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        // 使用1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        // 解压时字节计数
        int count = 0;
        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            //Log.i(TAG,"解压文件 入口 1： " +zipEntry );
            if (!zipEntry.isDirectory()) {  //如果是一个文件
                // 如果是文件
                String fileName = zipEntry.getName();
                //   Log.i(TAG,"解压文件 原来 文件的位置： " + fileName);
                fileName = fileName.substring(fileName.lastIndexOf("/") + 1);  //截取文件的名字 去掉原文件夹名字
                //   Log.i(TAG,"解压文件 的名字： " + fileName);
                file = new File(outputDirectory + File.separator + fileName);  //放到新的解压的文件路径

                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while ((count = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, count);
                }
                fileOutputStream.close();

            }

            // 定位到下一个文件入口
            zipEntry = zipInputStream.getNextEntry();
            //   Log.i(TAG,"解压文件 入口 2： " + zipEntry );
        }
        zipInputStream.close();
        //   Log.i(TAG,"解压完成");

    }


    //打开文件选择器
    public void openFileChoose(Activity c, int i) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        c.startActivityForResult(intent, i);

		/*
		 活动回调  要判断回调码
		 @Override
		 protected void onActivityResult(int requestCode, int resultCode, Intent data)
		 {
		 if (resultCode == Activity.RESULT_OK)
		 {
		 Uri uri = data.getData();
		 if (requestCode == 5)
		 {
		 new DabaiUtils(). toast(getApplicationContext(),new DabaiUtils().UritoPath(getApplicationContext(),uri));
		 }
		 }
		 }
		 */


    }


    //flie：要删除的文件夹&文件的所在位置
    public void deleteDir(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteDir(f);
            }

            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    public void toast(Context context, Object text) {
        Toast.makeText(context, text + "", Toast.LENGTH_SHORT).show();
    }


    public String getVersionName(Context c) {
        PackageInfo pInfo = null;
        try {
            pInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
            return pInfo.versionName;      ///或者 pInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package version name: " + e);
        }
    }


    /**
     * 复制文件夹
     */
    public static boolean copyDirectory(File src, File dest) {
        if (!src.isDirectory()) {
            return false;
        }
        if (!dest.isDirectory() && !dest.mkdirs()) {
            return false;
        }

        File[] files = src.listFiles();
        for (File file : files) {
            File destFile = new File(dest, file.getName());
            if (file.isFile()) {
                if (!copyFile(file, destFile)) {
                    return false;
                }
            } else if (file.isDirectory()) {
                if (!copyDirectory(file, destFile)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 复制文件
     */
    public static boolean copyFile(File src, File des) {
        if (!src.exists()) {
            Log.e("cppyFile", "file not exist:" + src.getAbsolutePath());
            return false;
        }
        if (!des.getParentFile().isDirectory() && !des.getParentFile().mkdirs()) {
            Log.e("cppyFile", "mkdir failed:" + des.getParent());
            return false;
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(src));
            bos = new BufferedOutputStream(new FileOutputStream(des));
            byte[] buffer = new byte[4 * 1024];
            int count;
            while ((count = bis.read(buffer, 0, buffer.length)) != -1) {
                if (count > 0) {
                    bos.write(buffer, 0, count);
                }
            }
            bos.flush();
            return true;
        } catch (Exception e) {
            Log.e("copyFile", "exception:", e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();

        }

    }


    //读取文本
    public String readSDFile(String fileName) throws IOException {

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        int length = fis.available();
        byte[] buffer = new byte[length];
        fis.read(buffer);
        String res = new String(buffer, "UTF-8");
        fis.close();
        return res;
    }


    //分享文件
    void shareFile(Context c, String path) {
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setType("*/*");
        imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(String.format("file://%s", path)));
        c.startActivity(Intent.createChooser(imageIntent, "分享"));
    }


    //uri  转  path
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String UritoPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
//                Log.i(TAG,"isExternalStorageDocument***"+uri.toString());
//                Log.i(TAG,"docId***"+docId);
//                以下是打印示例：
//                isExternalStorageDocument***content://com.android.externalstorage.documents/document/primary%3ATset%2FROC2018421103253.wav
//                docId***primary:Test/ROC2018421103253.wav
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
//                Log.i(TAG,"isDownloadsDocument***"+uri.toString());
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
//                Log.i(TAG,"isMediaDocument***"+uri.toString());
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"content***"+uri.toString());
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"file***"+uri.toString());
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    public boolean checkApkExist(Context context, String packageName){
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public void admDownload(Context context,String link){
        Intent intent = new Intent();
        intent.setData(Uri.parse(link));
        intent.setClassName("com.dv.adm.pay","com.dv.get.AEditor");
        context.startActivity(intent);
    }

}