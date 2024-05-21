<template>
    <div>
        用户姓名
    </div>
    <div>
        <el-text>{{ this.$store.state.person.name }}</el-text>
    </div>
    <div>
        <el-button type="primary" @click="jump">
            跳转到界面1
        </el-button>
    </div>
</template>

<script>

export default {
    data() {
        return {

        }
    },
    methods: {
        jump() {
            this.$router.push('/creditCard/firstView')
        }
    },
    mounted() {

    }
}

</script>

<style></style>