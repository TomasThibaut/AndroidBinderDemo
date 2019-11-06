package cn.gc.module2

import android.os.Bundle
import android.text.TextUtils
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import cn.gc.module2.shell.ShellUtils
import cn.gc.module2.utils.MD5Utils
import cn.gc.module2.utils.SPXmlParser
import cn.gc.module2.utils.WechatCode

import kotlinx.android.synthetic.main.activity_wx_db.*
import kotlinx.android.synthetic.main.content_wx_db.*
import net.sqlcipher.Cursor
import net.sqlcipher.database.SQLiteDatabase
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

class WxDBActivity : AppCompatActivity() {
    companion object {
        const val PAC = "com.tencent.mm"
        const val MicroMsg = "MicroMsg"
        const val EnMicroMsg = "EnMicroMsg.db"
        const val SP = "shared_prefs"
        const val MM_PREFS = "com.tencent.mm_preferences.xml"
        const val SYS_CONFIG_PREFS = "system_config_prefs.xml"
        const val AUTH_INFO_PREFS = "auth_info_key_prefs.xml"
        const val LAST_AVATAR_DIR = "last_avatar_dir"
    }

    data class FolderKey(val folder: String, val key: String)
    private var wxSqlDatabase: SQLiteDatabase? = null

    private var path = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wx_db)
        setSupportActionBar(toolbar)
        SQLiteDatabase.loadLibs(this)
        try {
            openDb()
        } catch (e: Exception) {
            e.printStackTrace()
            logi("openDb error")
        }
    }

    private fun openDb() {
        //获取所有微信db路径
        val resultMsg = ShellUtils.execCmd("find /data/data/ -name *EnMicroMsg.db").successMsg
        logi("resultMsg = $resultMsg")
        val pathList = resultMsg.split("\t")
        logi("pathList = $pathList and size = ${pathList.size}")
        val rowPath = pathList[1]
        val mmIndex = rowPath.indexOf("com.tencent.mm")

        path = rowPath.substring(0, mmIndex + PAC.length + 1)
        //修改文件权限
        ShellUtils.execCmd("chmod -R 777 $path")

        val uin = getUinFromSps()
        val imei = getPhoneIMEI()
        val folder = MD5Utils.getMD5Digest("mm$uin")
        val mStart = path.indexOf(MicroMsg)
        val mEnd = path.lastIndexOf("/")
        val dbFolder = rowPath.substring(mStart + MicroMsg.length + 1, mEnd)
        var folderKey: FolderKey? = null
        if (folder == dbFolder) {
            folderKey = FolderKey(folder, getWXDatabaseKey(uin, imei!!))
        } else {
            val uin2 = uin.toLong() - 4294967296
            val uin2Folder = MD5Utils.getMD5Digest("mm$uin2")
            if (uin2Folder == dbFolder) {
                folderKey = FolderKey(uin2Folder, getWXDatabaseKey(uin2.toString(), imei!!))
            }
        }

        //open it !
        logi("rowPath: $rowPath")
        wxSqlDatabase = SQLiteDatabase.openDatabase(rowPath,"",null,SQLiteDatabase.NO_LOCALIZED_COLLATORS or SQLiteDatabase.OPEN_READONLY)
        wxSqlDatabase!!.execSQL("PRAGMA key = '${folderKey?.key}';")
        wxSqlDatabase!!.execSQL("PRAGMA cipher_use_hmac = OFF;")
        wxSqlDatabase!!.execSQL("PRAGMA cipher_page_size = 1024;")
        //数据库读取必须先等这行代码执行完毕
        //https://stackoverflow.com/questions/13537073/android-sqlcipher-pragma-problems
        wxSqlDatabase!!.execSQL("PRAGMA kdf_iter = 4000;")
        val isOpenAndDecoded = isDBDecodedAlready()
        logi("isOpenAndDecoded = $isOpenAndDecoded")
    }

    //检测是否数据库已经打开并且解密
    fun isDBDecodedAlready(): Boolean {
        return if (isDBConnection()) {
            var cursor: Cursor? = null
            try {
                //db.isOpen并不意味着db成功解密,只能以一个操作来判断是否报错来判断,这里使用sqlite的默认表来做判断
                cursor = wxSqlDatabase?.rawQuery("SELECT name FROM sqlite_master limit 1" ,null)
                true
            } catch (e: Exception) {
                false
            }finally {
                cursor?.close()
            }
        } else {
            false
        }
    }

    //检测数据库链接状态
    fun isDBConnection(): Boolean {
        if (wxSqlDatabase != null) return wxSqlDatabase!!.isOpen
        return false
    }

    private fun getWXDatabaseKey(uin: String, imei: String): String {
        val dbKeyMD5 = MD5Utils.getMD5Digest(imei + uin)
        if (TextUtils.isEmpty(dbKeyMD5) || dbKeyMD5.length < 7) {
            return ""
        }
        return dbKeyMD5.substring(0, 7)
    }

    //从几个sp文件里面取uin
    private fun getUinFromSps(): String {
        var curUin: String? = null
        var spHashMap: HashMap<String, String>? = null
        //com.tencent.mm_preferences.xml
        var spFile = File("$path$SP/$MM_PREFS")
        if (spFile.exists()) {
            spHashMap = SPXmlParser.parse(BufferedInputStream(FileInputStream(spFile)))
            curUin = spHashMap["last_login_uin"]
            logi("getUin com.tencent.mm_preferences map size ${spHashMap.size} $curUin")
        }

        //system_config_prefs.xml
        if (TextUtils.isEmpty(curUin) || curUin == "0") {
            spFile = File("$path$SP/$SYS_CONFIG_PREFS")
            if (spFile.exists()) {
                spHashMap = SPXmlParser.parse(BufferedInputStream(FileInputStream(spFile)))
                curUin = spHashMap["default_uin"]
                logi("getUin system_config_prefs map size ${spHashMap.size} $curUin")
            }
        }

        //auth_info_key_prefs.xml
        if (TextUtils.isEmpty(curUin) || curUin == "0") {
            spFile = File("$path$SP/$AUTH_INFO_PREFS")
            if (spFile.exists()) {
                spHashMap = SPXmlParser.parse(BufferedInputStream(FileInputStream(spFile)))
                curUin = spHashMap["_auth_uin"]
                logi("getUin auth_info_key_prefs map size ${spHashMap.size} $curUin")
            }
        }

        return curUin ?: ""
    }

    private fun getPhoneIMEI(): String {
        val imeiList: MutableList<String> = WechatCode.getImei()
        logi("getPhoneIMEI ,imeiList = $imeiList and size = ${imeiList.size}")
        return imeiList[0]
    }
}
