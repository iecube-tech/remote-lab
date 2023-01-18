<template>
    <div class="hello-ezuikit-js">
        <div id="video-container" style="width:600px;height:400px"></div>
    </div>
</template>

<script>
import EZUIKit from "ezuikit-js";
// import axios from 'axios'
var player = null;

export default {
    name: "HelloWorld",
    props: {
        msg: String
    },
    mounted: () => {
        axios.post('https://open.ys7.com/api/lapp/token/get', {
            "appKey": "5714850ab2174d868eb99e8916099e1f",
            "appSecret": "b78c1c789ada73f50d91e7021c7e4b94"
        }, { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }).then(function (res) {
            console.log(res.data.data.accessToken)
            var accessToken = res.data.data.accessToken
            player = new EZUIKit.EZUIKitPlayer({
                id: 'video-container', // 视频容器ID
                accessToken: accessToken,
                url: 'ezopen://open.ys7.com/K93747614/1.hd.live',
                // simple - 极简版; pcLive-pc直播；pcRec-pc回放；mobileLive-移动端直播；mobileRec-移动端回放;security - 安防版;voice-语音版;
                // 使用自定义模板
                template: '92d46ddc8f45417ab373131145645794',
                // plugin: ['talk'], // 加载插件，talk-对讲
                width: 600,
                height: 400,
            });
            window.player = player;
        }).catch(function (error) {
            console.log(error);
        });
    },
};
</script>
