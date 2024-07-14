<template>
	<view>
		<view class="images">
			<view class="imageItem" v-for="(item,index) in images" :key="index">
				<view class="step">第 {{index+1}} 步</view>
				<image :src="item" mode="widthFix"></image>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				images:[]
			}
		},
		onLoad(options) {
			//获取页面跳转携带过来的参数
			if(options.data){
				let data = JSON.parse(decodeURIComponent(options.data))
				this.images = data.images
			}
		},
		methods: {
			
		}
	}
</script>

<style scoped lang="less">
.images{
	height: 100%;
	padding: 20px;
	.imageItem{
		width: 100%;
		.step{
			padding: 10px 0;
		}
		image{
			width: 100%;
		}
	}
}
</style>
