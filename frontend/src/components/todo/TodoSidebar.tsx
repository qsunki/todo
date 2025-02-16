import { Layout, Button, Typography, Avatar } from 'antd';
import { ListTodo, LogOut } from 'lucide-react';
import TodoFilters from './TodoFilters';
import { TodoFilter } from '../../types/todo';

const { Sider } = Layout;
const { Text, Title } = Typography;

interface TodoSidebarProps {
  username: string;
  onLogout: () => void;
  onFiltersChange: (filters: TodoFilter[]) => void;
}

function TodoSidebar({ username, onLogout, onFiltersChange }: TodoSidebarProps) {
  return (
    <Sider
      width={250}
      className="bg-white"
      style={{ borderRight: '1px solid #f0f0f0' }}
    >
      <div className="p-4">
        <div className="flex items-center gap-2 mb-6">
          <ListTodo className="w-6 h-6 text-blue-600" />
          <Title level={4} style={{ margin: 0 }}>Todo</Title>
        </div>

        {/* <Text className="text-gray-500 mb-2 block">보기 목록</Text> */}
        
        {/* <div className="space-y-2 mb-6">
          <Button type="text" className="w-full text-left flex items-center gap-2">
            <Star className="w-4 h-4" />
            모든 할 일
            <Badge count={10} className="ml-auto" />
          </Button>
          <Button type="text" className="w-full text-left flex items-center gap-2">
            <Star className="w-4 h-4" />
            중요한 일
            <Badge count={3} className="ml-auto" />
          </Button>
          <Button type="text" className="w-full text-left flex items-center gap-2">
            <Clock className="w-4 h-4" />
            이번 주 마감
            <Badge count={7} className="ml-auto" />
          </Button>
          <Button type="text" className="w-full text-left">
            + 새 보기 추가
          </Button>
        </div> */}

        <TodoFilters onFiltersChange={onFiltersChange} />
      </div>

      <div className="absolute bottom-0 w-full border-t p-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Avatar>{username[0]}</Avatar>
            <Text>{username}</Text>
          </div>
          <Button 
            type="text" 
            icon={<LogOut className="w-4 h-4" />}
            onClick={onLogout} 
          />
        </div>
      </div>
    </Sider>
  );
}

export default TodoSidebar; 