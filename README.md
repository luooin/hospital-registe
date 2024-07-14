# hospital-registe🎂
基于微信小程序的医院挂号系统
#### 介绍🌞

> 本系统分为后台管理系统和微信小程序前台两个分类，其中，后台管理系统又分为SpringBoot后端和Vue前端。后台管理系统实现了分管理员和用户双角色登录，对科室信息、挂号信息等信息进行管理，对就诊情况发表评论和用户信息的修改等功能。前台微信小程序实现了预约挂号、余额充值,显示充值记录和医生排班等信息的功能。

**有偿获取完整源码或调试代码+🐧：1902317191**

**CSDN项目合集：http://t.csdnimg.cn/Q4u84**

#### 软件架构🌞

  1. 后台管理系统有用户登录功能、个人信息的增删改查、科室，门诊，医院排班，预约挂号的信息管理、黑名单和评论管理等功能。
  2. 微信小程序前台则有用户登录、预约挂号、查看我的评论,金额充值等功能。

#### 安装教程🌞

1. 运行环境准备mysql8 + java8 + node14.16.1

2. 配置maven路径，加载依赖

3. 运行sql文件，确保application.yml的数据库名称和账号密码是数据库所在主机的账号密码

#### 使用说明🌞

1. 登入

> 管理员账号：admin	密码：123456
>
> 医生账号：doctor	密码：123456
>
> 微信小程序登录会识别开发者工具的信息，但是需要在`SysLoginController`进行配置`appid`和`secret`

2. 运行流程

>  SpringBoot+Vue项目的部署详情可以查看这篇CSDN博客：http://t.csdnimg.cn/kpuxS
>
>  前后端不分离项目的部署流程可以查看这篇CSDN博客：http://t.csdnimg.cn/CslA5

#### 项目演示🌞

**管理员**

![GIF 2024-7-14 13-16-10](https://github.com/user-attachments/assets/b206259d-ed4a-43d8-925a-1c0ce2c9eddc)

![GIF 2024-7-14 13-17-24](https://github.com/user-attachments/assets/875d2b48-60c3-4580-8b3c-d9b97945375f)

**医生**

![GIF 2024-7-14 13-19-35](https://github.com/user-attachments/assets/56288bbe-2252-4a72-81a2-fff0da2c49b8)

**小程序**

![GIF 2024-7-14 13-21-44](https://github.com/user-attachments/assets/cf59d1fd-070c-4239-9f3f-4fc4e997986d)



#### 参与贡献🌞

    1.  Fork 本仓库
    2.  新建 Feat_xxx 分支
    3.  提交代码
    4.  新建 Pull Request


#### 特技🌞

    1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
    2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
    3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
    4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
    5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
    6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
