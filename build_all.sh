#!/bin/bash
# 乐视S50 Air TVBOX 一键构建脚本
# 用法: ./build_all.sh

set -e

echo "========================================"
echo "  乐视S50 Air TVBOX 构建工具"
echo "  兼容 Android 4.4 (API 19)"
echo "========================================"
echo ""

# 检查环境
echo "[1/5] 检查构建环境..."

if ! command -v java &> /dev/null; then
    echo "X 未找到Java,请先安装JDK 8"
    exit 1
fi
echo "√ Java: $(java -version 2>&1 | head -1)"

if [ -z "$ANDROID_HOME" ] && [ -z "$ANDROID_SDK_ROOT" ]; then
    echo "注意: 未找到ANDROID_HOME,请设置后重试"
    echo "  下载: https://developer.android.com/studio"
    echo "  设置: export ANDROID_HOME=/path/to/sdk"
fi

# 生成Gradle Wrapper
echo ""
echo "[2/5] 生成Gradle Wrapper..."
cat > gradle/wrapper/gradle-wrapper.properties << 'EOF'
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-4.10.3-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
EOF

# 创建签名配置
echo ""
echo "[3/5] 生成调试签名..."
mkdir -p app/keystore
if [ ! -f app/keystore/debug.keystore ]; then
    keytool -genkey -v -keystore app/keystore/debug.keystore \
        -storepass android -alias androiddebugkey \
        -keypass android -keyalg RSA -keysize 2048 -validity 10000 \
        -dname "CN=Android Debug,O=LetvTVBox,C=CN" 2>/dev/null
    echo "√ 调试签名已生成"
else
    echo "√ 调试签名已存在"
fi

# 构建APK
echo ""
echo "[4/5] 开始构建APK..."
echo "   目标设备: 乐视S50 Air (Android 4.4 / LetvUI 3.0)"
echo "   最低支持: API 14 (Android 4.0)"
echo "   输出路径: app/build/outputs/apk/release/"
echo ""

chmod +x gradlew
./gradlew clean assembleRelease

# 检查输出
echo ""
echo "[5/5] 构建完成!"
echo ""
if [ -f app/build/outputs/apk/release/app-release.apk ]; then
    SIZE=$(du -h app/build/outputs/apk/release/app-release.apk | cut -f1)
    echo "√ APK文件: app/build/outputs/apk/release/app-release.apk"
    echo "  文件大小: $SIZE"
    echo ""
    echo "========================================"
    echo "  安装到乐视S50 Air 电视:"
    echo "========================================"
    echo ""
    echo "方法一: U盘安装"
    echo "  1. 将APK复制到U盘(FAT32格式)"
    echo "  2. 插入电视USB口"
    echo "  3. 打开文件管理器 → 点击APK安装"
    echo "  4. 如提示禁止安装, 去设置→安全→允许未知来源"
    echo ""
    echo "方法二: ADB安装"
    echo "  1. 电视开启ADB调试"
    echo "     信号源 → 输入2580 → 其他选项 → ADB调试 → 开启"
    echo "  2. 获取电视IP(设置→网络→查看IP)"
    echo "  3. adb connect 电视IP:5555"
    echo "  4. adb install -r app-release.apk"
    echo ""
    echo "方法三: 甲壳虫ADB助手(手机端)"
    echo "  1. 手机安装甲壳虫ADB助手APP"
    echo "  2. 手机和电视连同一WiFi"
    echo "  3. 搜索电视设备 → 一键安装APK"
    echo ""
else
    echo "X APK未生成,请检查构建日志"
fi
