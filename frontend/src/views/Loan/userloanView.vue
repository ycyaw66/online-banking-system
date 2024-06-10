<template>
    <div class="page-container">
        <div class="submit-button-container">
            <el-button type="primary" @click="submitForm">提交(Submit)</el-button>
        </div>
        <div class="loan-application-form">
            <el-form ref="loan_form" :model="loan_data" :rules="rules" label-position="top">
                <el-form-item label="银行卡号(Bank Card Number)" prop="card_id">
                    <el-input v-model="loan_data.card_id" />
                </el-form-item>
                <el-form-item label="姓名(Name)" prop="form_data.user_name">
                    <el-input v-model="loan_data.form_data.user_name" />
                </el-form-item>
                <el-form-item label="身份证号(ID Number)" prop="form_data.id_number">
                    <el-input v-model="loan_data.form_data.id_number" />
                </el-form-item>
                <el-form-item label="性别(Gender)" prop="form_data.gender">
                    <el-select v-model="loan_data.form_data.gender" placeholder="请选择性别">
                        <el-option label="男" value="male"></el-option>
                        <el-option label="女" value="female"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="婚姻情况(Marital Status)" prop="form_data.emotion">
                    <el-select v-model="loan_data.form_data.emotion" placeholder="请选择婚姻情况">
                        <el-option label="已婚" value="married"></el-option>
                        <el-option label="未婚" value="single"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="年收入(Annual Income)" prop="form_data.income">
                    <el-input v-model="loan_data.form_data.income" />
                </el-form-item>
                <el-form-item label="住宅地址(Residential Address)" prop="form_data.address">
                    <el-input v-model="loan_data.form_data.address" />
                </el-form-item>
                <el-form-item label="电话号码(Phone Number)" prop="form_data.phone_number">
                    <el-input v-model="loan_data.form_data.phone_number" />
                </el-form-item>
                <el-form-item label="电子邮件地址(Email Address)" prop="form_data.email">
                    <el-input v-model="loan_data.form_data.email" />
                </el-form-item>
                <el-form-item label="教育背景(Education Background)" prop="form_data.education">
                    <el-input v-model="loan_data.form_data.education" />
                </el-form-item>
                <el-form-item label="贷款用途(Loan Purpose)" prop="form_data.purpose">
                    <el-input v-model="loan_data.form_data.purpose" />
                </el-form-item>
                <el-form-item label="个人情况声明(Personal Statement)" prop="form_data.statement">
                    <el-input type="textarea" :autosize="{ minRows: 7, maxRows: 14 }" v-model="loan_data.form_data.statement" />
                </el-form-item>
                <el-form-item label="贷款金额(Loan Amount)" prop="amount">
                    <el-input v-model="loan_data.amount" />
                </el-form-item>
                <el-form-item label="贷款期限(Loan Term)" prop="term">
                    <el-input v-model="loan_data.term" />
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import { ElForm, ElFormItem, ElInput, ElButton, ElSelect, ElOption } from 'element-plus';

    export default {
        components: {
            ElForm, ElFormItem, ElInput, ElButton, ElSelect, ElOption
        },
        data() {
            return {
                loan_data: {
                    card_id: '',
                    form_data: {
                        user_name: '',
                        id_number: '',
                        gender: '',
                        emotion: '',
                        income: '',
                        address: '',
                        phone_number: '',
                        email: '',
                        education: '',
                        purpose: '',
                        statement: ''
                    },
                    amount: '',
                    term: '',
                    officer_id: '' //这里需要算法实现随机分给符合权限的officer！！！
                    //borrow_id也还未实现
                },
                rules: {
                    card_id: [{ required: true, message: '请输入银行卡号', trigger: 'blur' }],
                    form_data: {
                        user_name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
                        id_number: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
                        gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
                        emotion: [{ required: true, message: '请选择婚姻情况', trigger: 'change' }],
                        income: [{ required: true, message: '请输入年收入', trigger: 'blur' }],
                        address: [{ required: true, message: '请输入住宅地址', trigger: 'blur' }],
                        phone_number: [{ required: true, message: '请输入电话号码', trigger: 'blur' }],
                        email: [{ required: true, message: '请输入电子邮件地址', trigger: 'blur' }],
                        education: [{ required: true, message: '请输入教育背景', trigger: 'blur' }],
                        purpose: [{ required: true, message: '请输入贷款用途', trigger: 'blur' }],
                        statement: [{ required: true, message: '请输入个人情况声明', trigger: 'blur' }]
                    },
                    amount: [{ required: true, message: '请输入贷款金额', trigger: 'blur' }],
                    term: [{ required: true, message: '请输入贷款期限', trigger: 'blur' }]
                }
            };
        },
        methods: {
            async submitForm() {
                this.$refs.loan_form.validate(async (valid) => {
                    if (valid) {
                        try {
                            const formData = {
                                user_name: this.loan_data.form_data.user_name,
                                id_number: this.loan_data.form_data.id_number,
                                gender: this.loan_data.form_data.gender,
                                emotion: this.loan_data.form_data.emotion,
                                income: this.loan_data.form_data.income,
                                address: this.loan_data.form_data.address,
                                phone_number: this.loan_data.form_data.phone_number,
                                email: this.loan_data.form_data.email,
                                education: this.loan_data.form_data.education,
                                purpose: this.loan_data.form_data.purpose,
                                statement: this.loan_data.form_data.statement
                            };

                            // Submit the form data and get the form_id
                            const formResponse = await this.$axios.post('/add-form', formData);
                            const form_id = formResponse.data.id; // Assuming the response contains the form_id

                            // Prepare loan data with the form_id
                            const loanData = {
                                card_id: this.loan_data.card_id,
                                amount: this.loan_data.amount,
                                term: this.loan_data.term,
                                officer_id: this.loan_data.officer_id,
                                form_id: form_id // Include the form_id in the loan data
                            };

                            // Submit the loan data
                            await this.$axios.post('/add-loan', loanData);

                            this.$message.success('贷款申请已提交成功！');
                        } catch (error) {
                            console.error('提交贷款申请时发生错误:', error);
                            this.$message.error('提交失败。');
                        }
                    } else {
                        this.$message.error('请完整填写表单');
                        return false;
                    }
                });
            }
        }
    }
</script>

<style scoped>
    .page-container {
        position: relative;
        max-width: 1000px;
        margin: 20px auto;
        padding: 20px;
        background-color: #fff;
        border: 1px solid #ccc;
        border-radius: 15px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        box-sizing: border-box;
    }

    .loan-application-form {
        padding: 20px;
        max-height: calc(100vh - 100px); /* 设置最大高度 */
        overflow-y: auto; /* 添加垂直滚动条 */
    }

    .submit-button-container {
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 1000;
    }

    .el-form-item {
        margin-bottom: 30px;
    }

    .el-input {
        width: 100%;
    }

    .el-button {
        width: 150px;
    }
</style>
