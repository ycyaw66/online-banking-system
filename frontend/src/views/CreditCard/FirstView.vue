<template>
    <div>
        用户id
    </div>
    <div>
        <el-text>{{ this.$store.state.person.id }}</el-text>
    </div>
    <div>
        <el-button type="primary" @click="jump">
            跳转到界面2
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
            this.$router.push('/creditCard/secondview');
        }
    },
    mounted() {

    }
}

</script>

<style></style>