import { Form, Input, Button, Card, Typography, Divider, message } from 'antd';
import { LockIcon, UserIcon, LogInIcon } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';

const { Title, Text } = Typography;

interface LoginForm {
  username: string;
  password: string;
}

function Login() {
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [messageApi, contextHolder] = message.useMessage();

  const onFinish = async (values: LoginForm) => {
    try {
      await axios.post('http://localhost:8080/api/login', {
        username: values.username,
        password: values.password
      }, {
        withCredentials: true  // CORS 요청에서 쿠키를 주고받기 위해 필요
      });
      
      messageApi.success('로그인 성공!');
      navigate('/home');
    } catch (error) {
      if (axios.isAxiosError(error)) {
        messageApi.error(error.response?.data?.message || '로그인에 실패했습니다');
      } else {
        messageApi.error('로그인 중 오류가 발생했습니다');
      }
      console.error('로그인 오류:', error);
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
          <LogInIcon className="w-12 h-12 text-blue-500 mx-auto mb-4" />
          <Title level={2} style={{ marginBottom: 8 }}>Welcome Back</Title>
          <Text type="secondary">Please sign in to continue</Text>
        </div>

        <Form
          form={form}
          name="login"
          layout="vertical"
          onFinish={onFinish}
          autoComplete="off"
          requiredMark={false}
        >
          <Form.Item
            name="username"
            rules={[
              { required: true, message: 'Please input your username!' },
              { min: 3, message: 'Username must be at least 3 characters!' }
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
              { min: 3, message: 'Password must be at least 3 characters!' }
            ]}
          >
            <Input.Password
              prefix={<LockIcon className="w-4 h-4 text-gray-400" />}
              placeholder="Password"
              size="large"
            />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" size="large" block>
              Sign In
            </Button>
          </Form.Item>

          <Divider>
            <Text type="secondary">Don't have an account?</Text>
          </Divider>

          <Link to="/signup">
            <Button type="link" block>
              Create an account
            </Button>
          </Link>
        </Form>
      </Card>
    </div>
  );
}

export default Login;