# EasyUpdateDemo
安卓app应用更新框架
### 使用方法
**1.实体类UpdateInfo**
```
public class UpdateInfo {
    private String url; // 下载地址
    private String md5;  // 安装包的MD5
    private String fileName; // 文件名（apk结尾）
    private int versionCode;   
    private String versionName;
    private String characters; // 版本特性
    private boolean forceUpate = true; // 强制更新(使用默认弹出框的情形下，只有一个更新按钮，且弹出框不可取消)
    private boolean autoInstall = true;  // 自动更新
    }
````
**2.代码示例**
```
// 示例一：这个方法是使用默认的弹出框，而且不对updateinfo的信息做逻辑判断，弹出框内点击“升级”后，直接下载安装包，并在后台生成Nofication。
EasyUpdate.debug(true)
          .check(MainActivity.this, updateInfo);

// 示例二：方法check(Context context,UpdateInfo updateInfo,CheckListener checkListener)内，如果 参数checkListener缺省或为null，则是与示例一一样的效果
//        否则要自己逻辑判断后，实现接下来的操作
EasyUpdate.debug(true)
          .check(MainActivity.this, updateInfo,new EasyUpdate.CheckListener() {
                            @Override
                            public void handle(UpdateInfo updateInfo) {
                                 /* 
                                   这里请自行对updateInfo的信息进行判断 
                                 */
                                 
                                 /* 
                                  下载方法一 EasyUpdate.download(Context context,UpdateInfo updateInfo)
                                  下载方法二 EasyUpdate.download(Context context,UpdateInfo updateInfo, DownloadListener downloadListener)
                                   与check方法类似，不传入downloadListener对象，使用默认的Nofication并下载，否则要自己实现下载时的表现
                                */
                                 EasyUpdate.download(MainActivity.this,updateInfo);
                            }
                        });
```

### gradle引入
项目的build.gradle
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
app的build.gradle
```
dependencies {
	       	   compile 'com.github.Ccapton:EasyUpdateDemo:1.1.1'
	}
```
