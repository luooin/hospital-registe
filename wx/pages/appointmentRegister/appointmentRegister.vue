<template>
	<view class="doctorList">
		<view class="doctorItem" v-for="(item,index) in doctorList" :key="index">
			<view class="doctorImg">
				<u-avatar :size='142' :src="item.sysUser.avatar"></u-avatar>
			</view>
			<view class="doctorInfo">
				<view class="top">
					<view class="doctorName">{{item.sysUser.nickName}}</view>
					<view class="doctorPost">{{item.pSection.deptName}} | {{item.sysUser.sex === '0' ? '男' : '女'}} | {{item.sysUser.age}} | {{item.sysUser.rank1}}</view>
				</view>
				<view class="center">医生简介：{{item.sysUser.remark}}</view>
				<view class="bottom">
					<view class="money">
						<view class="label">挂号费:</view>
						<view class="value">¥{{item.sysUser.balance}}</view>
					</view>
					<view class="subscribe" @click="subscribe(item)">
						预约
					</view>
					<view class="subscribe" @click="collect(item,index)">
						{{item.flag === true ? '已收藏' : '收藏'}}
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import request from '@/utils/request.js'
	export default {
		data() {
			return {
				//诊室id
				roomId: null,
				deptId: null,
				searchParameters: {
					pageNum: 1,
					pageSize: 100,
					sectionId: null,
					outpatienId: null,
				},
				//医生列表
				doctorList: [],
			}
		},
		onLoad(options) {
			this.searchParameters.sectionId = options.deptId
			this.searchParameters.outpatienId = options.roomId
			// if (options.item) {
			// 	this.roomId = JSON.parse(options.item).roomId
			// }
			this.getUserList()
		},
		methods: {
			getUserList(){
				request.getSchedulingList(this.searchParameters).then(res => {
					if(res && res.code === 200){
						console.log('length===>',res.rows.length)
						this.doctorList = res.rows
					}
					
				})
			},
			// 收藏
			collect(item,index){
				console.log(item)
				let obj = Object.assign({
					userId: uni.getStorageSync('userInfo').userId,
					doctorId: item.sysUser.userId,
					sectionId: item.pSection.deptId,
					outpatien: item.pOutpatien.roomId,
				})
				request.addCollect(obj).then(res => {
					if(res && res.code === 200){
						if(res.flag){
							uni.showToast({
								title: '收藏成功',
								icon: 'none'
							})
							this.doctorList[index].flag = res.flag
						}else{
							uni.showToast({
								title: '取消成功',
								icon: 'none'
							})
							this.doctorList[index].flag = res.flag
						}
				
					}
				})
			},
			//预约挂号
			subscribe(item){
				uni.navigateTo({
					url:'/pages/subscribeInfo/subscribeInfo?item='+encodeURIComponent(JSON.stringify(item)) + '&type=' + 1
				})
			}
		}
	}
</script>
<style>
	page {
		width: 100%;
		height: 100%;
		background-color: #F8F8F8;
	}
</style>
<style scoped lang="scss">
	.doctorList {
		background-color: #F8F8F8;

		.doctorItem {
			margin-top: 30rpx;
			height: 206rpx;
			background: #FFFFFF;
			display: flex;
			align-items: center;
			padding: 20rpx 40rpx 30rpx;

			.doctorImg {
				width: 142rpx;
				height: 142rpx;
				border: 2px solid #FFFFFF;
				border-radius: 50%;
				box-shadow: 0px 2px 2px 2px #E1E1E1;
				margin-right: 30rpx;
			}

			.doctorInfo {
				display: flex;
				flex-direction: column;
				justify-content: space-around;

				.top {
					display: flex;

					.doctorName {
						font-size: 36rpx;
						color: #5D5C5C;
					}

					.doctorPost {
						margin-left: 20rpx;
						margin-top: 15rpx;
						font-size: 24rpx;
						color: #5D5C5C;
					}
				}

				.center {
					color: #979797;
					font-size: 24rpx;
					line-height: 30rpx;
					padding: 15rpx 0;
				}

				.bottom {
					display: flex;
					justify-content: space-between;

					.money {
						display: flex;
						font-size: 24rpx;
						line-height: 32rpx;
						color: #5D5C5C;

						.value {
							font-size: 32rpx;
							margin-left: 10rpx;
							letter-spacing: 2rpx;
							color: #4F84FD;
						}
					}

					.subscribe {
						width: 116rpx;
						height: 56rpx;
						background: linear-gradient(90deg, #4E84FC 0%, #63BCF7 100%);
						opacity: 1;
						font-size: 32rpx;
						color: #FFFFFF;
						display: flex;
						justify-content: center;
						align-items: center;
						border-radius: 12rpx;
						margin-left: 10px;
					}
				}
			}
		}
	}
</style>
