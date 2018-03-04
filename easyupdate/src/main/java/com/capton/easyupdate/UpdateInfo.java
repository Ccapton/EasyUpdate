package com.capton.easyupdate;

/**
 * Created by capton on 2018/3/1.
 */

public class UpdateInfo {
    private String url;
    private String md5;
    private String fileName;
    private int versionCode;
    private String versionName;
    private String characters;
    private boolean forceUpate = true;
    private boolean autoInstall = true;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getCharacters() {
        return characters;
    }

    public void setCharacters(String characters) {
        this.characters = characters;
    }

    public boolean isForceUpate() {
        return forceUpate;
    }

    public void setForceUpate(boolean forceUpate) {
        this.forceUpate = forceUpate;
    }

    public boolean isAutoInstall() {
        return autoInstall;
    }

    public void setAutoInstall(boolean autoInstall) {
        this.autoInstall = autoInstall;
    }
}
