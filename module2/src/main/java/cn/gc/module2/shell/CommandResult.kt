package cn.gc.module2.shell

/**
 * Created by 宫成 on 2019-07-30 17:18.
 */
data class CommandResult(
    /**
     * 结果码
     */
    var result: Int,
    /**
     * 成功信息
     */
    var successMsg: String,
    /**
     * 错误信息
     */
    var errorMsg: String
)