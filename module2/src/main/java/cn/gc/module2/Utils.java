package cn.gc.module2;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.*;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.telephony.TelephonyManager;
import android.text.*;
import android.text.format.Formatter;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URI;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Utils {

    public static void leadUserToRatingInMarket(Context context) {

        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityInfo info = intent.resolveActivityInfo(context.getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY);
        if (info != null) {
            context.startActivity(intent);
        } else {
        }
    }

    public static boolean isMobileNO(String mobiles) {
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|17[678]|(18[0-9]))\\d{8}$");
        // 出现170号段，现在只规则是：首位为1 ，共11位的号码。2016-07-11 fix GC
        Pattern p = Pattern.compile("^(1)\\d{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNum(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）
		总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		*/
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 昵称校验规则
     * 昵称仅支持中文、英文、数字和·
     *
     * @param nickname
     * @return
     */
    public static boolean nicknameLimit(String nickname) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5\\u00b7\\dA-Za-z]{1,10}");
        Matcher m = p.matcher(nickname);
        return m.matches();
    }

    /**
     * 截取当前屏幕
     */
//	    public void GetandSaveCurrentImage(Context context) {
//		    WindowManager windowManager = getWindowManager();
////		    WindowManager mWm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
//		    Display display = windowManager.getDefaultDisplay();
//		    int w = display.getWidth();
//		    int h = display.getHeight();
//		    Bitmap Bmp = Bitmap.createBitmap( w, h, Config.ARGB_8888 );
//		    View decorview = this.getWindow().getDecorView();
//		    decorview.setDrawingCacheEnabled(true);
//		    Bmp = decorview.getDrawingCache();
//		    String SavePath = getSDCardPath()+"/AndyDemo/ScreenImage";
//		    try {
//		        File path = new File(SavePath);
//		        String filepath = SavePath + "/Screen_1.jpg";
//		        File file = new File(filepath);
//		        if(!path.exists()){
//		            path.mkdirs();
//		        }
//		        if (!file.exists()) {
//		            file.createNewFile();
//		        }
//
//		        FileOutputStream fos = null;
//		        fos = new FileOutputStream(file);
//		        if (null != fos) {
//		            Bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
//		            fos.flush();
//		            fos.close();
//		        }
//		    } catch (Exception e) {
//		        e.printStackTrace();
//		    }
//		}
    public static String getSystemVersion() {
        return Build.VERSION.SDK;
    }


    //获取SDK路径
    public static String getSDCardPath() {
        File sdcardDir = null;
        boolean sdcardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }


    public static int[] getScreenWH(Context context) {
        if (context == null) return new int[]{0, 0};
        DisplayMetrics metrics = getDisplayMetrics(context);
        if (metrics == null)
            return new int[]{0, 0};
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    /**
     * dp值转化为px
     *
     * @param context
     * @param dp
     * @return
     */
    public static float dip2px(Context context, float dp) {
        if (context == null) return 0;
        DisplayMetrics metrics = getDisplayMetrics(context);
        if (metrics == null)
            return 0;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    /**
     * px值转化为dip
     *
     * @param context
     * @param px
     * @return
     */
    public static float px2dip(Context context, float px) {
        if (context == null) return 0;
        DisplayMetrics metrics = getDisplayMetrics(context);
        if (metrics == null)
            return 0;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, metrics);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int pxTodip(Context context, float pxValue) {
        if (context == null) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp 转 px
     *
     * @param context
     * @param sp
     * @return
     */
    public static float sp2px(Context context, float sp) {
        if (context == null) return 0;
        DisplayMetrics metrics = getDisplayMetrics(context);
        if (metrics == null)
            return 0;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null || wm.getDefaultDisplay() == null)
            return null;
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics;
    }

    public static boolean isHoneycomb() {
        return Build.VERSION.SDK_INT >= 11;
    }

    public String getImei(Context context) {
        String imei;
        TelephonyManager telephonymanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return null;
    }

    private String replaceImei(Context context) {
        String data;
        try {
            data = getRandom(context);
        } catch (Exception e) {
            data = createRandomNumber();
        } catch (Error e) {
            data = createRandomNumber();
        }
        return data;
    }

    private String createRandomNumber() {
        StringBuffer sb = new StringBuffer();
        String base = "0123456789";
        Random random = new Random();
        for (int i = 0; i < 13; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        addTestNumber(sb);
        sb.insert(0, "sz_");
        return sb.toString();
    }

    private String addTestNumber(StringBuffer sb) {
        int sum = 0;
        int product = 1;
        String data = sb.toString();
        for (int i = 0; i < data.length(); i++) {
            char x = data.charAt(i);
            // 判断X是否为数字
            if (Character.isDigit(x)) {
                // x类型转换然后统计
                int a = Integer.parseInt(String.valueOf(x));
                if (a != 0) {
                    product = product * a;
                }
                sum += a;
            }
        }
        int blast = (product % 10);
        String newproduct = String.valueOf(Math.abs(blast));
        sb.append(newproduct);// 倒数第二位是积
        int last = (sum % 10);
        String newsum = String.valueOf(Math.abs(last));
        sb.append(newsum);// 最后一位是和
        return sb.toString();
    }

    private String getRandom(Context context) {
        if (getExternalStorageRootPath() == null) {
            return createRandomNumber();
        }
        String random = null;
        File file = new File("//sdcard//yzgkj");
        if (!file.exists()) {
            file.mkdirs();
            random = createRandomNumber();
            saveRandom(context, random);
            return random;
        } else {
            try {
                File dirPath = new File(file, "shangde.yzgkj");
                if (!dirPath.exists()) {
                    dirPath.createNewFile();
                    random = createRandomNumber();
                    saveRandom(context, random);
                    return random;
                } else {
                    // 文件中有的话就直接取出来
                    FileReader fr;
                    fr = new FileReader(dirPath);
                    BufferedReader in = new BufferedReader(fr);
                    random = validate(in.readLine());
                    if (random == null) {
                        random = createRandomNumber();
                        saveRandom(context, random);
                    }
                    return random;
                }
            } catch (Exception e) {
                random = createRandomNumber();
                saveRandom(context, random);
                return random;
            }
        }
    }

    private String validate(String result) {
        if (result == null) {
            return null;
        }
        if (!result.startsWith("sz_")) {
            return null;
        }
        if (result.length() != 18) {
            return null;
        }
        int sum = 0;
        int product = 1;
        String initNum = result.substring(3, result.length() - 2);// 数字的前十三位(除去sz)
        String getProduct = result.substring(result.length() - 2, result.length() - 1); // 倒数第二位(积)
        int PD = Integer.parseInt(getProduct);
        String getSum = result.substring(result.length() - 1, result.length());// 最后一位(和)
        int SUM = Integer.parseInt(getSum);
        for (int i = 0; i < initNum.length(); i++) {
            char x = initNum.charAt(i);
            // 判断X是否为数字
            if (Character.isDigit(x)) {
                // x类型转换然后统计
                int a = Integer.parseInt(String.valueOf(x));
                if (a != 0) {
                    product = product * a;
                }
                sum += a;
            }
        }
        int blast = Math.abs(product % 10);
        int last = Math.abs(sum % 10);
        if (PD == blast && SUM == last) {
            return result;
        } else {
            return null;
        }
    }

    private void saveRandom(Context context, String random) {
        try {
            File file = new File("//sdcard//yzgkj");
            if (!file.exists()) {
                file.mkdirs();
            }
            File dirPath = new File(file, "shangde.yzgkj");
            if (!dirPath.exists()) {
                dirPath.createNewFile();
            } else {
                dirPath.delete();
                dirPath.createNewFile();
            }
            BufferedWriter writer;
            writer = new BufferedWriter(new FileWriter(dirPath));
            writer.write(random);
            writer.close();
        } catch (Throwable ex) {
        }
    }

    public String getExternalStorageRootPath() {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String root = Environment.getExternalStorageDirectory().getPath();
                File sdcard = new File("/sdcard");
                if (sdcard.exists() && sdcard.list().length == 0) {
                    return null;
                }
                return root;
            }
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 隐藏软键盘
     *
     * @param context
     */
    public static void hideSoftKeyBoard(Context context) {

        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())  //一直是true
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideSoftKeyBoard(Activity act, int id) {
        InputMethodManager inputMethodManager = (InputMethodManager) act.getApplicationContext().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editText = (EditText) act.findViewById(id);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0); //隐藏
    }

    /**
     * 隐藏软键盘
     *
     * @param context 上下文
     * @param view    可以当前布局中已经存在的任何一个View
     */
    public static void hideSoftKeyBoard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            if (inputMethodManager != null && view != null && inputMethodManager.isActive())
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0); //隐藏
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Bitmap getBitmap(Activity act, int img_id) {
        //获取图片信息
        BitmapFactory.Options opts = new BitmapFactory.Options();//解析位图的附加条件
        opts.inJustDecodeBounds = false;//真正解析位图
//      opts.inPreferredConfig = Bitmap.Config.ARGB_4444;

        //把图片的解析条件opts在创建的时候带上
        return BitmapFactory.decodeResource(act.getResources(), img_id, opts);
    }

    public static int getBarHeight(Activity act) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;//默认为38，貌似大部分是这样的

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = act.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    public static void commonToast(final Activity act, final String str) {
    }

    public static void showCommonErrorToast(Activity act, View view) {
        commonToast(act, "获取数据失败");
    }

    public static void showCommonErrorToast(Activity act, int status) {
        switch (status) {
            case 0107:
                commonToast(act, "用户账号或密码错误");
                break;
            case -1:
                commonToast(act, "连接服务器失败");
                break;

            case -2:
                commonToast(act, "提交数据错误");
                break;

            case -3:
                commonToast(act, "用户名不存在");
                break;

            case -4:
                commonToast(act, "密码不匹配");
                break;

            case -5:
                commonToast(act, "用户信息已存在");
                break;

            case -6:
                commonToast(act, "昵称和性别都为空");
                break;

            case -7:
                commonToast(act, "连接服务器失败");
                break;

            case -99999:
                commonToast(act, "网络解析异常");
                Log.e("duoduo", "showCommonErrorToast status:-99999 发送时Json解析异常");
                break;

            case -99998:
                commonToast(act, "网络解析异常");
                Log.e("duoduo", "showCommonErrorToast status:-99998 接收时Json解析异常");
                break;

            case -99997:
                commonToast(act, "网络连接异常");
                Log.e("duoduo", "showCommonErrorToast status:-99997 网络连接异常");
                break;
        }
    }


    // 过滤特殊字符
    public static String StringFilter(String str) {
        // 只允许字母和数字
        // String   regEx  =  "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    // 过滤特殊字符
    public static boolean haveIlegalCharacter(String str) {
        return str.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0;
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            return sdDir.toString() + "/";
        } else {
            return null;
        }
    }


    public static File[] getSDPathList() {
        File sdDir = null;
        String filePathName = null;
        File mfile;
        File[] files;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            filePathName = sdDir.toString() + "/sunland";
            mfile = new File(filePathName);
            files = mfile.listFiles();
            return files;
        } else {
            return null;
        }
    }


    public static long size = 0;

    public static String getFileName(File[] files, String name) {
        String fileNm;
        String fileSize = null;
        FileInputStream fileInputStream;
        if (files != null) {
            for (File file : files) {
//                if (file.isDirectory()){//如果是文件夹继续查
//                    getFileName(file.listFiles());
//                }else {
                //                }
                String fileName = file.getName();
//                fileNm = fileName.substring(0,fileName.lastIndexOf(".")).toString();
                if (name.equals(fileName)) {
                    try {
                        fileInputStream = new FileInputStream(file);
                        size = fileInputStream.available();
                        fileSize = getSize(size);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return fileSize;
    }


    public static boolean getFileEsxist(String name) {
        boolean Esxist = false;
        File sdDir = null;
        String filePathName = null;
        File mfile;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            filePathName = sdDir.toString() + "/sunland" + "/" + name;
            mfile = new File(filePathName);
            if (mfile.exists()) {
                Esxist = true;
            }
        }
        return Esxist;
    }


    public static String getSize(Long size) {
        double result;
        String unit;

        if (size > (1 << 30)) {
            result = div(size, 1 << 30, 2);
            unit = "GB";
        } else if (size > (1 << 20)) {
            result = div(size, 1 << 20, 2);
            unit = "MB";
        } else {
            result = div(size, 1 << 10, 2);
            unit = "KB";
        }
        return result + unit;
    }

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }

        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Intent getOpenFileIntent(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();

        /* 依扩展名的类型决定MimeType */
//        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
//                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
//            return getAudioFileIntent(filePath);
//        }

//        if (end.equals("3gp") || end.equals("mp4")) {
//            return getAudioFileIntent(filePath);
//        }

        if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        }

//        if (end.equals("apk")) {
//            return getApkFileIntent(filePath);
//        }

        if (end.equals("ppt") || end.equals("pptx")) {
            return getPptFileIntent(filePath);
        }

        if (end.equals("xls") || end.equals("xlsx")) {
            return getExcelFileIntent(filePath);
        }

        if (end.equals("doc") || end.equals("docx")) {
            return getWordFileIntent(filePath);
        }

        if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        }

//        if (end.equals("chm")) {
//            return getChmFileIntent(filePath);
//        }

        if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        }

        if (end.equals("mp3")) {
            return getAudioFileIntent(filePath);
        }
        if (end.equals("mp4") || end.equals("wmv")) {
            return getVideoFileIntent(filePath);
        }

        if (end.equals("zip") || end.equals("rar")) {
            return getZipOrRarIntent(filePath);
        }


        return getAllIntent(filePath);
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(String param) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(param);
        intent.setDataAndType(Uri.fromFile(file), "video/*");
        return intent;
    }

    //补课模式获取SDk地址
    public static String getMakeUpPdfPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            return sdDir.getAbsolutePath().toString() + "/";
        } else {
            return null;
        }
    }


    //Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }


    //Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }


    //Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");

        return intent;
    }

    //android 获取一个用于打开ZIP或者RAR的intent
    public static Intent getZipOrRarIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-zip-compressed");

        return intent;
    }


    //Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    //Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    public static long getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                if (children == null || children.length == 0) {
                    return 0;
                }
                long size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                return file.length();
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0L;
        }
    }

    /**
     * 是否是debug模式(如果是release版本apk，debug为false。如果是用eclipse直接运行，debug为true)
     * 前提条件是不能  AndroidManifest.xml 的application 标 设  debugable 属性
     *
     * @param context
     * @return
     * @author SHANHY
     * @date 2015-8-7
     */
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (; ; ) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }


    public static String millsecondsToStr(long seconds) {
        seconds = seconds / 1000;
        String result = "";
        long hour = 0, min = 0, second = 0;//
        hour = seconds / 3600;
        min = seconds / 60 - (hour * 60);
        second = seconds - (seconds / 60) * 60;
        if (hour < 10) {
            result += "0" + hour + ":";
        } else {
            result += hour + ":";
        }
        if (min < 10) {
            result += "0" + min + ":";
        } else {
            result += min + ":";
        }
        if (second < 10) {
            result += "0" + second;
        } else {
            result += second;
        }
        return result;
    }

    public static String millsecondsToStrForOnBackDialog(long seconds) {
        seconds = seconds / 1000;
        String result = "";
        int min = 0, second = 0;
        min = (int) (seconds / 60);
        second = (int) (seconds - (seconds / 60) * 60);
        if (min < 10) {
            result += "0" + min + ":";
        } else {
            result += min + ":";
        }
        if (second < 10) {
            result += "0" + second;
        } else {
            result += second;
        }

        return result;
    }

    public static String millSecondsToStrForRanking(long seconds) {
        String result = "";
        if (seconds == 0)
            return "0分";
        long hour = 0, min = 0;
        hour = seconds / 3600;
        min = seconds / 60 - (hour * 60);
        if (hour < 10) {
            result += "0" + hour + "小时";
        } else {
            result += hour + "小时";
        }
        if (min < 10) {
            result += "0" + min + "分";
        } else {
            result += min + "分";
        }

        return result;
    }


    public static String millsecondsToStrForMakeUp(long seconds) {
        seconds = seconds / 1000;
        String result = "3";
        int min = 0;
        min = (int) (seconds / 60);
        result = min + "";
        return result;
    }


    public static String s2String(int seconds) {
        int hour = seconds / 3600;
        int minute = seconds % 3600 / 60;
        int second = seconds % 60;
        String result = "";
        if (hour < 10) {
            result += "0" + hour + ":";
        } else {
            result += hour + ":";
        }
        if (minute < 10) {
            result += "0" + minute + ":";
        } else {
            result += minute + ":";
        }
        if (second < 10) {
            result += "0" + second;
        } else {
            result += second;
        }
        return result;
    }


    /**
     * 错题数量显示格式文本
     */
    public static SpannableString getWrongText(int count) {
        String wrongText = "共" + count + "题";
        SpannableString span = new SpannableString(wrongText);
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#ce0000"));
        span.setSpan(fcs, 1, span.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    /**
     * 题目详情显示题目类型格式文本
     *
     * @param type
     * @param title
     */
    public static SpannableString getQuestionTitleText(String type, String title) {
        String content = type + " " + title;
        SpannableString span = new SpannableString(content);
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#dc4c4c"));
        span.setSpan(fcs, 0, type.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    /**
     * 材料，答案，考点，解析，关键字显示内容格式文本
     *
     * @param header
     * @param content
     * @return
     */
    public static SpannableString getAnswerDetailText(String header, String content) {
        String detail = header + " " + content;
        SpannableString span = new SpannableString(detail);
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#323232"));
        span.setSpan(fcs, 0, header.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }


    /**
     * 判断当前是否有  连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        return false;
    }

    /**
     * 返回当前时间
     */
    public static long getNewTime(String beginTime) {
        if (TextUtils.isEmpty(beginTime)) return 0;
        long timeStart = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            timeStart = formatter.parse(beginTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStart;
    }

    /**
     * 返回对应的时间
     */
    public static String getTime(long time) {
        if (time == 0) return "";
        Date date = new Date(time);
        String sDateTime = dateToString(date, "yyyy-MM-dd");
        return sDateTime;
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    public void testStringFilter() throws PatternSyntaxException {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        System.out.println(regEx);
        System.out.println(StringFilter(regEx));
    }

    public int getVersionCode(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi.versionCode;
    }

    public static float getScreenWidthDp(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = activity.getResources().getDisplayMetrics().density;
        /*float dpHeight = outMetrics.heightPixels / density;*/
        return outMetrics.widthPixels / density;
    }

    /**
     * APP渠道读取


    public static boolean getWifior4G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            ToastManager.showText(context, "您现在处于非WIFI网络环境，建议到WIFI环境观看视频和下载视频");
            return true;
        }

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
            ToastManager.showText(context, "您现在处于WIFI网络环境");
            return false;
        }
        ToastManager.showText(context, "您现在处于非WIFI网络环境，建议到WIFI环境观看视频和下载视频");
        return true;
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


    public static Bitmap getThumbBitmap(Bitmap sourceBitmap, int size) {
        if (sourceBitmap == null || sourceBitmap.isRecycled()) return null;

        if (sourceBitmap.getByteCount() <= size) {
            return sourceBitmap;
        }

        int zoomRation = 2;
        Bitmap destBitmap = Bitmap.createScaledBitmap(
                sourceBitmap,
                sourceBitmap.getWidth() / zoomRation,
                sourceBitmap.getHeight() / zoomRation,
                true);
        while (destBitmap.getByteCount() > size) {
            if (sourceBitmap.isRecycled()) break;
            zoomRation++;
            destBitmap = Bitmap.createScaledBitmap(
                    sourceBitmap,
                    sourceBitmap.getWidth() / zoomRation,
                    sourceBitmap.getHeight() / zoomRation,
                    true);
        }

        return destBitmap;
    }

    public static String getAppVersionName(Context context) {
        return SunAppInstance.getInstance().getVersionName();
    }

    public static String getAppVersionName() {
        return SunAppInstance.getInstance().getVersionName();
    }

    public static String getOsVersion() {
        return "Android-" + Build.VERSION.SDK_INT;
    }

    /**
     * 获取应用当前支持的最小SDK版本
     *
     * @return 版本code
     */

    /**
     * 把毫秒转换为 天/时/分/秒
     *
     * @param time 毫秒
     * @return
     */
    public static long[] getDealTime(long time) {
        long[] times = new long[4];
        long days = time / (1000 * 60 * 60 * 24);
        long hours = (time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (time % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (time % (1000 * 60)) / 1000;
        times[0] = days;
        times[1] = hours;
        times[2] = minutes;
        times[3] = seconds;
        return times;
    }

    /**
     * 获取小时数
     *
     * @param time 毫秒
     */
    public static long getHours(long time) {
        long[] times = getDealTime(time);
        long days = times[0];
        long hours = times[1];
        hours += days * 24;
        return hours;
    }

    /**
     * 获取分钟数
     *
     * @param time 毫秒
     */
    public static long getMinutes(long time) {
        long[] times = getDealTime(time);
        return times[2];
    }


    public static String getPauseTime(long time) {

        return (time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60) + ":" + (time % (1000 * 60 * 60)) / (1000 * 60) + ":" + (time % (1000 * 60)) / 1000;
    }

    /**
     * 模考分数显示规则
     * 如果返回的分数小数点后为0，转化为整数显示
     * 否则保留小数点后一位，四舍五入
     *
     * @param examScore
     * @return
     */
    public static String getExamScore(double examScore) {
        int score = (int) examScore;
        if (score == examScore)
            return String.valueOf(score);

        return String.format("%.1f", examScore);
    }

    /**
     * 关注/粉丝/点赞等数量显示规则
     * 最多显示4位数9999,超过9999后,取万级数量显示，小数点保留到千位。示例：
     * 11000显示1.1w+,24999显示2.4w+,641415显示64.1w+
     *
     * @param count
     * @return
     */
    public static String getFormatNumber(int count) {
        if (count > 9999) {
            double num = count / 10000.0 - 0.05;
            String formatNum = String.format("%.1f", num);
            return formatNum + "w";
        } else {
            return String.valueOf(count);
        }
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        if (TextUtils.isEmpty(str)) return false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 格式化物流编号、课程编号
     */
    public static String getDealNumber(String number) {
        if (TextUtils.isEmpty(number)) return "";
        String dealNumber = number.replaceAll("\\S{4}(?!$)", "$0 ");
        return dealNumber;
    }

    /**
     * 对指定字符串过滤掉规定的字符
     *
     * @param origin 需要被过滤掉字符的串
     * @param params 过滤某些指定的字符
     * @return 过滤完成的串
     */
    public static String filterString(String origin, String... params) {
        if (TextUtils.isEmpty(origin)) {
            return "";
        }
        for (String param : params) {
            origin = origin.replaceAll(param, "");
        }
        return origin;
    }

    /**
     * 保存学院id和学院名称
     *
     * @param context
     * @param resultJson
     * @param position
     */
    public static void saveCollegeInfo(Context context, JSONArray resultJson, int position) {
        if (resultJson == null || resultJson.length() < 1) return;
        try {
            JSONObject object = resultJson.getJSONObject(position);
            int collegeId = object.getInt("collegeId");
            String collegeName = object.getString("collegeName");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * md5加密
     *
     * @param input
     * @return
     */
    public final static String stringMD5(String input) {

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = input.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }


    public static String creatSign(String roomId, String userName, String userNumber, String userRole, String key) {
        String keystr = "room_id=" + roomId + "&" + "user_name=" + userName + "&" + "user_number=" + userNumber + "&" + "user_role=" + userRole + "&" + "partner_key=" + key;
        return stringMD5(keystr);
    }

    /**
     * 获取展示互动 本地存放log地址
     *
     * @return
     */
    public static File[] getGenseeLog() {
        File sdDir = null;
        String filePathName = null;
        File mfile;
        File[] files;
        File[] files1 = new File[5];
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            filePathName = sdDir.toString() + "/gensee/log/";
            mfile = new File(filePathName);
            files = mfile.listFiles();
            if (null == files) return null;
            if (files.length > 5) {
                System.arraycopy(files, 0, files1, 0, 5);
                return files1;
            }
            return files;
        } else {
            return null;
        }
    }

    public static JSONArray getFileNameForGensee(File[] files) {
        JSONArray jsonArray = new JSONArray();
        if (files == null || files.length < 1) return jsonArray;

        for (int i = 0; i < files.length; i++) {
            jsonArray.put("android - " + files[i].getName());
        }
        return jsonArray;
    }


    public static String float2String(String str) {
        try {
            float f = Float.parseFloat(str);
            return String.valueOf((int) f);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static int float2Int(String str) {
        float f = Float.parseFloat(str);
        return (int) f;
    }

    /**
     * 获取屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = getDisplayMetrics(context);
        if (metrics == null)
            return 0;
        return metrics.widthPixels;
    }

    /**
     * 获取屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = getDisplayMetrics(context);
        if (dm == null)
            return 0;
        return dm.heightPixels;
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /**
     * 检测当的网络状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        return false;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        if (context == null) return 0;
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    //图片缩放比例, 比例越小, 模糊效果越明显
    private static final float BITMAP_SCALE = 0.5f;

    /**
     * 图片高斯模糊
     *
     * @param blurRadius 模糊程度,范围:[1f,25f]
     */
    public static Bitmap blurBitmap(Context context, Bitmap image, float blurRadius) {
        // 计算图片缩小后的长宽
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        // 将缩小后的图片做为预渲染的图片
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurRadius);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            case Calendar.FEBRUARY:
                return (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) ? 29 : 28;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    /**
     * 给H5拼接带参数的Uri
     *
     * @param uri         以#+fragment结尾的Uri,例如：http://172.16.117.225:8000/community-pc-war/m/index.html#notice/6
     * @param appendQuery 需要拼接的参数，格式 userId=12312
     * @return 结果Uri
     */
    public static String generateH5Uri(String uri, String... appendQuery) {
        URI oldUri;
        try {
            oldUri = new URI(uri);
            String newQuery = oldUri.getQuery();
            if (newQuery == null) {
                for (int i = 0; i < appendQuery.length; i++) {
                    if (i == 0) {
                        newQuery = appendQuery[i];
                    } else {
                        newQuery += "&" + appendQuery[i];
                    }
                }
            } else {
                for (String aq : appendQuery) {
                    newQuery += "&" + aq;
                }
            }

            Class clazz = Class.forName("java.net.URI");
            Field queryField = clazz.getDeclaredField("query");
            queryField.setAccessible(true);
            queryField.set(oldUri, newQuery);
            Field stringField = clazz.getDeclaredField("string");
            stringField.setAccessible(true);
            /* 把URI中的string置空，强制URI重新生成string */
            stringField.set(oldUri, null);
            return oldUri.toString();

            /**
             * 当URI中的fragment包含中文字符时，下面这种拼接方法会有编码问题
             */

//            URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(),
//                    oldUri.getPath(), newQuery, oldUri.getFragment());
//            URI newUri1 = new URI(oldUri.getScheme(), oldUri.getAuthority(),
//                    oldUri.getPath(), newQuery, fragment);
//            return newUri.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * 统一拼接一些公共参数
     * userId，appVersion，channelCode
     *
     * @param context 取userId
     * @return url
     * <p>
     * 请使用URI 拼接 queryParam 的方式
     */

    /**
     * 移除下划线
     *
     * @param context
     * @param p_Text
     */
    public static void removeUnderlines(Context context, Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);

        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            p_Text.setSpan(span, start, end, 0);
        }
    }

    /**
     * 移除下划线并且无点击
     *
     * @param context
     * @param p_Text
     */
    public static void removeUnderlinesNoClick(Context context, Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);

        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            p_Text.setSpan(span, start, end, 0);
        }
    }

    /**
     * 判断点击位置是否属于view范围
     *
     * @param view
     * @param event
     * @return
     */
    public static boolean isTouchInViewArea(View view, MotionEvent event) {
        if (view == null || event == null)
            return false;

        if (view.getVisibility() != View.VISIBLE)
            return false;

        float eventX = event.getRawX();
        float eventY = event.getRawY();
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        int viewX = position[0];
        int viewY = position[1];
        int width = view.getWidth();
        int height = view.getHeight();

        return eventX >= viewX
                && eventX <= viewX + width
                && eventY >= viewY
                && eventY <= viewY + height;
    }


    /**
     * 点击空白隐藏软键盘
     */
    public static void hideKeyboardWhileClickOthers(MotionEvent event, View view, Activity activity) {
        try {
            if (view != null && view instanceof EditText) {
                int[] location = {0, 0};
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth(), botom = top + view.getHeight();
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.getY() < top - 100 || event.getRawY() > botom) {
                    // 隐藏键盘
                    IBinder token = view.getWindowToken();
                    InputMethodManager inputMethodManager = (InputMethodManager) activity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(token,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static final Pattern emoji = Pattern.compile(
            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    public static boolean checkHasEmoji(CharSequence charSequence) {
        if (charSequence == null) return false;
        Matcher matcher = emoji.matcher(charSequence);
        if (matcher.find()) return true;
        else return false;
    }


    private static final SimpleDateFormat SOURCE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final long MINUTE_MILLIS = 60 * 1000;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getModifyTime(String modifyTime) {
        Date time = null;
        try {
            time = SOURCE_DATE_FORMAT.parse(modifyTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        } catch (NullPointerException e) {
            //https://bugly.qq.com/v2/crash-reporting/crashes/1104528778/64404?pid=1
            return "";
        }

        long mTime = time.getTime();
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - mTime < 0) {
            return "";
        } else if (currentTimeMillis - mTime < MINUTE_MILLIS) {
            return (currentTimeMillis - mTime) / 1000 + "秒前";
        } else if (currentTimeMillis - mTime < HOUR_MILLIS) {
            return (currentTimeMillis - mTime) / MINUTE_MILLIS + "分钟前";
        } else if (currentTimeMillis - mTime < DAY_MILLIS) {
            return (currentTimeMillis - mTime) / HOUR_MILLIS + "小时前";
        } else {
            return DISPLAY_DATE_FORMAT.format(mTime);
        }
    }


    public static List<JSONObject> getJsonList(JSONArray jsonArray) {
        List<JSONObject> jsonList = new ArrayList<>();
        if (jsonArray == null) return jsonList;
        int size = jsonArray.length();
        for (int i = 0; i < size; i++) {
            try {
                jsonList.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {

            }
        }
        return jsonList;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getPhoneSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机品牌
     *
     * @return 手机品牌
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机制造商
     *
     * @return 手机制造商
     */
    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */

    /**
     * 点赞动效
     *
     * @param i 0未点赞，1已点赞
     */
    public static void showGoodView(int i, Context context, View view) {
        if (i == 0) {
        }
    }


    public static void addEmojiFilter(EditText editText) {
        if (editText == null) {
            return;
        }
//        直接set新的InputFilter会导致maxLength属性失效,新逻辑在设置表情过滤会保存其他的已设置的过滤
//        editText.setFilters(emojiFilters);
        InputFilter[] oldFilters = editText.getFilters();
        InputFilter[] newFilters = new InputFilter[oldFilters.length + 1];
        System.arraycopy(oldFilters, 0, newFilters, 0, oldFilters.length);
        editText.setFilters(newFilters);
    }

    /**
     * 动态替换icon需求专用：获取一个icon对应的所有url里面时间符合的url,代码用的地放太多，先扔这里吧
     *
     * @param dynamicIconCacheEntity
     * @return 返回可能为null
     */
//    public static String getAimIconUrl( DynamicIconCacheEntity dynamicIconCacheEntity, String buttonKey) {
//        long currentTime = new Timestamp(System.currentTimeMillis()).getTime();
//        List<DynamicIconCacheEntity.ResultListBean.IconListBean> barHomepageIconList = null;
//        DynamicIconCacheEntity.ResultListBean.IconListBean aimIcon = null;
//        List<DynamicIconCacheEntity.ResultListBean> allIconList = dynamicIconCacheEntity.getResultList();
//
//        if (allIconList != null && allIconList.size() != 0) {
//            for (DynamicIconCacheEntity.ResultListBean iconBean : allIconList) {//底部首页button对应的所有icon链接
//                if (iconBean.getButtonKey().equals(buttonKey)) {
//                    barHomepageIconList = iconBean.getIconList();
//                    break;
//                }
//            }
//        }

//        if (barHomepageIconList != null && barHomepageIconList.size() != 0) {//底部button下面的时间符合的icon链接
//            for (DynamicIconCacheEntity.ResultListBean.IconListBean iconListBean : barHomepageIconList) {
//                if (currentTime >= iconListBean.getStartTime() && currentTime <= iconListBean.getEndTime()) {
//                    aimIcon = iconListBean;
//                    break;
//                }
//            }
//        }
//        if (aimIcon == null) {
//            return null;
//        } else {
//            return aimIcon.getIconUrl();
//        }
//    }

    /**
     * 判断fresco是否缓存该图片
     *
     * @return
     */
//    public static boolean hasFrescoCache(String url) {
//        if (url == null) return false;
//        DataSource<Boolean> source = Fresco.getImagePipeline().isInDiskCache(Uri.parse(url));
//        Boolean isDiskCache = null;
//
//        if (source != null)
//            isDiskCache = source.getResult();
//
//        boolean value = (isDiskCache == null ? false : isDiskCache.booleanValue()) ||
//                Fresco.getImagePipeline().isInBitmapMemoryCache(Uri.parse(url));
//
//        return value;
//    }

//    public static void getBitmapFromFrescoMemory(String url, Context context, final SimpleDraweeView sdv) {
//        ImageRequest imageRequest = ImageRequestBuilder
//                .newBuilderWithSource(Uri.parse(url))
//                .setProgressiveRenderingEnabled(true)
//                .build();
//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        DataSource<CloseableReference<CloseableImage>>
//                dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
//        dataSource.subscribe(new BaseBitmapDataSubscriber() {
//            @Override
//            public void onNewResultImpl(Bitmap bitmap) {
//                sdv.setImageBitmap(bitmap);
//            }
//
//            @Override
//            public void onFailureImpl(DataSource dataSource) {
//
//            }
//        }, CallerThreadExecutor.getInstance());
//    }

    /**
     * 通过原图url返回压缩图url
     *
     * @param originUrl 原图url
     * @return 压缩图url
     */
    String getSmallImageUrl( String originUrl) {
        if (originUrl != null && originUrl.contains("original")) {
            String smallUrl = originUrl.replaceFirst("original", "small");
            return smallUrl;
        }
        return originUrl;
    }

    /**
     * @param colorIfMatch 当text中匹配到keyword的字符串需要改变为何种颜色
     * @param content      待匹配字符串
     * @param keyword      匹配关键字
     * @return SpannableString
     */
    public static SpannableString matcherSearchTextChangeColor(int colorIfMatch, String content,
                                                               String keyword) {
        if (content == null) {
            content = "";
        }
        SpannableString span = new SpannableString(content);
        if (keyword == null) {

            return span;
        }
        Pattern p;
        // 先使用不会造成异常的特殊字符将keyword进行转义操作
        char[] specialRegex = ".$^]}|".toCharArray();
        for (char c : specialRegex) {
            String s = String.valueOf(c);
            if (keyword.contains(s)) {
                keyword = keyword.replace(s, "\\" + c);
            }
        }
        try {
            p = Pattern.compile(keyword.toLowerCase());
        } catch (PatternSyntaxException e) {
//             如若包含了会导致异常的特殊字符，那么再次字符串转义
            char[] specialString = "*?+[(){".toCharArray();
            for (char c : specialString) {
                String s = String.valueOf(c);
                if (keyword.contains(s)) {
                    keyword = keyword.replace(s, "\\" + c);
                }
            }
            try {
                // 转义之后再进行compile
                p = Pattern.compile(keyword.toLowerCase());
            } catch (PatternSyntaxException e1) {
                // 如若还出现异常，那么直接返回原span
                return span;
            }
        }
        Matcher m = p.matcher(content.toLowerCase());
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            span.setSpan(new ForegroundColorSpan(colorIfMatch), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return span;
    }

    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;

    /**
     * 防止button快速连点
     *
     * @return
     */
    public static boolean isNotFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    /**
     * 显示进度加载框
     *
     * @param context
     * @param msg
     * @param isAnimation
     * @param imageId
     * @return
     */
   /* public static Dialog createLoadingDialog(Context context, String msg,
                                             boolean isAnimation, int imageId) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = v.findViewById(R.id.img);
        TextView tipTextView = v.findViewById(R.id.tipTextView);// 提示文字

        if (imageId != 0) {
            spaceshipImage.setImageResource(imageId);
        }
        if (isAnimation) {
            // 加载动画
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                    context, R.anim.test_animations);
            // 使用ImageView显示动画
            spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        }

        if (TextUtils.isEmpty(msg)) {
            tipTextView.setVisibility(View.GONE);
        } else {
            tipTextView.setVisibility(View.VISIBLE);
            tipTextView.setText(msg);// 设置加载信息
        }

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;

    }
*/
    /**
     * 将图片转换成Base64编码的字符串
     *
     * @param path
     * @return base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }


    /**
     * double 除法
     *
     * @param d1
     * @param d2
     * @param scale 四舍五入 小数点位数
     * @return
     */
    public static double chuFa(double d1, double d2, int scale) {
        //  当然在此之前，你要判断分母是否为0，
        //  为0你可以根据实际需求做相应的处理
        try {
            if (d2 == 0) {
                throw new Exception("分母不能为0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.divide
                (bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件 
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小 

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Long.valueOf(arrayOfString[1]) * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte 
            localBufferedReader.close();
            return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
        } catch (Exception e) {
            return "";
        }
    }


    public static String getAvailMemory(Context context) {// 获取android当前可用内存大小 
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(mi);//mi.availMem; 当前系统的可用内存
            return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        } catch (Exception e) {
            return "";
        }
    }


    public static String getTotalInternalMemorySize(Context context) {
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return Formatter.formatFileSize(context, blockSize * totalBlocks);
        } catch (Exception e) {
            return "";
        }

    }

    public static String getAvailableInternalMemorySize(Context context) {
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return Formatter.formatFileSize(context, availableBlocks * blockSize);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * base64转化成Bitmap
     *
     * @param string
     * @return
     */
    public static Bitmap base64ToBitmap(String string) {
        if (string == null) return null;
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 保存图片到相册
     *
     * @param bitmap
     */
    public static void save2Album(Context context, Bitmap bitmap) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "尚德机构");
        if (!appDir.exists()) appDir.mkdir();

        String fileName = System.currentTimeMillis() + ".jpg";
        final File file = new File(appDir, fileName);
        try {
            final Context newContext = context.getApplicationContext();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    newContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测为不合法字符，就转为unicode 编码，OkHttp 中的checkNameAndValue去遍历每个字符就不会为非法了
     *
     * @return
     */
    public static String getValidFileName(String fileName) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = fileName.length(); i < length; i++) {
            char c = fileName.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
