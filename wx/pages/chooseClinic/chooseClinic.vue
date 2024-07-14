<template>
	<view class="contentBox">
		<view class="leftBox">
			<view v-for="(item,index) in deptList" :key="index" :class="selectIndex==index?'item active':'item'"
				@click="changeDept(item,index)">
				<view class="name"> {{item.deptName}}</view>
			</view>
		</view>
		<view class="rightBox">
			<view v-for="(item,index) in outpatientList" :key="index" class="item"
				@click="registration(item,index)">
				<view class="name"> {{item.roomName}}</view>
				<u-icon name="arrow-right" color="#D6D6D6" size="36"></u-icon>
			</view>
		</view>
	</view>
</template>

<script>
	import request from '@/utils/request.js'
	export default {
		data() {
			return {
				searchParameters: {
					pageNum: 1,
					pageSize: 100,
				},
				//选中科室索引
				selectIndex: 0,
				//科室列表
				deptList: [
				],
				//门诊列表
				outpatientList:[],
				outpatientServiceList:[
				]
			}
		},
		onLoad() {
			this.getSectionList()
		},
		mounted() {
			
			
		},
		methods: {
			getSectionList(){
				request.getSectionList(this.searchParameters).then(res => {
					if(res && res.code === 200){
						console.log('length===>',res.rows.length)
						this.deptList = res.rows
						request.getOutpatienList(this.searchParameters).then(res1 => {
							if(res1 && res1.code === 200){
								console.log('length===>',res1.rows.length)
								this.outpatientServiceList = res1.rows
								for (var i = 0; i < this.outpatientServiceList.length; i++) {
									if(this.outpatientServiceList[i].deptId==this.deptList[this.selectIndex].deptId){
										this.outpatientList.push(this.outpatientServiceList[i])
									}
								}
							}
							
						})
						// for (var i = 0; i < this.outpatientServiceList.length; i++) {
						// 	if(this.outpatientServiceList[i].deptId==this.deptList[this.selectIndex].deptId){
						// 		this.outpatientList.push(this.outpatientServiceList[i])
						// 	}
						// }
					}
					
				})
			},
			//预约挂号
			registration(item){
				console.log(item)
				uni.navigateTo({
					url:'/pages/appointmentRegister/appointmentRegister?deptId=' + item.deptId + '&roomId=' + item.roomId
				})
			},
			//切换科室查询医生
			changeDept(item, index) {
				this.selectIndex = index
				this.outpatientList = []
				for (var i = 0; i < this.outpatientServiceList.length; i++) {
					if(this.outpatientServiceList[i].deptId==item.deptId){
						this.outpatientList.push(this.outpatientServiceList[i])
					}
				}
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
	.contentBox {
		height: 100%;
		background-color: #F8F8F8;
		padding: 20rpx 5rpx;
		display: flex;

		.leftBox {
			width: 246rpx;
			height: 100%;
			border-right: 1px solid #E5E5E5;
			border-top: 1px solid #E5E5E5;

			.item {
				text-align: center;
				line-height: 80rpx;
				height: 80rpx;
				letter-spacing: 4rpx;
			}

			.active {
				background-color: #FFFFFF;
				border-left: 12rpx solid #5AA3F9;
			}
		}

		.rightBox {
			flex: 1;
			background-color: #FFFFFF;
			.item{
				border-bottom: 1px solid #E5E5E5;
				display: flex;
				justify-content: space-between;
				align-items: center;
				height: 75rpx;
				line-height: 75rpx;
				padding: 0 40rpx;
				font-size: 32rpx;
				color: #5D5C5C;
			}
		}
	}
</style>
