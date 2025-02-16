import { useState, useEffect, useCallback } from 'react';
import { Button, Layout, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import TodoSidebar from '../components/todo/TodoSidebar';
import TodoTable from '../components/todo/TodoTable';
import { Todo, TodoFilter, PropertyData } from '../types/todo';
import { List, Repeat } from 'lucide-react';

const { Content, Header } = Layout;

function Home() {
  const navigate = useNavigate();
  const [messageApi, contextHolder] = message.useMessage();
  const [username, setUsername] = useState("");
  const [todos, setTodos] = useState<Todo[]>([]);
  const [loading, setLoading] = useState(false);
  const [currentFilters, setCurrentFilters] = useState<TodoFilter[]>([]);

  useEffect(() => {
    const loadUserInfo = async () => {
      try {
        const response = await api.get('/users/me');
        setUsername(response.data.username);
      } catch (error) {
        console.error('사용자 정보 로드 실패:', error);
        messageApi.error('사용자 정보 로드 실패');
        navigate('/login');
      }
    };
    loadUserInfo();
  }, [messageApi, navigate]);


  const handleLogout = async () => {
    try {
      await api.get('/logout');
      navigate('/login');
    } catch (error) {
      console.error('로그아웃 중 오류가 발생했습니다:', error);
      messageApi.error('로그아웃 중 오류가 발생했습니다');
    }
  };

  const loadTodos = useCallback(async (filters: TodoFilter[] = []) => {
    try {
      setLoading(true);
      const response = await api.post('/todos/query', { filters });
      setTodos(response.data);
    } catch (error) {
      console.error('할 일 목록 로드 실패:', error);
      messageApi.error('할 일 목록 로드에 실패했습니다');
    } finally {
      setLoading(false);
    }
  }, [messageApi]);

  useEffect(() => {
    loadTodos(currentFilters);
  }, [loadTodos, currentFilters]);

  useEffect(() => {
    if (currentFilters.length > 0) {
      loadTodos(currentFilters);
    }
  }, [loadTodos, currentFilters]);

  const handleFiltersChange = useCallback((filters: TodoFilter[]) => {
    setCurrentFilters(filters);
  }, []);

  const handleEditTodo = async (todo: Todo) => {
    try {
      await api.patch(`/todos/${todo.id}`, {
        type: 'content',
        content: todo.content
      });
      messageApi.success('할 일이 수정되었습니다');
      loadTodos(currentFilters);
    } catch (error) {
      console.error('할 일 수정 실패:', error);
      messageApi.error('할 일 수정에 실패했습니다');
    }
  };

  const handleDeleteTodo = async (id: number) => {
    try {
      await api.delete(`/todos/${id}`);
      messageApi.success('할 일이 삭제되었습니다');
      loadTodos(currentFilters);
    } catch (error) {
      console.error('할 일 삭제 실패:', error);
      messageApi.error('할 일 삭제에 실패했습니다');
    }
  };

  const handleToggleComplete = async (id: number) => {
    try {
      const todo = todos.find(t => t.id === id);
      if (!todo) return;

      await api.patch(`/todos/${id}`, {
        type: 'complete',
        complete: !todo.completed
      });

      messageApi.success(
        todo.completed ? '할 일을 미완료로 변경했습니다' : '할 일을 완료했습니다'
      );
      loadTodos(currentFilters);
    } catch (error) {
      console.error('할 일 완료 상태 변경 실패:', error);
      messageApi.error('할 일 완료 상태 변경에 실패했습니다');
    }
  };

  const handleAddTodo = async (content: string) => {
    try {
      await api.post('/todos', { content });
      messageApi.success('할 일이 추가되었습니다');
      loadTodos(currentFilters);
    } catch (error) {
      console.error('할 일 추가 실패:', error);
      messageApi.error('할 일 추가에 실패했습니다');
    }
  };

  const handleUpdateProperty = async (todoId: number, propertyName: string, data: PropertyData) => {
    try {
      await api.post(`/todos/${todoId}/properties`, {
        todoId,
        name: propertyName,
        type: propertyName === '마감일' ? 'DATE' : 'SELECT',
        data
      });
      messageApi.success(
        propertyName === '마감일' ? '마감일이 설정되었습니다' : '우선순위가 설정되었습니다'
      );
      loadTodos(currentFilters);
    } catch (error) {
      console.error(`${propertyName} 설정 실패:`, error);
      messageApi.error(`${propertyName} 설정에 실패했습니다`);
    }
  };

  return (
    <Layout className="min-h-screen">
      {contextHolder}
      <TodoSidebar 
        username={username} 
        onLogout={handleLogout}
        onFiltersChange={handleFiltersChange}
      />
      <Layout>
        <Header className="bg-white px-6 flex items-center gap-4" style={{ borderBottom: '1px solid #f0f0f0' }}>
          <Button icon={<Repeat className="w-4 h-4" />}>
            반복 할 일
          </Button>
          <Button icon={<List className="w-4 h-4" />}>
            반복 할 일 관리
          </Button>
        </Header>
        <Content className="p-6">
          <TodoTable
            todos={todos}
            onEdit={handleEditTodo}
            onDelete={handleDeleteTodo}
            onToggleComplete={handleToggleComplete}
            onAdd={handleAddTodo}
            loading={loading}
            onUpdateProperty={handleUpdateProperty}
          />
        </Content>
      </Layout>
    </Layout>
  );
}

export default Home;