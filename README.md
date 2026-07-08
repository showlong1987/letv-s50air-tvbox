# 乐视S50 Air TVBOX 套壳版

专为 **乐视超级电视 S50 Air** 深度定制的 TVBOX 套壳应用，适配 LetvUI 3.0 系统（底层 Android 4.4）。

## 项目特点

| 特性 | 说明 |
|------|------|
| **设备适配** | 专为乐视S50 Air定制（Mstar 6A918 / Mali 450 MP4 / 2GB RAM） |
| **系统兼容** | 最低支持 Android 4.0（API 14），目标 Android 4.4（API 19） |
| **分辨率** | 适配 1920x1080 全高清屏幕 |
| **遥控器** | 完整适配乐视超级遥控器（方向键/确认/返回/菜单/音量） |
| **开机自启** | 支持开机自动启动TVBOX（可设置开关） |
| **接口配置** | 内置多个热门接口源，支持自定义接口地址 |
| **播放器** | 内置硬解/软解切换，适配Mstar芯片 |

## 内置接口源

- 饭太硬: `http://www.fantaiying.com/tv/`
- 巧记: `http://cdn.qiaoji8.com/tvbox.json`
- 小雅: `http://drpy.site/js1`
- 多多: `https://yydsys.top/duo`

## 快速开始

### 方式一：使用 GitHub Actions 自动构建（推荐，零基础）

1. 下载本仓库代码，解压
2. 在 GitHub 上新建仓库（仓库名随意，如 `my-letv-tvbox`）
3. 将代码推送到你的仓库：
   ```bash
   git init
   git add .
   git commit -m "init"
   git remote add origin https://github.com/你的用户名/my-letv-tvbox.git
   git push -u origin master
   ```
4. 进入仓库页面 → 点击顶部 **Actions** 标签
5. 如果看到黄色提示，点 **"I understand my workflows..."** 启用
6. 左侧选构建工作流 → 点 **Run workflow** → 再点确认
7. 等待 3~8 分钟，绿色勾 = 成功
8. 点进构建记录 → 底部 **Artifacts** → 下载 APK

### 方式二：本地构建

```bash
# 1. 确保已安装 Java 8 和 Android SDK
export ANDROID_HOME=/path/to/android-sdk

# 2. 赋予执行权限
chmod +x gradlew build_all.sh

# 3. 一键构建
./build_all.sh
```

### 方式三：基于开源TVBOX代码修改

如果需要更完整的TVBOX功能，可以基于以下开源项目修改：

1. **q215613905/TVBoxOS** - 功能最全的社区版本
2. **takagen99/Box** - 优化版，性能更好
3. **rbqvq/TVBoxOS** - 已适配 Android 4.x 的版本

修改要点：
- 修改 `app/build.gradle` 中的 `minSdkVersion` 为 14
- `targetSdkVersion` 改为 19
- `compileSdkVersion` 改为 19
- 替换图标和背景为乐视风格
- 内置乐视S50 Air 专用接口源
- 修改包名为 `com.letv.tvbox`

## 安装到乐视S50 Air 电视

### 方法一：U盘安装（最稳）

1. 将 APK 文件复制到 U 盘（**FAT32 格式**）
2. 插入电视 USB 接口
3. 打开文件管理器 → 找到 APK → 点击安装
4. 如提示"禁止安装"，去 **设置 → 安全 → 允许未知来源**

### 方法二：ADB 安装

1. 开启 ADB 调试：
   - 打开电视 → 按 **信号源** 按钮
   - 输入数字 **2580**
   - 选择 **其他选项** → **ADB调试** → **开启**
   - 重启电视

2. 获取电视 IP（设置 → 网络 → 查看IP）

3. 执行命令：
```bash
adb connect 电视IP:5555
adb install -r 乐视TVBOX.apk
```

### 方法三：甲壳虫ADB助手（手机端，最简单）

1. 手机安装「甲壳虫ADB助手」APP
2. 确保手机和电视在同一 WiFi
3. 搜索设备 → 连接电视 → 安装APK

## 配置说明

安装后首次打开，按遥控器 **菜单键** 进入设置页面：

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| 接口配置地址 | TVBOX数据源接口 | 饭太硬接口 |
| 播放器类型 | hard(硬解)/soft(软解) | hard |
| EPG地址 | 直播节目预告 | https://epg.112114.xyz/ |
| 开机自启 | 电视开机是否自动启动 | 开启 |

## 乐视S50 Air 硬件参数

```
型号: 乐视超级电视 S50 Air
CPU: Mstar 6A918 (Cortex A9 四核 1.5GHz)
GPU: Mali 450 MP4 (四核)
RAM: 2GB DDR3
ROM: 16GB eMMC
系统: LetvUI 3.0 (Android 4.4)
分辨率: 1920x1080 (全高清)
网络: 双频WiFi + 有线网卡
```

## 常见问题排查

### 安装提示"解析包时出现问题"

**原因**: APK 的 minSdkVersion 高于电视系统版本

**解决**: 确保构建时 `minSdkVersion=14`，`targetSdkVersion=19`。本仓库已正确配置。

### 安装后打不开

1. 检查是否开启了"未知来源"安装权限
2. 检查 APK 是否完整（重新下载）
3. 确认电视系统是 Android 4.4 或更低

### 播放卡顿

1. 进入设置 → 播放器类型改为 "soft"（软解）
2. 检查网络是否稳定
3. 尝试更换接口源

## 注意事项

1. **开启未知来源**: 设置 → 安全 → 允许安装未知来源应用
2. **系统版本**: 确认电视系统为 LetvUI 3.0
3. **存储空间**: 确保至少有 100MB 可用空间
4. **网络环境**: 需要稳定的网络连接以加载在线内容
5. **U盘格式**: 使用 FAT32 格式，NTFS 可能不识别

## 免责声明

本项目仅供学习交流使用，所有资源来源于互联网，版权归原作者所有。请勿用于商业用途。

## 相关资源

- [TVBox 官方仓库](https://github.com/CatVodTVOfficial/TVBoxOSC)
- [q215613905/TVBoxOS](https://github.com/q215613905/TVBoxOS)
- [takagen99/Box](https://github.com/takagen99/Box)
- [TVBox Q版（支持Android 4.x）](https://github.com/rbqvq/TVBoxOS)
