# MiroTalk Native Android

这是一个纯原生 Kotlin/XML Android 客户端：媒体采集和轨道控制使用 WebRTC Android API，信令使用 Socket.IO，不使用 WebView。

## 配置

在 `app/build.gradle.kts` 的 `MIROTALK_URL` 中填写自己的 MiroTalk P2P 服务地址。服务端需允许 Socket.IO 客户端接入，并提供与当前 MiroTalk 版本一致的信令事件。

## 构建

使用 Android Studio 打开此目录，安装 Android SDK 35 后执行 `:app:assembleDebug`。本机当前未安装 Gradle 命令，因此无法在此环境执行构建。

## 已实现

- 相机和麦克风运行时权限
- 入会页：房间名和昵称
- 原生本地音视频采集
- 麦克风、摄像头和挂断控制
- 参会人页面
- 深色 Webex 风格控制栏与深红色挂断按钮

MiroTalk 的官方 JS 客户端本身不能在纯原生 Android 进程中直接执行；本工程保留了原生协议适配层，避免以 WebView 套壳。