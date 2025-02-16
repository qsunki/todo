import { Table, Badge, Button, Form, Input, Dropdown, DatePicker } from 'antd';
import { DateData, SelectData, Todo, PropertyData } from '../../types/todo';
import { Plus, Pencil, Calendar } from 'lucide-react';
import dayjs from 'dayjs';
import { useState } from 'react';
import locale from 'antd/es/date-picker/locale/ko_KR';

interface TodoTableProps {
  todos: Todo[];
  onEdit: (todo: Todo) => void;
  onDelete: (id: number) => void;
  onToggleComplete: (id: number) => void;
  onAdd: (content: string) => void;
  loading: boolean;
  onUpdateProperty?: (todoId: number, propertyName: string, data: PropertyData) => void;
}

function TodoTable({ todos, onEdit, onDelete, onToggleComplete, onAdd, loading, onUpdateProperty }: TodoTableProps) {
  const [form] = Form.useForm();
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editForm] = Form.useForm();

  const handleAdd = () => {
    const content = form.getFieldValue('content');
    if (content) {
      onAdd(content);
      form.resetFields();
    }
  };

  const getProperty = (todo: Todo, name: string) => {
    return todo.properties.find(prop => prop.name === name);
  };

  const handleEditEnd = (record: Todo, newContent: string) => {
    if (newContent && newContent !== record.content) {
      onEdit({ ...record, content: newContent });
    }
    setEditingId(null);
  };

  const priorityOptions = [
    { label: '높음', value: '상', color: 'red' },
    { label: '중간', value: '중', color: 'gold' },
    { label: '낮음', value: '하', color: 'blue' }
  ];

  const columns = [
    {
      title: '완료',
      key: 'completed',
      width: 60,
      render: (_: unknown, record: Todo) => (
        <input 
          type="checkbox" 
          checked={record.completed} 
          onChange={() => onToggleComplete(record.id)} 
        />
      ),
    },
    {
      title: '제목',
      dataIndex: 'content',
      key: 'content',
      render: (content: string, record: Todo) => {
        const isEditing = record.id === editingId;
        
        if (isEditing) {
          return (
            <Form 
              form={editForm}
              initialValues={{ content }}
            >
              <Form.Item 
                name="content"
                style={{ margin: 0 }}
              >
                <Input 
                  autoFocus
                  onBlur={() => {
                    const newContent = editForm.getFieldValue('content');
                    handleEditEnd(record, newContent);
                  }}
                  onPressEnter={(e) => {
                    const newContent = (e.target as HTMLInputElement).value;
                    handleEditEnd(record, newContent);
                  }}
                />
              </Form.Item>
            </Form>
          );
        }

        return (
          <div className="group flex items-center gap-2">
            <span 
              className="cursor-pointer hover:text-blue-600"
              onClick={() => {
                setEditingId(record.id);
                editForm.setFieldsValue({ content });
              }}
            >
              {content}
            </span>
            <Pencil 
              className="w-4 h-4 text-gray-400 opacity-0 group-hover:opacity-100 cursor-pointer hover:text-blue-600 transition-opacity"
              onClick={() => {
                setEditingId(record.id);
                editForm.setFieldsValue({ content });
              }}
            />
          </div>
        );
      }
    },
    {
      title: '마감일',
      key: 'deadline',
      width: 200,
      render: (_: unknown, record: Todo) => {
        const deadline = getProperty(record, '마감일');
        
        if (!deadline) {
          return (
            <Dropdown
              trigger={['click']}
              dropdownRender={() => (
                <div className="bg-white p-2 rounded shadow-lg">
                  <DatePicker 
                    locale={locale}
                    placeholder="마감일 선택"
                    onChange={(date) => {
                      if (date) {
                        onUpdateProperty?.(record.id, '마감일', {
                          type: 'date',
                          start: date.format('YYYY-MM-DDTHH:mm:ss'),
                          end: null
                        });
                      }
                    }}
                    allowClear={false}
                  />
                </div>
              )}
            >
              <div className="flex items-center gap-2 cursor-pointer text-gray-400 hover:text-blue-600">
                <Calendar className="w-4 h-4" />
                <span>마감일 설정</span>
              </div>
            </Dropdown>
          );
        }

        if (deadline.type !== 'DATE') return '-';
        const data = deadline.data as DateData;
        return data.start ? dayjs(data.start).format('YYYY-MM-DD') : '-';
      }
    },
    {
      title: '우선순위',
      key: 'priority',
      width: 100,
      render: (_: unknown, record: Todo) => {
        const priority = getProperty(record, '우선순위');
        
        if (!priority) {
          return (
            <Dropdown
              menu={{
                items: priorityOptions.map(opt => ({
                  key: opt.value,
                  label: (
                    <div className="flex items-center gap-2">
                      <Badge color={opt.color} text={opt.label} />
                    </div>
                  ),
                  onClick: () => onUpdateProperty?.(record.id, '우선순위', {
                    type: 'select',
                    name: opt.value,
                    color: opt.color
                  } as PropertyData)
                }))
              }}
              trigger={['click']}
            >
              <div className="cursor-pointer text-gray-400 hover:text-blue-600">
                우선순위
              </div>
            </Dropdown>
          );
        }

        if (priority.type !== 'SELECT') return '-';
        const data = priority.data as SelectData;
        
        return (
          <Badge 
            color={data.color} 
            text={data.name}
          />
        );
      }
    },
    {
      title: '작업',
      key: 'actions',
      width: 80,
      render: (_: unknown, record: Todo) => (
        <Button 
          type="text" 
          danger 
          size="small"
          onClick={() => onDelete(record.id)}
        >
          삭제
        </Button>
      ),
    }
  ];

  return (
    <div className="bg-white rounded-lg">
      <Table 
        loading={loading}
        columns={columns} 
        dataSource={todos}
        pagination={false}
        className="mb-4"
        rowKey="id"
      />
      <div className="px-4 py-3 border-t">
        <Form form={form} className="flex gap-2">
          <Form.Item 
            name="content" 
            className="mb-0 flex-1"
          >
            <Input placeholder="새로운 할 일" />
          </Form.Item>
          <Button 
            type="primary"
            icon={<Plus className="w-4 h-4" />}
            onClick={handleAdd}
          >
            추가
          </Button>
        </Form>
      </div>
    </div>
  );
}

export default TodoTable; 