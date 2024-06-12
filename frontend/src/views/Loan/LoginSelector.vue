<template>
    <div class="login-selector">
      <h1>贷款系统工作人员登录选择界面</h1>
      <div class="button-container">
        <button @click="goToLogin('officer')">登录 Officer</button>
        <button @click="goToLogin('manager')">登录 Manager</button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'LoginSelector',
    methods: {
      goToLogin(type) {
        this.$router.push(`/${type}-login`);
      }
    }
  }
  </script>
  
  <style scoped>
  html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
}
#app {
  height: 100%;
  width: 100%;
}
  
  .login-selector {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 100%;
  background-image: url('@/assets/background.jpg'); /* 使用上传的图片 */
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center center;
  background-attachment: fixed;
  text-align: center;
  color: #fff;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}
  
  .login-selector h1 {
    color: #fff;
    margin-bottom: 20px;
    font-size: 2.5rem;
  }
  
  .button-container {
    display: flex;
    gap: 20px;
  }
  
  button {
    padding: 15px 30px;
    font-size: 1.2rem;
    color: #fff;
    background-color: #007bff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }
  
  button:hover {
    background-color: #0056b3;
  }
  </style>
  