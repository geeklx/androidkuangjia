package com.sangfor.sdkdemo.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sangfor.sdkdemo.R;
import com.sangfor.sdkdemo.utils.PermissionUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileTestActivtiy extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FileTestActivtiy";
    private static final int SDCARD_PERMISSION_CODE = 101;
    private TextView mTestResult;
    private Button mCheckApplyPermissionBtn,
            mCreateDataDataBtn,
            mCreateAndroidDataBtn,
            mCreateSdcardFileBtn,
            mReadOtherDataBtn,
            mAndroid11GlobalPermissionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vpn_activity_file_test_activtiy);
        initViews();
    }

    private void initViews() {
        mCheckApplyPermissionBtn = findViewById(R.id.btn_check_apply_permission);
        mCreateDataDataBtn = findViewById(R.id.btn_create_data_data_file);
        mCreateAndroidDataBtn = findViewById(R.id.btn_create_android_data_file);
        mCreateSdcardFileBtn = findViewById(R.id.btn_create_sdcard_file);
        mReadOtherDataBtn = findViewById(R.id.btn_read_other_android_data_file);
        mAndroid11GlobalPermissionBtn = findViewById(R.id.btn_android11_apply_permission);
        mTestResult = findViewById(R.id.tv_test_result);

        //检查/申请sdcard权限
        mCheckApplyPermissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtil.requestSDCardPermissions(FileTestActivtiy.this, SDCARD_PERMISSION_CODE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        String appName = "AppName";
        PackageManager pm = getPackageManager();
        try {
            int labelRes = pm.getPackageInfo(this.getPackageName(), 0).applicationInfo.labelRes;
            appName = getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
            showResult(e.toString());
        }

        File dataFile = new File(getApplicationContext().getDir(appName, MODE_APPEND), appName + ".txt");
        File androidDataFile = new File(getExternalFilesDir(null), "getExternalFilesDir" + appName + ".txt");
        File sdcardFile = new File("/sdcard", appName + ".txt");
        File renameFile = new File("/sdcard", "【标准化发布】 FGAP_3.0.2_20200807版本升级指导.pdf");
        String otherPackage = getExternalFilesDir(null).getAbsolutePath().replace(getPackageName(), "com.securemail");
        File otherAndroidDataFile = new File(otherPackage, "otherAndroidData.txt");
        String specifDirName = ((EditText) findViewById(R.id.specif_dir_name_edit)).getText().toString();
        String specifFileName = ((EditText) findViewById(R.id.specif_file_name_edit)).getText().toString();
        if (TextUtils.isEmpty(specifFileName)) {
            specifFileName = "【标准化发布】 FGAP_3.0.2_20200807版本升级指导.pdf-crypt2";
            ((EditText) findViewById(R.id.specif_file_name_edit)).setText(specifFileName);
        }
        File specifFile = new File(String.format("/sdcard/%s", specifDirName), specifFileName);

        int id = v.getId();//在data/data/沙箱创建文件
        if (id == R.id.btn_create_data_data_file) {
            createFile(dataFile);
            //测试写文件
        } else if (id == R.id.btn_write_data_data_file) {
            writeFile(dataFile);
            //测试读文件
        } else if (id == R.id.btn_read_data_data_file) {
            readFile(dataFile);
        } else if (id == R.id.btn_see_data_data_file) {
            showFiles(dataFile);
        } else if (id == R.id.btn_delete_data_data_file) {
            deleteFile(dataFile);
            //在Android/data/专属目录创建文件
        } else if (id == R.id.btn_create_android_data_file) {
            createFile(androidDataFile);
        } else if (id == R.id.btn_write_android_data_file) {
            writeFile(androidDataFile);
        } else if (id == R.id.btn_read_android_data_file) {
            readFile(androidDataFile);
        } else if (id == R.id.btn_see_android_data_file) {
            showFiles(androidDataFile);
        } else if (id == R.id.btn_delete_android_data_file) {
            deleteFile(androidDataFile);
            //在/sdcard根目录创建文件
        } else if (id == R.id.btn_create_sdcard_file) {
            createFile(sdcardFile);
        } else if (id == R.id.btn_write_sdcard_file) {
            writeFile(sdcardFile);
        } else if (id == R.id.btn_read_sdcard_file) {
            readFile(sdcardFile);
        } else if (id == R.id.btn_see_sdcard_file) {
            showFiles(sdcardFile);
        } else if (id == R.id.btn_delete_sdcard_file) {
            deleteFile(sdcardFile);
        } else if (id == R.id.btn_create_specif_file) {
            createFile(specifFile);
        } else if (id == R.id.btn_write_specif_file) {
            writeFile(specifFile);
        } else if (id == R.id.btn_read_specif_file) {
            readFile(specifFile);
        } else if (id == R.id.btn_see_specif_file) {
            showFiles(specifFile);
        } else if (id == R.id.btn_delete_specif_file) {
            deleteFile(specifFile);
        } else if (id == R.id.btn_create_other_android_data_file) {
            createFile(otherAndroidDataFile);
            //读取其他应用的Android/data/专属目录文件
        } else if (id == R.id.btn_read_other_android_data_file) {
            readFile(otherAndroidDataFile);
        } else if (id == R.id.btn_write_other_android_data_file) {
            writeFile(otherAndroidDataFile);
        } else if (id == R.id.btn_see_other_android_data_file) {
            showFiles(otherAndroidDataFile);
        } else if (id == R.id.btn_delete_other_android_data_file) {
            deleteFile(otherAndroidDataFile);
        } else if (id == R.id.btn_create_rename_file) {
            boolean ret = sdcardFile.renameTo(renameFile);
            showResult(String.format("rename file:[%s] result:%b", renameFile.getAbsolutePath(), ret));
        } else if (id == R.id.btn_write_rename_file) {
            writeFile(renameFile);
        } else if (id == R.id.btn_read_rename_file) {
            readFile(renameFile);
        } else if (id == R.id.btn_see_rename_file) {
            showFiles(renameFile);
        } else if (id == R.id.btn_delete_rename_file) {
            deleteFile(renameFile);
            //查看Android/data下的文件
        } else if (id == R.id.btn_list_data_files) {
            showAndroidDataFiles();
            //Android11申请全局读写权限`
        } else if (id == R.id.btn_android11_apply_permission) {
            applyAndroid11GlobalExternalPermission();
        }
    }

    private void showResult(String msg) {
        mTestResult.setText(msg);
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SDCARD_PERMISSION_CODE) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    showResult("授权sdcard权限失败!");
                }
            }
        }
    }

    private void createFile(File testFile) {
        if (testFile.exists()) {
            showResult(String.format("file:[%s] already exits", testFile.getAbsolutePath()));
            return;
        }

        File parentDir = testFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        boolean createSuccess = false;
        try {
            createSuccess = testFile.createNewFile();
        } catch (IOException e) {
        } finally {
            showResult(String.format("create file:[%s] result:%b", testFile.getAbsolutePath(), createSuccess));
        }
    }

    private void writeFile(File testFile) {
        if (!testFile.exists()) {
            showResult(String.format("file:[%s] not exits", testFile.getAbsolutePath()));
            return;
        }
        try {
            FileWriter writer = new FileWriter(testFile);
            writer.write("AAA");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            showResult(e.toString());
        } finally {
            showResult("write AAA finish");
        }
    }

    private void readFile(File testFile) {
        if (!testFile.exists()) {
            showResult(String.format("file:[%s] not exits", testFile.getAbsolutePath()));
            return;
        }
        try {
            FileReader reader = new FileReader(testFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            showResult(String.format("read file:[%s] content:[%s]", testFile.getAbsolutePath(), result.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showFiles(File testFile) {
        File dirFile = testFile;
        if (!dirFile.isDirectory()) {
            dirFile = testFile.getParentFile();
        }

        if (!dirFile.exists()) {
            showResult(String.format("%s 目录不存在", dirFile.getAbsoluteFile()));
            return;
        }

        File[] files = dirFile.listFiles();
        if (files == null) {
            showResult("没有文件");
            return;
        }

        StringBuilder filesStr = new StringBuilder();
        int i = 0;
        for (File f : files) {
            filesStr.append(String.format("%d:  %s\n", ++i, f.getAbsolutePath()));
        }

        showResult(String.format("%s 目录下： \n %s", dirFile.getAbsoluteFile(), filesStr));
    }

    private void deleteFile(File testFile) {
        if (!testFile.exists()) {
            showResult(String.format("file:[%s] not exits", testFile.getAbsolutePath()));
            return;
        }
        try {
            boolean deleteResult = testFile.delete();
            showResult(String.format("delete file:[%s] content:[%s]", testFile.getAbsolutePath(), deleteResult ? "success" : "failed"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyAndroid11GlobalExternalPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            showResult("当前权限只适用于Android11");
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        startActivity(intent);
    }

    private void showAndroidDataFiles() {
        if (PermissionUtil.isNeedRequestSDCardPermission(this)) {
            showResult("注意当前手机没有外置存储权限");
        }
        File file = new File("/sdcard/Android", "data");
        if (!file.exists()) {
            showResult("/sdcard/Android/data 目录不存在不");
            return;
        }
        File[] files = file.listFiles();
        if (files == null) {
            showResult("没有文件");
            return;
        }
        StringBuilder filesStr = new StringBuilder();
        for (File f : files) {
            filesStr.append(String.format(" 存在文件 [%s] \n", f.getAbsolutePath()));
        }
        showResult(String.format("/sdcard/Android/data 目录下： \n %s", filesStr));
    }
}