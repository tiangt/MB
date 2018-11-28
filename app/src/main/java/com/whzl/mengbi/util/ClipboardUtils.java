package com.whzl.mengbi.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

/**
 * @author cliang
 * @date 2018.11.27
 */
public class ClipboardUtils {

    public static void putTextIntoClip(Context context, String str) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("simple text copy", str);
        //添加ClipData对象到剪切板中
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "复制到剪贴板", Toast.LENGTH_SHORT).show();
    }
}
