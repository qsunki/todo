import { Form, Input, Button, Card, Typography, Divider, message } from 'antd';
import { LockIcon, UserIcon, UserPlusIcon } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';

const { Title, Text } = Typography;

interface SignUpForm {
  username: string;
  password: string;
  confirmPassword: string;
}

function SignUp() {
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [messageApi, contextHolder] = message.useMessage();

  const onFinish = async (values: SignUpForm) => {
    try {
      await axios.post('/users', {
        username: values.username,
        password: values.password
      });

      messageApi.success('계정이 생성되었습니다!');
      navigate('/login');
    } catch (error) {
      if (axios.isAxiosError(error)) {
        messageApi.error(error.response?.data?.message || '회원가입 중 오류가 발생했습니다');
      } else {
        messageApi.error('회원가입 중 오류가 발생했습니다');
      }
      console.error('회원가입 오류:', error);
    }
  };

  return (
    <div 
      className="min-h-screen flex items-center justify-center p-4"
      style={{
        background: 'linear-gradient(135deg, #1677ff 0%, #4096ff 100%)',
      }}
    >
      {contextHolder}
      <Card className="w-full max-w-md shadow-2xl">
        <div className="text-center mb-8">
          <UserPlusIcon className="w-12 h-12 text-blue-500 mx-auto mb-4" />
          <Title level={2} style={{ marginBottom: 8 }}>Create Account</Title>
          <Text type="secondary">Join us today!</Text>
        </div>

        <Form
          form={form}
          name="signup"
          layout="vertical"
          onFinish={onFinish}
          autoComplete="off"
          requiredMark={false}
        >
          <Form.Item
            name="username"
            rules={[
              { required: true, message: 'Please input your username!' },
              { min: 3, message: 'Username must be at least 3 characters!' },
              { pattern: /^[a-zA-Z0-9_]+$/, message: 'Username can only contain letters, numbers and underscores!' }
            ]}
          >
            <Input 
              prefix={<UserIcon className="w-4 h-4 text-gray-400" />} 
              placeholder="Username"
              size="large"
            />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[
              { required: true, message: 'Please input your password!' },
              { min: 6, message: 'Password must be at least 6 characters!' }
            ]}
          >
            <Input.Password
              prefix={<LockIcon className="w-4 h-4 text-gray-400" />}
              placeholder="Password"
              size="large"
            />
          </Form.Item>

          <Form.Item
            name="confirmPassword"
            dependencies={['password']}
            rules={[
              { required: true, message: 'Please confirm your password!' },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('password') === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(new Error('The passwords do not match!'));
                },
              }),
            ]}
          >
            <Input.Password
              prefix={<LockIcon className="w-4 h-4 text-gray-400" />}
              placeholder="Confirm Password"
              size="large"
            />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" size="large" block>
              Create Account
            </Button>
          </Form.Item>

          <Divider>
            <Text type="secondary">Already have an account?</Text>
          </Divider>

          <Link to="/login">
            <Button type="link" block>
              Sign in instead
            </Button>
          </Link>
        </Form>
      </Card>
    </div>
  );
}

export default SignUp;