<template>
	<view class="cardPayBox">
		<view class="userInfo">
			<view class="top">
				<view class="name">{{userInfo.nickName}}</view>
				<view class="sex">{{userInfo.sex === '0' ? '男':'女'}}</view>
			</view>
			<view class="bottom">
				<view class="balance">卡余额:¥{{userInfo.balance}}</view>
			</view>
		</view>
		<view class="payButton" @click="submitPay">确认充值</view>
		<view class="moneyList">
			<view :class="moneyIndex == index? 'moneyItem activeItem' :'moneyItem'" @click="changeMoney(item,index)"
				v-for="(item,index) in moneyList" :key="index">金额:
				<view class="money">{{item}}元</view>
			</view>
			<view :class="moneyIndex == 5? 'moneyItem activeItem' :'moneyItem'" @click="moneyOther">
				<input  type="number" border="0" class="input" v-model="payMoney" placeholder="其他" placeholderStyle="color:#535353" color='#FFFFFF' v-if="moneyIndex!=5" @click="moneyOther"></input>
				<input  type="number" class="input" border="0" v-model="payMoney" placeholder="其他" placeholderStyle="color:#FFFFFF" v-else @click="moneyOther"></input>
			</view>
		</view>
	</view>
</template>

<script>
	import request from '@/utils/request.js'
	export default {
		data() {
			return {
				//选中金额索引
				moneyIndex: 0,
				//充值金额
				payMoney: null,
				//可充值金额列表
				moneyList: ['100', '300', '500', '800', '1000'],
				userInfo: {}
			}
		},
		onLoad() {
			this.userInfo = uni.getStorageSync('userInfo')
		},
		methods: {
			// 确认充值
			submitPay() {
				let moneyVlaue = 0
				if (this.moneyIndex == 5) {
					if (this.payMoney) {
						moneyVlaue = this.payMoney
					} else {
						return uni.showToast({
							icon: "none",
							title: '请输入或选择正确的金额!'
						})
					}
				} else {
					moneyVlaue = this.moneyList[this.moneyIndex]
				}
				let userInfo = uni.getStorageSync('userInfo')
				let obj = Object.assign({
					userId: userInfo.userId,
					price: moneyVlaue
				})
				console.log(obj)
				request.addPay(obj).then(res => {
					if(res && res.code === 200){
						request.getUserByUserName(userInfo.userName).then(res1 => {
							if (res1 && res1.code === 200) {
								let userInfo = res1.userInfo
								uni.setStorageSync('userInfo', userInfo)
								uni.showToast({
									icon: "none",
									title: '您已充值' + moneyVlaue + '元'
								})
								this.userInfo = res1.userInfo
								
							}
						})
					}
				})
			},
			moneyOther() {
				this.moneyIndex = 5
				this.payMoney = ''
			},
			//修改选中金额
			changeMoney(item, index) {
				this.moneyIndex = index
				this.payMoney = null
			}
		}
	}
</script>

<style scoped lang="scss">
	.cardPayBox {
		position: relative;

		.userInfo {
			height: 296rpx;
			padding: 0 64rpx;
			background: linear-gradient(310deg, #63BCF7 0%, #4E82FD 100%);

			.top {
				display: flex;
				padding: 20rpx 0;
				color: #FFFFFF;

				.name {
					font-size: 40rpx;
				}

				.sex {
					align-self: center;
					margin-left: 20rpx;
					font-size: 32rpx;
				}
			}

			.bottom {
				width: 520rpx;
				height: 78rpx;
				background: rgba(255, 255, 255, 0.39);
				opacity: 0.55;
				color: #134F9B;
				font-size: 32prx;
				display: flex;
				flex-direction: column;
				justify-content: space-between;
				border-radius: 12rpx;
				padding: 20rpx 50rpx;
			}
		}

		.payButton {
			position: absolute;
			top: 250rpx;
			left: 50%;
			width: 390rpx;
			height: 96rpx;
			background: linear-gradient(90deg, #4F84FD 0%, #61B8F8 100%);
			border-radius: 100rpx;
			transform: translateX(-50%);
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
			border: 20rpx solid #FFFFFF;
		}

		.moneyList {
			margin-top: 120rpx;
			padding: 0 30rpx;
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;

			.moneyItem {
				width: 214rpx;
				height: 128rpx;
				border-radius: 8rpx;
				margin-bottom: 20rpx;
				border-left: 1px solid #F8F8F8;
				box-shadow: 5px 5px 5px #F8F8F8;
				display: flex;
				align-items: center;
				justify-content: center;
				font-size: 28rpx;
				color: #535353;

				.money {
					font-size: 36rpx;
				}

				.input {
					width: 100%;
					height: 100%;
					text-align: center;
					font-size: 36rpx;
				}
			}

			.activeItem {
				background: linear-gradient(132deg, #4E83FD 0%, #62BBF7 100%);
				color: #FFFFFF;
			}
		}
	}
</style>
