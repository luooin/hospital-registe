<template>
	<view class="addVisitorsBox">
		<view class="visitorsInfo">
			<view class="title">评价内容</view>
			<view class="value">
				<u-textarea placeholder="请输入评价内容" v-model="form.evaluateContent"></u-textarea>
				
			</view>
		</view>
		<view class="addButton" @tap="handleEvaluate()">
			提交
		</view>
	</view>
</template>

<script>
	import request from '@/utils/request.js'
	export default {
		data() {
			return {
				form: {
					userId: uni.getStorageSync('userInfo').userId,
					evaluateContent: '',
					doctorId: null,
					registrationId: null,
				},
				//默认建档方式
				filingWay: 0,
				//是否显示号码类型选择
				showCardType: false,
				//号码类型
				cardTypeList: [
					['就诊卡', '医保卡']
				],
				//绑卡类型
				cardType: null,
				//就诊人信息
				userInfo: {}
			}
		},
		onLoad(e) {
			this.form.doctorId = e.doctorId
			this.form.registrationId = e.id
		},
		methods: {
			handleEvaluate(){
				if(this.form.evaluateContent === '' || this.form.evaluateContent === null){
					uni.showToast({
						title: '内容不能为空',
						icon:'none'
					})
					return
				}
				
				request.addEvaluate(this.form).then(res => {
					if (res && res.code === 200) {
						uni.showToast({
							title: res.msg,
							icon: 'success',
						})
						uni.navigateTo({
							url: '/pages/nucleicAcidPayStatus/nucleicAcidPayStatus'
						})
					}
				})
				
			},
			confirm(res) {
				this.cardType = res.value[0]
				this.showCardType  = false
			}
		}
	}
</script>

<style scoped lang="scss">
	.addVisitorsBox {
		padding:30rpx 40rpx;
		.visitorsInfo{
			.title{
				font-weight: 400;
				line-height: 56rpx;
				color: #343434;
				font-size: 40rpx;
			}
			.value{
				margin-top: 30rpx;
			}
		}
		.filingWay{
			font-weight: 400;
			line-height: 56rpx;
			color: #343434;
			margin-top: 30rpx;
			font-size: 40rpx;
			.cardType{
				display: flex;
				justify-content: space-between;
				padding: 9rpx 6rpx;
				font-size: 30rpx;
				margin-top: 30rpx;
				color: #CBCBCB;
				border: 1px solid #E4E4E2;
				border-radius: 8rpx;
			}
			.value{
				margin-top: 30rpx;
			}
		}
		.addButton {
			width: 490rpx;
			height: 84rpx;
			margin: 100rpx auto 0;
			background: linear-gradient(90deg, #5088FD 0%, #62B9F9 100%);
			border-radius: 12rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			color: #fff;
			font-size: 40rpx;
			letter-spacing: 4rpx;
		}
	}
</style>
