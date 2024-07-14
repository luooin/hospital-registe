<template>
	<view class="addVisitorsBox">
		<view class="visitorsInfo">
			<view class="title">就诊人信息</view>
			<view class="value">
				<u-input placeholder="请输入就诊人姓名" v-model="userInfo.nickName"></u-input>
				
			</view>
			<view class="value">
				<u-input placeholder="请输入就诊人身份证号" v-model="userInfo.remark"></u-input>
				
			</view>
			<view class="value">
				<u-input placeholder="请输入就诊人性别" v-model="sex"></u-input>
				
			</view>
			<view class="value">
			<u-input placeholder="请输入联系方式" v-model="userInfo.phonenumber"></u-input>
				
			</view>
		</view>
		<view class="addButton" @tap="handleUpdate()">
			确认修改
		</view>
		
		<u-picker :show="show" :columns="columns" @change="changeHandler" @confirm="confirm" @cancel="cancel"></u-picker>
		
	</view>
</template>

<script>
	import request from '@/utils/request.js'
	export default {
		data() {
			return {
				sex: '',
				show: false,
				columns: [
					['男','---','女']
				],
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
				userInfo: {

				}
			}
		},
		onLoad() {
			this.userInfo = uni.getStorageSync('userInfo')
			this.userInfo.sex === '0' ? this.sex = '男' : this.sex = '女'
		},
		methods: {
			changeHandler(e) {
				console.log(e)
			            },
			toSelectSex(){
				this.show = true
			},
			confirm(e) {
				//console.log('confirm', e)
				this.show = false
			},
			cancel(e) {
				console.log('confirm', e)
				this.show = false
			},
			handleUpdate(){
				if(this.userInfo.nickName === '' || this.userInfo.nickName === null){
					uni.showToast({
						title: '姓名不能为空',
						icon:'none'
					})
					return
				}
				
				if(this.userInfo.remark === '' || this.userInfo.remark === null){
					uni.showToast({
						title: '身份证不能为空',
						icon:'none'
					})
					return
				}
				if(this.sex === ''){
					ni.showToast({
						title: '性别不能为空',
						icon:'none'
					})
					return
				}
				if(this.userInfo.phonenumber === '' || this.userInfo.phonenumber === null){
					uni.showToast({
						title: '联系方式不能为空',
						icon:'none'
					})
					return
				}
				//this.sex === '男' ? this.userInfo.sex = '0' : '1'
				if(this.sex === '男'){
					this.userInfo.sex = '0'
				}else{
					this.userInfo.sex = '1'
				}
				request.editUserInfo(this.userInfo).then(res => {
					if (res && res.code === 200) {
						uni.showToast({
							title: res.msg,
							icon: 'success',
						})
						request.getUserByUserName(this.userInfo.userName).then(res1 => {
							if (res1 && res1.code === 200) {
								let userInfo = res1.userInfo
								uni.setStorageSync('userInfo', userInfo)
							}
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
